package reports.rules.OnArrival;

import Assert.Assertion;
import constants.*;
import dao.OneAnswer;
import filters.*;
import orderedcollection.*;
import reports.OneReport;
import reports.rules.*;
import sensors.data.GpsDataPoint;
import sensors.data.SensorDataCollection;
import sensors.data.SensorDataOfOneType;
import sensors.gps.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public class OnArrivalPerformanceEval {
	private final int _cid;
	private final int _rid;
	private final int _sensorId = Constants.SENSORID_GPS;
	private final int _numRuleFiresTotal;
	private int _goodAnsCount = 0;
	private int _lateAnsCount = 0;
	private int _likelyMissedAnsCount = 0;
	private int _shouldFireCount = 0;
	private int _ruleTrueFilterFalseCount = 0;

	private final double _sensorFireTimeInterval;
	private final double _2si;
	private double _trueFireT = 0.0;

	private final AnswersCollection _allAnswers;
	private final AnswersCollection _answersLeft;
	private final IMJ_OC<OneAnswer> _lateAns;
	private final IMJ_OC<OneAnswer> _earlyAns;
	private final Predicate _pred;
	private final GpsDataAdapter _ad;
	
	private final IMJ_OC<Filter> _filters;

    // _answers contain all answers, regardless of cid and rid
	public OnArrivalPerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double sensorFireTimeInterval, int cid, int rid) {
		_cid = cid;
		_rid = rid;
		_sensorFireTimeInterval = sensorFireTimeInterval;
		_2si = 2 * _sensorFireTimeInterval;
		_allAnswers = answers;
		
		OneRule rule = rules.getRuleById(_rid);
		// minimum time that must pass between rule fires
		OnArrivalOrDepRuleParams param = (OnArrivalOrDepRuleParams) rule.getParams();
		_pred = new PredicateInLocRadius(new GpsCoordinate(param.getLat(), param.getLon()), param.getDist());
		_filters = rule.getFilters();

		_answersLeft = answers.getAnsForRuleAndCid(_cid, _rid);  // a new AnswersCollection is already a deep copy
			
		_numRuleFiresTotal = _answersLeft.size();
		_earlyAns = new MJ_OC_Factory<OneAnswer>().create();
		_lateAns = new MJ_OC_Factory<OneAnswer>().create();
		
		SensorDataOfOneType gpsData = allSensorData.getCouponDataOfType(_cid, ConstTags.SENSORID_TO_TYPE.get(_sensorId)).getDeepCopy();
		Assertion.test(gpsData != null, "No GPS data found for coupon ID " + _cid);
		_ad = new GpsDataAdapter(gpsData, _sensorFireTimeInterval, _2si);
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
		map.addValue(ConstTags.REPORTS_RULE_MIN_T, _2si, ConstTags.REPORTS_R_M_T_TEXT);
		return map;
	}

	private void doTheWork() {
		double maxT = getMaxTimeToCheck();
		// step through time by each next time having been at the rule location for at least 2si
		// after not having been at rule location for at least 2si
		for (double fireT = getVeryFirstShouldFireTime(maxT); fireT < maxT; fireT = getNextShouldFireTime(fireT, maxT)) {
			locWithinRadius(fireT);
			// set fireT to when it actually fired
			Assertion.test(_trueFireT >= fireT, "moving BACK from " + fireT + " to " + _trueFireT + " by " + (fireT - _trueFireT));
			fireT = _trueFireT;
		}
		
		Assertion.test(
				_earlyAns.size() + _answersLeft.size() + _goodAnsCount + _lateAns.size() == _numRuleFiresTotal,
				"not all answers are accounted for");
	}

	private void locWithinRadius(double tNowAndIdealFireT) {
		boolean shouldFire = true;
		if (_filters != null) {
			IMJ_OC<AbsFilterInput> conds = getFilterConds(tNowAndIdealFireT);
			for (Filter f: _filters) {
				if (! f.checkFilterCondition(conds) ) {
					_ruleTrueFilterFalseCount++;
					shouldFire = false;
					break;
				}
			}
		}
		
		if ( shouldFire ) {
			_shouldFireCount++;  
			if (_answersLeft.size() > 0) {
				findGoodAnswer(tNowAndIdealFireT);
			}
		}
		// dummy true fire time when the filter condition was not met and/or there are no answers left
		// to be able to continue looping through all the times it should fire
		if ( ! shouldFire || _answersLeft.size() <= 0){
			_trueFireT = tNowAndIdealFireT;
		}
	}

	private void findGoodAnswer(double tNowAndIdealFireT) {
		OneAnswer ans;
		double allowedDevT = Constants.PERCENT_ALLOWED_DEVIATION_FROM_REQ_RULE_FIRE_TIME * _2si;
		int lenBefore = _answersLeft.size();

		// loop through answers
		for (int i = 0; i < _answersLeft.size(); i++) {
			ans = _answersLeft.getAnsAtIdx(i);

			_trueFireT = ans.getRuleFiredTime().getTimeInMillis() / 1000.0;

			// if this answer's fire time is later than or equal to the ideal fire time
			if (_trueFireT >= tNowAndIdealFireT) {

				// if this answer's fire time is also earlier than the allowed maximum time,
				if (_trueFireT <= tNowAndIdealFireT + allowedDevT) {
					// then it is a good answer (fired roughly when and where it should have)
					_goodAnsCount++;
				}
				// if this answer's fire time is also later than the allowed maximum time,
				// it is a late answer; but count this as the next fire time
				else {
					double tAmountLate = _trueFireT - tNowAndIdealFireT;
					// if the answer is late by more than 80% of the 2si, it is a missed answer
					if (tAmountLate > .8 * _2si) {
						_likelyMissedAnsCount++;
					} else {
						_lateAnsCount++;
					}
					_lateAns.add(ans);
				}
				// remove this answer from the list of answers to avoid counting it again
				_answersLeft.removeAnsAtIdx(i);
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
					_answersLeft.removeAnsAtIdx(i);
					Assertion.test(lenBefore == _answersLeft.size() + 1, "delete did not work");
					i--;
					lenBefore--;
				}
			}
		}
	}
	
	private IMJ_OC<AbsFilterInput> getFilterConds(double tNow) {
		IMJ_OC<AbsFilterInput> conds = new MJ_OC_Factory<AbsFilterInput>().create();
		conds.add(new LocFilterInput(tNow, _ad));
		conds.add(new TimeFilterInput(tNow));
		conds.add(new QuestionFilterInput(_allAnswers, tNow));
		return conds;
	}

	private double getMaxTimeToCheck() {
		// presence at the last locations can be assumed until just before another sensor recording should happen
		double gpsMaxT = _ad.getLastRecordingTime() + _sensorFireTimeInterval - 1;  
		double ansMaxT = 0.0;
		if (_answersLeft.size() > 0) {
			ansMaxT = _answersLeft.getAnsAtIdx(_answersLeft.size() - 1).getRuleFiredTime().getTimeInMillis() / 1000.0;
		}
		return Math.max(gpsMaxT, ansMaxT);
	}

	private double getVeryFirstShouldFireTime(double maxTToCheck) {
		// get next should fire time after first recording time and after not being at rule location for at least 2si
		//return getNextShouldFireTime(_ad.getFirstRecordingTime(), maxTToCheck);
		return getNextShouldFireTime(0.0, maxTToCheck);
	}
	
	private double getNextShouldFireTime(double tNow, double maxTToCheck) {
		double t = tNow;
		while (t <= maxTToCheck) {
			// get the time for the next first recording at the rule location
			double firstT = _ad.getNextStartTime(t, _pred);
			if (firstT <= 0.0) {
				return maxTToCheck;
			}
			// and add 2*SI, one by one, to get the potential should fire time
			// and get each location for those next two time
			t = firstT + _sensorFireTimeInterval;
			GpsCoordinate c1 = _ad.getLocation(t).getGpsCoord();
			t += _sensorFireTimeInterval;
			GpsCoordinate c2 = _ad.getLocation(t).getGpsCoord();
			
			// check if this second location is also at the rule location
			if (_pred.test(c1) && _pred.test(c2)) {
				// get the previous two fire time, 1si and 2si before the first time at rule location
				double prevT = firstT - _sensorFireTimeInterval;
				GpsDataPoint dp = _ad.getLocation(prevT);
				if (dp == null) {
					c1 = null;
				} else {
					c1 = dp.getGpsCoord();
				}
				prevT -= _sensorFireTimeInterval;
				dp = _ad.getLocation(prevT);
				if (dp == null) {
					c2 = null;
				} else {
					c2 = dp.getGpsCoord();
				}
				// get those locations
				// check if those location are not at the rule location
				if ( ! _pred.test(c1) && ! _pred.test(c2) ) {
					return t;
				}
			}
		}
		
		return maxTToCheck;
	}

	private OneReport getGoodFireCounts(OneReport map) {
		// counts of good, total, and ideal rule fires
		map.addValue(ConstTags.REPORTS_GOOD_RULE_FIRES, _goodAnsCount * 1.0, ConstTags.REPORTS_G_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_TOTAL_RULE_FIRES, _numRuleFiresTotal * 1.0, ConstTags.REPORTS_T_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_IDEAL_RULE_FIRES, _shouldFireCount * 1.0, ConstTags.REPORTS_I_R_F_TEXT);

		// percent good of ideal fires
		double ans1 = 0.0;
		if (_shouldFireCount > 0) {
			ans1 = Math.round(((double) _goodAnsCount / _shouldFireCount) * 100.0);
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
				map.addValue(ConstTags.REPORTS_LATEORMISSED_ANS(i),
						_lateAns.get(i).getRuleFiredTime().getTimeInMillis() * 1.0, ConstTags.REPORTS_LOM_A_TEXT(i));
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
