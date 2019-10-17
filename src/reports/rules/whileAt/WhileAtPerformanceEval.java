package reports.rules.whileAt;

import Assert.Assertion;
import constants.ConstTags;
import constants.Constants;
import dao.OneAnswer;
import dao.rules.OneRule;
import dao.rules.WhileAtRuleParams;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.OneReport;
import reports.rules.AnswersCollection;
import reports.rules.Predicate;
import reports.rules.PredicateInLocRadius;
import reports.rules.RulesCollection;
import sensors.data.DataCollection;
import sensors.data.GpsDataPoint;
import sensors.data.OneCouponsData;
import sensors.gps.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public class WhileAtPerformanceEval {
	private final int _cid;
	private final int _rid;
	private final int _sensorId = 12;
	private final int _numRuleFiresTotal;
	private int _goodAnsCount = 0;
	private int _lateAnsCount = 0;
	private int _likelyMissedAnsCount = 0;
	private int _idealWorldNumRuleFires = 0;
	private int _shouldFireCount = 0;

	private final double _sensorFireTimeInterval;
	private final double _minTReq;
	private double _curMinTBetweenFires = 0.0;
	private double _totalTimeAtLoc = 0.0;
	private double _prevIdealFireT = 0.0;
	private double _trueFireT = 0.0;

	private AnswersCollection _allWhileAtAnswers;

	private final OneRule _rule;
	private final OneCouponsData _gpsData;
	private final AnswersCollection _answersLeft;
	private final IMJ_OC<OneAnswer> _lateAns;
	private final IMJ_OC<OneAnswer> _earlyAns;
	private final Predicate _pred;
	private final GpsDataAdapter _ad;

    // _answers contain all answers, regardless of cid and rid
	public WhileAtPerformanceEval(AnswersCollection answers, RulesCollection rules, DataCollection allGpsSensorData,
			double sensorFireTimeInterval, int cid, int rid) {
		_cid = cid;
		_rid = rid;
		_rule = rules.getRuleById(_rid);
		_sensorFireTimeInterval = sensorFireTimeInterval;
		
		// will contain all whileAt answers for this Cid (regardless of rid)
		_allWhileAtAnswers = answers.getOneRuleTypesAnswersForCid(_cid, rules, Constants.RULE_WHILEAT_NOTAT);

		Assertion.test(allGpsSensorData.length() > 0, "No GPS data found");
		_gpsData = allGpsSensorData.getCouponDataOfType(_cid, ConstTags.SENSORID_TO_TYPE.get(_sensorId)).getDeepCopy();
		Assertion.test(_gpsData.length() > 0, "No GPS data found for coupon ID " + _cid);
		
		// minimum time that must pass between rule fires
    	WhileAtRuleParams param = (WhileAtRuleParams) _rule.getParams();
		_minTReq = param.getMinTimeSinceLastFire() * 1.0;

		
		AllWhileAtRuleData allWhileAtRuleData = new AllWhileAtRuleData(answers, rules);
		
		
		OneCouponsWhileAtAnswers oneCidWhileAtAns = new OneCouponsWhileAtAnswers(allWhileAtRuleData, cid);

		if (_allWhileAtAnswers != null) {
			AnswersCollection oneRuleAnsers = oneCidWhileAtAns.getOneRulesAnswersCollection(_rid);
			_answersLeft = oneRuleAnsers.getDeepCopy();
		} else {
			_answersLeft = new AnswersCollection(new MJ_OC_Factory<OneAnswer>().create());
		}
		_numRuleFiresTotal = _answersLeft.size();
		_earlyAns = new MJ_OC_Factory<OneAnswer>().create();
		_lateAns = new MJ_OC_Factory<OneAnswer>().create();

		_pred = new PredicateInLocRadius(_rule);
		_ad = new GpsDataAdapter(_gpsData, _sensorFireTimeInterval, _minTReq);
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
		map.addValue(ConstTags.REPORTS_RULE_MIN_T, getMinTReq(), ConstTags.REPORTS_R_M_T_TEXT);
		return map;
	}

	private void doTheWork() {
		GpsDataPoint p;
		GpsCoordinate c;
		boolean atLoc = false;
		int atLocConsecCount = 0;
		double maxT = getMaxTimeToCheck();
		double veryFirstFireT = getVeryFirstShouldFireTime();
		
		// loop through time by minT intervals, skipping absences from the rule location
		for (double fireT = veryFirstFireT; fireT <= maxT; fireT += _curMinTBetweenFires) {
			p = _ad.getLocationAndClearPreceeding(fireT);
			// GpsDataPoint p = _ad.getLocation(t);
			c = p.getGpsCoord();

			// if we are roughly at the required location
			if (_pred.test(c)) {
				atLoc = true;
				atLocConsecCount++;
				locWithinRadius(fireT, atLocConsecCount);
				// set fireT to when it actually fired
				
				Assertion.test(_trueFireT >= fireT, "moving back!!! from " 
						+ fireT + " to " + _trueFireT + " by " + (fireT - _trueFireT));

				fireT = _trueFireT;

			} else {
				atLoc = false;
				atLocConsecCount = 0;
				locNotWithinRadius();
				// set fireT to the time of the next gps recording at the location
				fireT = _ad.getNextStartTime(fireT, _pred);
				if (fireT <= 0.0) { // if there are no more recordings at the location
					break;
				}
			}
		}
		// if the last minT interval time was at the location, round off the
		// calculations of time spent there
		if (atLoc) {
			locNotWithinRadius();
		}
		Assertion.test(
				_earlyAns.size() + _answersLeft.size() + _goodAnsCount + _lateAns.size() == _numRuleFiresTotal,
				"not all answers are accounted for");
	}

	private void locNotWithinRadius() {
		// presence at the location is counted until just before the first GPS recording
		// not at the location
		// the below calculation not quite accurate, as we do not know the exact time we
		// switched location,
		// just that the next time we check based on the minT intervals was not at the
		// location.
		// to get a rough estimate, taking half of the minT interval during which we
		// switched locations
		_totalTimeAtLoc += _curMinTBetweenFires / 2.0;
		_curMinTBetweenFires = 2 * _sensorFireTimeInterval;
		// if the time spent at this location is more than 2 * SI
		// (i.e. there should be at least one rule fire at this location),
		if (_totalTimeAtLoc >= _sensorFireTimeInterval * 2) {
			// subtract 2*SI from total time because the first minT between rule fires is
			// 2*SI
			_totalTimeAtLoc -= _sensorFireTimeInterval * 2;
			// and add that first rule fire to the counter,
			_idealWorldNumRuleFires++;
			// then get the remaining count of times it should have fired, if any
			_idealWorldNumRuleFires += Math.floor(_totalTimeAtLoc / _minTReq);
		}
		_totalTimeAtLoc = 0.0;
	}

	private void locWithinRadius(double tNowAndIdealFireT, int count) {
		_shouldFireCount++;
		_curMinTBetweenFires = _minTReq;

		if (_answersLeft.size() > 0) {
			findGoodAnswer(tNowAndIdealFireT);
		} else {
			// dummy true fire time when there are no answers left
			// to be able to continue looping through all the times it should fire
			_trueFireT = tNowAndIdealFireT;
		}
		// if this is the first time the rule should fire,
		// add 2*SI to the total time as there is no previous fire time yet
		// if this fire was late, also add the amount it was late by
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
			ans = _answersLeft.getAnsAtIdx(i);

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
				_answersLeft.removeAnsAtIdx(i);
				Assertion.test(lenBefore == _answersLeft.size() + 1, "delete did not work");
				break;
			}
			// if this answer's fire time is too early,
			else {
				if (!_earlyAns.contains(ans)) {
					// keep track of answers/rule fires that were too early
					_earlyAns.add(ans);
					// if this is too early now, it will only be earlier still for the next rule
					// fire time
					// remove this answer from the list of answers to avoid counting it again
					_answersLeft.removeAnsAtIdx(i);
					Assertion.test(lenBefore == _answersLeft.size() + 1, "delete did not work");
					// System.out.println("new early answer");
					i--;
					lenBefore--;
				}
			}
		}
	}

	private double getMaxTimeToCheck() {
		double gpsMaxT = _ad.getLastRecordingTime();
		double ansMaxT = 0.0;
		if (_answersLeft.size() > 0) {
			ansMaxT = _answersLeft.getAnsAtIdx(_answersLeft.size() - 1).getRuleFiredTime().getTimeInMillis() / 1000.0;
		}
		return Math.max(gpsMaxT, ansMaxT);
	}

	private double getVeryFirstShouldFireTime() {
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

		return map;
	}

	private OneReport getLateAndMissedFireCounts(OneReport map) {
		// counts of late and missed fires
		map.addValue(ConstTags.REPORTS_LATE_RULE_FIRES, _lateAnsCount * 1.0, ConstTags.REPORTS_L_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_MISSED_RULE_FIRES, _likelyMissedAnsCount * 1.0, ConstTags.REPORTS_M_R_F_TEXT);
		// cutoff for deciding if late or missed
		map.addValue(ConstTags.REPORTS_PERC_CUTOFF_MINT_LATE_ANS, Constants.PERCENT_CUTOFF_OF_MINT_FOR_LATE_ANS * 100.0,
				ConstTags.REPORTS_P_C_M_L_A_TEXT);
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
