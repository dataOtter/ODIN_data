package reports.rules;

import Assert.Assertion;
import constants.*;
import dao.OneAnswer;
import filters.*;
import orderedcollection.*;
import reports.OneReport;
import sensors.data.SensorDataCollection;
import sensors.data.SensorDataOfOneType;
/**
*
* @author Maisha Jauernig
*/
public abstract class AbsRulePerformanceEval {
	protected final int _cid;
	protected final int _rid;
	
	protected final int _numRuleFiresTotal;
	protected int _goodAnsCount = 0;
	protected int _lateAnsCount = 0;
	protected int _likelyMissedAnsCount = 0;
	protected int _idealWorldNumRuleFires = 0;
	protected int _ruleTrueFilterFalseCount = 0;
	
	protected final double _sensorFireTimeInterval;
	protected final double _minTReq;
	protected double _maxAnsT;
	protected double _trueFireT = 0.0;
	
	protected final AnswersCollection _allAnswers;
	protected final AnswersCollection _answersLeft;
	protected final IMJ_OC<OneAnswer> _lateAns;
	protected final IMJ_OC<OneAnswer> _earlyAns;
	
	protected final GpsDataAdapter _gpsAd;
	private final IMJ_OC<Filter> _filters;
	
	protected AbsRulePerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double sensorFireTimeInterval, int cid, int rid, double minTReq) {
		_cid = cid;
		_rid = rid;
		_sensorFireTimeInterval = sensorFireTimeInterval;
		_minTReq = minTReq;
		
		_filters = rules.getRuleById(_rid).getFilters();

		_allAnswers = answers;
		_answersLeft = answers.getAnsForRuleAndCid(_cid, _rid);
		_numRuleFiresTotal = _answersLeft.size();
		_earlyAns = new MJ_OC_Factory<OneAnswer>().create();
		_lateAns = new MJ_OC_Factory<OneAnswer>().create();
		
		String sensorId = ConstTags.SENSORID_TO_TYPE.get(Constants.SENSORID_GPS);
		SensorDataOfOneType gpsData = allSensorData.getCouponDataOfType(_cid, sensorId).getDeepCopy();
		if (gpsData != null) {
			_gpsAd = new GpsDataAdapter(gpsData, _sensorFireTimeInterval, _minTReq);
		}
		else {
			_gpsAd = null;
		}
	}
	
	public OneReport getPerformanceEvalData(OneReport map) {
		doTheWork();
		map.addValue(ConstTags.REPORTS_COUPONID, _cid * 1.0);
		map.addValue(ConstTags.REPORTS_RULEID, _rid * 1.0);
		map = getGoodFireCounts(map);
		map = getLateAndMissedFireCounts(map);
		map = getEarlyFireCounts(map);
		// allowed margin of error for firing late or early
		map.addValue(ConstTags.REPORTS_PERC_ALLW_DEV_FRM_RULE_FIRE_T,
				Constants.PERCENT_ALLOWED_DEVIATION_FROM_REQ_RULE_FIRE_TIME * 100, ConstTags.REPORTS_P_A_D_F_R_F_T_TEXT);
		// 2*SI and minTReq
		map.addValue(ConstTags.REPORTS_SENSOR_INTERVAL, _sensorFireTimeInterval);
		map.addValue(ConstTags.REPORTS_RULE_MIN_T, _minTReq, ConstTags.REPORTS_R_M_T_TEXT);
		return map;
	}
	
	protected abstract void doTheWork();
	
	protected abstract double getVeryFirstShouldFireTime();
	
	protected final void shouldFireRule(double tNowAndIdealFireT, double tForCalcAllowedDevT) {
		boolean shouldFire = filtersPassed(tNowAndIdealFireT);
		if ( ! shouldFire ) {
			_ruleTrueFilterFalseCount++;
		}
		else {
			_idealWorldNumRuleFires++;  
			if (_answersLeft.size() > 0) {
				findGoodAnswer(tNowAndIdealFireT, tForCalcAllowedDevT);
			}
		}
		// dummy true fire time when the filter condition was not met and/or there are no answers left
		// to be able to continue looping through all the times it should fire
		if ( ! shouldFire || _answersLeft.size() <= 0){
			_trueFireT = tNowAndIdealFireT;
		}
		
		Assertion.test(_trueFireT >= tNowAndIdealFireT, "moving BACK from " + tNowAndIdealFireT + 
				" to " + _trueFireT + " by " + (tNowAndIdealFireT - _trueFireT));
	}
	
	protected final boolean filtersPassed(double time) {
		boolean passed = true;
		IMJ_OC<AbsFilterInput> inputs = getFilterInputs(time);
		if (_filters != null) {
			for (Filter f: _filters) {
				if (! f.checkFilterInputs(inputs) ) {
					passed = false;
					break;
				}
			}
		}
		return passed;
	}

	private void findGoodAnswer(double tNowAndIdealFireT, double tForCalcAllowedDevT) {
		OneAnswer ans;
		// arbitrary, picking SI because it seems reasonable 
		double allowedDevT = Constants.PERCENT_ALLOWED_DEVIATION_FROM_REQ_RULE_FIRE_TIME * tForCalcAllowedDevT;
		int lenBefore = _answersLeft.size();

		// loop through answers
		for (int i = 0; i < _answersLeft.size(); i++) {
			ans = _answersLeft.get(i);

			_trueFireT = ans.getRuleFiredTime().getTimeInMillis() / 1000.0;

			// if this answer's fire time is later than or equal to the ideal fire time
			if (_trueFireT >= tNowAndIdealFireT) {

				// if this answer's fire time is also earlier than the allowed maximum time,
				double t = tNowAndIdealFireT + allowedDevT;
				if (_trueFireT <= t) {
					// then it is a good answer (fired roughly when it should have)
					_goodAnsCount++;
				}
				// if this answer's fire time is also later than the allowed maximum time,
				// it is a late answer; but count this as the next fire time
				else {
					double tAmountLate = _trueFireT - tNowAndIdealFireT;
					// if the answer is late by less than 80% of the given time interval, it is a late answer
					if (tAmountLate < .8 * tForCalcAllowedDevT) {
						_lateAnsCount++;
					// if the answer is late by more than 80% of the given time interval, it is a missed answer
					} else {
						_likelyMissedAnsCount++;
						break;
					}
					_lateAns.add(ans);
				}
				// remove this answer from the list of answers to avoid counting it again
				_answersLeft.remove(i);
				Assertion.test(lenBefore == _answersLeft.size() + 1, "delete did not work");
				break;
			}
			
			// if this answer's fire time is too early (this also counts "early" answers due a time filter)
			else {
				if (!_earlyAns.contains(ans)) {
					// keep track of answers/rule fires that were too early
					_earlyAns.add(ans);
					// if this is too early now, it will only be earlier still for the next rule fire time,
					// so remove this answer from the list of answers to avoid counting it again
					_answersLeft.remove(i);
					Assertion.test(lenBefore == _answersLeft.size() + 1, "delete did not work");
					i--;
					lenBefore--;
				}
			}
		}
	}
	
	private IMJ_OC<AbsFilterInput> getFilterInputs(double tNow) {
		IMJ_OC<AbsFilterInput> inputs = new MJ_OC_Factory<AbsFilterInput>().create();
		inputs.add(new LocFilterInput(tNow, _gpsAd));
		inputs.add(new TimeFilterInput(tNow));
		inputs.add(new QuestionFilterInput(_allAnswers, tNow));
		return inputs;
	}
	
	private OneReport getGoodFireCounts(OneReport map) {
		// counts of good, total, and ideal rule fires
		map.addValue(ConstTags.REPORTS_GOOD_RULE_FIRES, _goodAnsCount * 1.0, ConstTags.REPORTS_G_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_TOTAL_RULE_FIRES, _numRuleFiresTotal * 1.0, ConstTags.REPORTS_T_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_IDEAL_RULE_FIRES, _idealWorldNumRuleFires * 1.0, ConstTags.REPORTS_I_R_F_TEXT);

		// percent good of ideal fires
		double ans1 = 0.0;
		if (_idealWorldNumRuleFires > 0) {
			ans1 = Math.round(((double) _goodAnsCount / _idealWorldNumRuleFires) * 100.0);
		}
		map.addValue(ConstTags.REPORTS_GOOD_FIRE_PERCENT, ans1, ConstTags.REPORTS_G_F_P_TEXT);

		double ans2;
		// percent good of all fires
		if (_numRuleFiresTotal > 0) {
			ans2 = Math.round((double) _goodAnsCount / (double) _numRuleFiresTotal * 100.0);
		} else {
			ans2 = 0.0;
		}
		map.addValue(ConstTags.REPORTS_GOOD_FIRE_PERCENT_OF_TOTAL, ans2, ConstTags.REPORTS_G_F_P_O_T_TEXT);
		
		map.addValue(ConstTags.REPORTS_RULE_TRUE_FILTER_FALSE, _ruleTrueFilterFalseCount*1.0, ConstTags.REPORTS_R_T_F_F);

		return map;
	}

	private OneReport getLateAndMissedFireCounts(OneReport map) {
		// cutoff for deciding if late or missed
		map.addValue(ConstTags.REPORTS_PERC_CUTOFF_MINT_LATE_ANS, Constants.PERCENT_CUTOFF_OF_MINT_FOR_LATE_ANS * 100.0,
				ConstTags.REPORTS_P_C_M_L_A_TEXT);
		// counts of late and missed fires
		map.addValue(ConstTags.REPORTS_LATE_RULE_FIRES, _lateAnsCount * 1.0, ConstTags.REPORTS_L_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_MISSED_RULE_FIRES, _likelyMissedAnsCount * 1.0, ConstTags.REPORTS_M_R_F_TEXT);
		// all late or missed answer times
		if (_lateAnsCount > 0) {
			for (int i = 0; i < _lateAns.size(); i++) {
				map.addValue(ConstTags.REPORTS_LATE_ANS(i),
						_lateAns.get(i).getRuleFiredTime().getTimeInMillis() * 1.0, ConstTags.REPORTS_L_A_TEXT(i));
			}
		}
		return map;
	}

	private OneReport getEarlyFireCounts(OneReport map) {
		// count of early fires
		map.addValue(ConstTags.REPORTS_EARLY_RULE_FIRES, _earlyAns.size() * 1.0, ConstTags.REPORTS_E_R_F_TEXT);
		// all early answer times
		if (_earlyAns.size() > 0) {
			for (int i = 0; i < _earlyAns.size(); i++) {
				map.addValue(ConstTags.REPORTS_EARLY_ANS(i),
						_earlyAns.get(i).getRuleFiredTime().getTimeInMillis() * 1.0,
						ConstTags.REPORTS_E_A_TEXT(i));
			}
		}
		// count of not late, missed, early, or on time fires
		map.addValue(ConstTags.REPORTS_OTHER_RULE_FIRES, _answersLeft.size() * 1.0, ConstTags.REPORTS_O_R_F_TEXT);

		return map;
	}
}
