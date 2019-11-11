package reports.rules.whileAt;

import Assert.Assertion;
import constants.ConstTags;
import constants.Constants;
import dao.OneAnswer;
import filters.*;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.OneReport;
import reports.rules.AnswersCollection;
import reports.rules.GpsDataAdapter;
import reports.rules.OneRule;
import reports.rules.Predicate;
import reports.rules.PredicateInLocRadius;
import reports.rules.RulesCollection;
import sensors.data.SensorDataCollection;
import sensors.data.GpsDataPoint;
import sensors.data.SensorDataOfOneType;
import sensors.gps.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public class WhileAtPerformanceEval {
	private final int _cid;
	private final int _rid;
	private final int _sensorId = Constants.SENSORID_GPS;
	private final int _numRuleFiresTotal;
	private int _goodAnsCount = 0;
	private int _lateAnsCount = 0;
	private int _likelyMissedAnsCount = 0;
	private int _idealWorldNumRuleFires = 0;
	private int _shouldFireCount = 0;
	private int _ruleTrueFilterFalseCount = 0;

	private final double _sensorFireTimeInterval;
	private final double _minTReq;
	private double _curMinTBetweenFires = 0.0;
	private double _totalTimeAtLoc = 0.0;
	private double _prevIdealFireT = 0.0;
	private double _trueFireT = 0.0;

	private final AnswersCollection _allAnswers;
	private final AnswersCollection _answersLeft;
	private final IMJ_OC<OneAnswer> _lateAns;
	private final IMJ_OC<OneAnswer> _earlyAns;
	private final Predicate _pred;
	private final GpsDataAdapter _ad;
	
	private final IMJ_OC<Filter> _filters;

    // _answers contain all answers, regardless of cid and rid
	public WhileAtPerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double sensorFireTimeInterval, int cid, int rid) {
		_cid = cid;
		_rid = rid;
		_sensorFireTimeInterval = sensorFireTimeInterval;
		_allAnswers = answers;
		
		OneRule rule = rules.getRuleById(_rid);
		// minimum time that must pass between rule fires
    	WhileAtRuleParams param = (WhileAtRuleParams) rule.getParams();
		_minTReq = param.getMinTimeSinceLastFire() * 1.0;
		_pred = new PredicateInLocRadius(new GpsCoordinate(param.getLat(), param.getLon()), param.getDist());
		_filters = rule.getFilters();

		_answersLeft = answers.getAnsForRuleAndCid(_cid, _rid);  // a new AnswersCollection is already a deep copy
			
		_numRuleFiresTotal = _answersLeft.size();
		_earlyAns = new MJ_OC_Factory<OneAnswer>().create();
		_lateAns = new MJ_OC_Factory<OneAnswer>().create();
		
		SensorDataOfOneType gpsData = allSensorData.getCouponDataOfType(_cid, ConstTags.SENSORID_TO_TYPE.get(_sensorId)).getDeepCopy();
		Assertion.test(gpsData != null, "No GPS data found for coupon ID " + _cid);
		_ad = new GpsDataAdapter(gpsData, _sensorFireTimeInterval, _minTReq);
	}

	public OneReport getWhileAtPerformanceEvalData(OneReport map) {
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

	private void doTheWork() {
		GpsDataPoint p;
		GpsCoordinate c;
		boolean atLoc = false;
		int atLocConsecCount = 0;
		double maxT = getMaxTimeToCheck();
		double veryFirstFireT = getVeryFirstShouldFireTime();
		double firstFireT = veryFirstFireT;
		
		// loop through time by minT intervals, skipping absences from the rule location
		for (double fireT = veryFirstFireT; fireT <= maxT; fireT += _curMinTBetweenFires) {
			p = _ad.getLocationAndClearPreceeding(fireT);
			// GpsDataPoint p = _ad.getLocation(t);
			c = p.getGpsCoord();

			// if we are roughly at the required location
			if (_pred.test(c)) {
				atLocConsecCount++;
				locWithinRadius(fireT, atLocConsecCount);
				// set fireT to when it actually fired
				Assertion.test(_trueFireT >= fireT, "moving BACK from " + fireT + " to " + _trueFireT + " by " + (fireT - _trueFireT));
				fireT = _trueFireT;
				if (atLoc == false) {  // if this is the first occurrence of being at this loc (again)
					firstFireT = fireT;  // set the first fire time at this loc to the ideal fire time
				}
				atLoc = true;
			} 
			else {
				atLoc = false;
				atLocConsecCount = 0;
				// use fireT as ideal fire time to get count of ideal world fires, note that
				// this may still not be entirely accurate as each fireT is based on the previous trueFireT and is not an absolute measure
				locNotWithinRadius(firstFireT);  
				// set fireT to the time of the next gps recording at the location
				fireT = _ad.getNextStartTime(fireT, _pred);
				if (fireT <= 0.0) { // if there are no more recordings at the location
					break;
				}
			}
		}
		
		// if the last recording was at the location, round off the calculations of total time spent there
		if (atLoc) {
			locNotWithinRadius(firstFireT);
		}
		Assertion.test(
				_earlyAns.size() + _answersLeft.size() + _goodAnsCount + _lateAns.size() == _numRuleFiresTotal,
				"not all answers are accounted for");
	}

	private void locNotWithinRadius(double firstFireT) {
		// presence at the location is counted until just before the first GPS recording not at the location
		// the below calculation is not quite accurate, as we do not know the exact time we switched location,
		// just that the next time we check based on the minT intervals was not at the location.
		// to get a rough estimate, taking half of the minT interval during which we switched locations
		_totalTimeAtLoc += _curMinTBetweenFires / 2.0;
		// if the time spent at this location is more than 2 * SI
		// (i.e. there should be at least one rule fire at this location, not taking filters into account),
		if (_totalTimeAtLoc >= _sensorFireTimeInterval * 2) {
			// subtract 2*SI from total time because the first minT between rule fires is 2*SI
			_totalTimeAtLoc -= _sensorFireTimeInterval * 2;
			// then get the count of times it should have fired
			// by looping from the first fire time to the last fire time, by minTReq
			for (double i = firstFireT; i <= (firstFireT+_totalTimeAtLoc); i+=_minTReq) {
				boolean add = true;
				IMJ_OC<AbsFilterInput> conds = getFilterConds(i);
				if (_filters != null) {
					for (Filter f: _filters) {
						if (! f.checkFilterCondition(conds) ) {
							add = false;
							break;
						}
					}
				}
				if (add) {
					_idealWorldNumRuleFires++;
				}
			}
			// then get the remaining count of times it should have fired, if any
			//_idealWorldNumRuleFires += Math.floor(_totalTimeAtLoc / _minTReq);
		}
		_curMinTBetweenFires = 2 * _sensorFireTimeInterval;
		_totalTimeAtLoc = 0.0;
	}
	
	private IMJ_OC<AbsFilterInput> getFilterConds(double tNow) {
		IMJ_OC<AbsFilterInput> conds = new MJ_OC_Factory<AbsFilterInput>().create();
		conds.add(new LocFilterInput(tNow, _ad));
		conds.add(new TimeFilterInput(tNow));
		conds.add(new QuestionFilterInput(_allAnswers, tNow));
		return conds;
	}

	private void locWithinRadius(double tNowAndIdealFireT, int count) {
		_curMinTBetweenFires = _minTReq; 
		
		boolean shouldFire = true;
		IMJ_OC<AbsFilterInput> conds = getFilterConds(tNowAndIdealFireT);
		if (_filters != null) {
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
		
		// do this regardless of filter conditions:
		
		// if this is the first time the rule should fire as per the location predicate,
		// add 2*SI to the total time spent there as no previous fire time exists yet
		// and in case this fire was late, also add the amount it was late by (+ _trueFireT - tNowAndIdealFireT)
		if (count == 1) {
			_totalTimeAtLoc += 2 * _sensorFireTimeInterval + _trueFireT - tNowAndIdealFireT;
		} else {
			_totalTimeAtLoc += _trueFireT - _prevIdealFireT;
		}
		_prevIdealFireT = _trueFireT;
	}

	private void findGoodAnswer(double tNowAndIdealFireT) {
		OneAnswer ans;
		double allowedDevT = Constants.PERCENT_ALLOWED_DEVIATION_FROM_REQ_RULE_FIRE_TIME * _curMinTBetweenFires;
		int lenBefore = _answersLeft.size();

		// loop through answers
		for (int i = 0; i < _answersLeft.size(); i++) {
			ans = _answersLeft.get(i);

			_trueFireT = ans.getRuleFiredTime().getTimeInMillis() / 1000.0;

			// if this answer's fire time is later than the required minimum time
			if (_trueFireT >= tNowAndIdealFireT) {

				// if this answer's fire time is also earlier than the required maximum time,
				if (_trueFireT <= tNowAndIdealFireT + allowedDevT) {
					// then it is a good answer (fired roughly when and where it should have)
					// System.out.println("this is a good answer");
					_goodAnsCount++;
				}
				// if this answer's fire time is also later than the required maximum time,
				// it is a late answer; but count this as the next fire time
				else {
					double tLate = _trueFireT - tNowAndIdealFireT;
					if (tLate > .8 * _curMinTBetweenFires) {
						_likelyMissedAnsCount++;
					} else {
						_lateAnsCount++;
					}
					_lateAns.add(ans);
					// System.out.println("here is a late answer");
				}
				// remove this answer from the list of answers to avoid counting it again
				_answersLeft.remove(i);
				Assertion.test(lenBefore == _answersLeft.size() + 1, "delete did not work");
				break;
			}
			// if this answer's fire time is too early (this also counts "early" answers due a time filter
			else {
				if (!_earlyAns.contains(ans)) {
					// keep track of answers/rule fires that were too early
					_earlyAns.add(ans);
					// if this is too early now, it will only be earlier still for the next rule
					// fire time
					// remove this answer from the list of answers to avoid counting it again
					_answersLeft.remove(i);
					Assertion.test(lenBefore == _answersLeft.size() + 1, "delete did not work");
					// System.out.println("new early answer");
					i--;
					lenBefore--;
				}
			}
		}
	}

	private double getMaxTimeToCheck() {
		// presence at the last locations can be assumed until just before another sensor recording should happen
		double gpsMaxT = _ad.getLastRecordingTime() + _sensorFireTimeInterval - 1;  
		double ansMaxT = 0.0;
		if (_answersLeft.size() > 0) {
			ansMaxT = _answersLeft.get(_answersLeft.size() - 1).getRuleFiredTime().getTimeInMillis() / 1000.0;
		}
		return Math.max(gpsMaxT, ansMaxT);
	}

	private double getVeryFirstShouldFireTime() {
		/*// get the time for the first recording
		double t = _ad.getFirstRecordingTime();
		GpsCoordinate c = null;

		// check if the second recording is also at the rule location
		while (! _pred.test(c)) {
			// get the time for the next first recording at the rule location, and add 2*SI
			t = _ad.getNextStartTime(t, _pred) + _2si;
			// get the location at that time to check if it is also at the rule location
			c = _ad.getLocationAndClearPreceeding(t).getGpsCoord();
		}
		
		return t;*/
		double t = _ad.getFirstRecordingTime();
		GpsDataPoint p = _ad.getLocationAndClearPreceeding(t);
		// GpsDataPoint p = _ad.getLocation(t);
		GpsCoordinate c = p.getGpsCoord();
		// if the first gps recording time is not at the rule location,
		if (!_pred.test(c)) {
			// get the time for the first recording at the rule location
			t = _ad.getNextStartTime(t, _pred);
		}
		// add the initial 2*SI to the first time at the rule location
		// to get the first time the rule should fire
		t += 2 * _sensorFireTimeInterval;
		return t;
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

	private double getMinTReq() {
		return _minTReq;
	}

	/*
	 * private void printRuleFireCounts(){ // report num good fires of
	 * numShouldHaveFired double ans = 0.0; if (_idealWorldNumRuleFires > 0){ ans =
	 * Math.round(((double)_goodAnsCount / _idealWorldNumRuleFires) * 100.0); }
	 * System.out.println("\nRule fired " + _goodAnsCount + " of " +
	 * _idealWorldNumRuleFires + " times it should have fired in an ideal world (" +
	 * ans + "%)\n");
	 * 
	 * if (_numRuleFiresTotal > 0){ ans = Math.round((double)_goodAnsCount /
	 * (double)_numRuleFiresTotal * 100.0); } else { ans = 0.0; }
	 * System.out.println(_goodAnsCount + " of " + _numRuleFiresTotal +
	 * " total rule fires were at the correct time (" + ans + "%)\n");
	 * 
	 * System.out.println(_lateAnsCount + " late rule fire(s), " +
	 * "meaning the late fire was late by less than " +
	 * Constants.PERCENT_CUTOFF_OF_MINT_FOR_LATE_ANS * 100.0 +
	 * "% of the minimum time between fires\n");
	 * 
	 * if (_lateAnsCount > 0){
	 * System.out.println("Late and missed answer times in millis: "); for (int i =
	 * 0; i< _lateAns.length(); i++){
	 * System.out.println(_lateAns.getItem(i).getRuleFiredTime().getTimeInMillis());
	 * } } System.out.println(_likelyMissedAnsCount + " missed rule fire(s), " +
	 * "meaning the rule fired late by more than " +
	 * Constants.PERCENT_CUTOFF_OF_MINT_FOR_LATE_ANS * 100.0 +
	 * "% of the minimum time between fires\n");
	 * 
	 * System.out.println(_earlyAns.length() + " early rule fire(s)\n");
	 * 
	 * if (_earlyAns.length() > 0){
	 * System.out.println("Early answer times in millis: "); for (int i = 0; i<
	 * _earlyAns.length(); i++){
	 * System.out.println(_earlyAns.getItem(i).getRuleFiredTime().getTimeInMillis())
	 * ; } } System.out.println(_answersLeft.length() +
	 * " rule fires were not early, late, missed, or on time\n");
	 * 
	 * //System.out.println("\nBased on each previous rule fire, the rule had " // +
	 * _shouldFireCount +
	 * " opportunities to fire - \n\tthis count includes any and all " // +
	 * "\"missed\" fires based on the previous fire if any");
	 * 
	 * System.out.
	 * println("Allowed time deviation from minimum time between fires = " +
	 * Constants.PERCENT_ALLOWED_DEVIATION_FROM_REQ_RULE_FIRE_TIME * 100 +
	 * "%, \n\twhere 2*SI = " + 2*_sensorFireTimeInterval/60.0 +
	 * " mins and rule required minimum time = " + _minTReq/60.0 + " mins"); }
	 */
}
