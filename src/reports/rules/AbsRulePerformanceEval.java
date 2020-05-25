package reports.rules;

import java.util.Calendar;

import Assert.Assertion;
import constants.*;
import dao.CouponCollection;
import dao.OneAnswer;
import filters.*;
import maps.IMJ_Map;
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
	private final IMJ_Map<Integer, String> _cIdToNames;
	protected final int _rid;
	protected double _startTimeInSecs;
	private double _stopTimeInSecs;
	
	private final int _numRuleFiresTotal;
	private final int _numAnsweredRuleFiresTotal;
	private final int _numSkippedAnswerRuleFiresTotal;
	private final int _numPoweredOffAnswerRuleFires;
	private final int _numExpiredAnswerRuleFires;
	private int _goodAnsCount = 0;
	private int _lateAnsCount = 0;
	private int _earlyAnsCount = 0;
	private int _likelyMissedAnsCount = 0;
	protected int _idealWorldNumRuleFires = 0;
	private int _ruleTrueFilterFalseCount = 0;
	
	protected final double _gpsSensorFireTimeInterval;
	protected final double _minTReqRule;
	protected final double _maxAnsT;
	protected double _trueFireT = 0.0;
	
	protected final AnswersCollection _allAnswers;
	private AnswersCollection _answersLeft;
	private final IMJ_OC<OneAnswer> _lateAns;
	private final IMJ_OC<OneAnswer> _earlyAns;
	
	protected final GpsDataAdapter _gpsAd;
	private final IMJ_OC<Filter> _filters;
	
	private final double _allowedDivergenceNotMissedFireT;
	private final double _allowedDivergenceOnTimeFireT;
	
	protected AbsRulePerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double gpsSensorFireTimeInterval, int cid, int rid, double minTReqRule, double allowanceOnTimeFireT, 
			double allowanceLateFireT, CouponCollection coupons, double stopTimeInSecs, double windowInHrs, 
			IMJ_Map<Integer, String> cIdToNames, double startTimeInSecs) {
		_cIdToNames = cIdToNames;
		_cid = cid;
		_rid = rid;
		
		setTimesAndAnsLeft(stopTimeInSecs, windowInHrs, startTimeInSecs, coupons, cid, answers);

		_maxAnsT = Math.min(coupons.getCouponById(_cid).getStudyEndTime().getTimeInMillis() / 1000.0, stopTimeInSecs);
		_gpsSensorFireTimeInterval = gpsSensorFireTimeInterval;
		_minTReqRule = minTReqRule;
		_allowedDivergenceOnTimeFireT = allowanceOnTimeFireT;
		_allowedDivergenceNotMissedFireT = allowanceLateFireT; 
		
		_filters = rules.getRuleById(_rid).getFilters();

		_allAnswers = answers;
		_numRuleFiresTotal = _answersLeft.size();
		_numAnsweredRuleFiresTotal = _answersLeft.getAnsForResponded().size();
		_numSkippedAnswerRuleFiresTotal = _answersLeft.getAnsForSkipped().size();
		_numPoweredOffAnswerRuleFires = _answersLeft.getAnsForPoweredOff().size();
		_numExpiredAnswerRuleFires = _answersLeft.getAnsForExpired().size();
		
		_earlyAns = new MJ_OC_Factory<OneAnswer>().create();
		_lateAns = new MJ_OC_Factory<OneAnswer>().create();
		
		String sensorId = ConstTags.SENSORID_TO_TYPE.get(Constants.SENSORID_GPS);
		SensorDataOfOneType gpsData = allSensorData.getCouponDataOfType(_cid, sensorId).getDeepCopy();
		if (gpsData != null) {
			_gpsAd = new GpsDataAdapter(gpsData, _gpsSensorFireTimeInterval, _minTReqRule);
		}
		else {
			_gpsAd = null;
		}
	}
	
	public OneReport getPerformanceEvalData(OneReport rep) {
		doTheWork();
		
		Assertion.test(
				_earlyAns.size() + _answersLeft.size() + _goodAnsCount + _lateAns.size() 
				== _numRuleFiresTotal, "not all answers are accounted for");

		rep.addValue(ConstTags.REPORTS_COUPONNAME, _cIdToNames.get(_cid));
		rep.addValue(ConstTags.REPORTS_COUPONID, Integer.toString(_cid));
		rep.addValue(ConstTags.REPORTS_RULEID, Integer.toString(_rid));
		rep = getGoodFireCounts(rep);
		rep = getLateAndMissedFireCounts(rep);
		rep = getEarlyFireCounts(rep);
		// 2*SI and minTReq
		rep.addValue(ConstTags.REPORTS_SENSOR_INTERVAL, Double.toString(_gpsSensorFireTimeInterval));
		rep.addValue(ConstTags.REPORTS_RULE_MIN_T, _minTReqRule, ConstTags.REPORTS_R_M_T_TEXT);
		return rep;
	}
	
	protected abstract void doTheWork();
	
	protected abstract double getVeryFirstShouldFireTime();
	
	protected final void shouldFireRule(double tNowAndIdealFireT) {
		boolean shouldFire = filtersPassed(tNowAndIdealFireT);
		
		if ( ! shouldFire ) {
			_ruleTrueFilterFalseCount++;
		}
		else {
			_idealWorldNumRuleFires++;  
			if (_answersLeft.size() > 0) {  // either ans left is already 0 len, or issue in ismissed counting
				findGoodAnswer(tNowAndIdealFireT);
			}
			else {
				_likelyMissedAnsCount++;
			}
		}
		// dummy true fire time when the filter condition was not met and/or there are no answers left
		// to be able to continue looping through all the times it should fire
		if ( ! shouldFire || _answersLeft.size() <= 0){
			_trueFireT = tNowAndIdealFireT;
		}
		
		//Assertion.test(_trueFireT >= tNowAndIdealFireT, "coupon " + _cid + " rule " + _rid + " moving BACK from " 
		//+ tNowAndIdealFireT + " to " + _trueFireT + " by " + (tNowAndIdealFireT - _trueFireT));
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
	
	private void setTimesAndAnsLeft(double stopTimeInSecs, double windowInHrs, double startTimeInSecs, 
			CouponCollection coupons, int cid, AnswersCollection answers) {
		// if no time restraints are given
        if (stopTimeInSecs == -1.0 && startTimeInSecs == -1.0 && windowInHrs == -1.0) {
			_startTimeInSecs = coupons.getCouponById(cid).getLastRegistrationTime().getTimeInMillis() / 1000.0;
			_stopTimeInSecs = (coupons.getCouponById(cid).getStudyEndTime().getTimeInMillis() / 1000.0) + 9999.0;
			_answersLeft = answers.getAnsForRuleAndCid(_cid, _rid);
		}
        // if some time restraints are given
        else {
        	// if a time window is given
        	if (windowInHrs != -1.0) {
        		double windowInSecs = windowInHrs * 60 * 60;
        		
        		if (stopTimeInSecs == -1.0 && startTimeInSecs == -1.0) {
                	_startTimeInSecs = coupons.getCouponById(cid).getLastRegistrationTime().getTimeInMillis() / 1000.0;
                	_stopTimeInSecs = startTimeInSecs + windowInSecs;
                }
        		else if (stopTimeInSecs == -1.0) {
        			_startTimeInSecs = startTimeInSecs;
                	_stopTimeInSecs = startTimeInSecs + windowInSecs;
                }
                else if (startTimeInSecs == -1.0) {
                	_startTimeInSecs = stopTimeInSecs - windowInSecs;
                	_stopTimeInSecs = stopTimeInSecs;
                }
                else {
                	_startTimeInSecs = Math.max(startTimeInSecs, coupons.getCouponById(cid).getLastRegistrationTime().getTimeInMillis() / 1000.0);
                	_stopTimeInSecs = startTimeInSecs + windowInSecs;
                }
        	}
        	// if no time window is given
            else {
            	if (stopTimeInSecs == -1.0 && windowInHrs == -1.0) {
            		_startTimeInSecs = startTimeInSecs;
                	_stopTimeInSecs = (coupons.getCouponById(cid).getStudyEndTime().getTimeInMillis() / 1000.0) + 9999.0;
                }
                else if (startTimeInSecs == -1.0 && windowInHrs == -1.0) {
        			_startTimeInSecs = coupons.getCouponById(cid).getLastRegistrationTime().getTimeInMillis() / 1000.0;
        			_stopTimeInSecs = stopTimeInSecs;
                }
                else {
                	_startTimeInSecs = Math.max(startTimeInSecs, coupons.getCouponById(cid).getLastRegistrationTime().getTimeInMillis() / 1000.0);
                	_stopTimeInSecs = Math.min(stopTimeInSecs, (coupons.getCouponById(cid).getStudyEndTime().getTimeInMillis() / 1000.0) + 9999.0);
                }
            }
        	// answers with time restraints 
        	_answersLeft = answers.getAnswersInTimeWindowForCidAndRid(_cid, _rid, _startTimeInSecs, _stopTimeInSecs);
        }
	}
	
	private OneAnswer findClosestAnswer(double t) {
		double smallestDiff = 99999999999.9;
		OneAnswer toReturn = null;

		for (OneAnswer a: _answersLeft) {
			//_trueFireT = a.getRuleFiredTime().getTimeInMillis() / 1000.0;
			double fireT = a.getNotificationReceivedTime().getTimeInMillis() / 1000.0;
			double tempDiff = Math.abs(t - fireT);
			
			if (tempDiff < smallestDiff) {
				smallestDiff = tempDiff;
				toReturn = a;
			} 
			else { break; }
		}
		
		return toReturn;
	}
	
	private void findGoodAnswer(double tNowAndIdealFireT) {
		
		OneAnswer ans = findClosestAnswer(tNowAndIdealFireT);
		
		//_trueFireT = ans.getRuleFiredTime().getTimeInMillis() / 1000.0;
		_trueFireT = ans.getNotificationReceivedTime().getTimeInMillis() / 1000.0;
		
		// if the answer's fire time is NOT MISSED compared to the time it should fire (not too early or too late),
		if (! isMissed(tNowAndIdealFireT) ) {
			
			// if the fire time is EARLIER than the allowed divergence from the ideal fire time, 
			if (_trueFireT < tNowAndIdealFireT - _allowedDivergenceOnTimeFireT) {
				_earlyAnsCount++;
				_earlyAns.add(ans);
				// remove this answer from the list of answers to avoid counting it again
				Assertion.test(_answersLeft.remove(ans), "delete did not work");
			}

			// if the fire time is LATER than the allowed divergence from the ideal fire time, 
			else if (_trueFireT > tNowAndIdealFireT + _allowedDivergenceOnTimeFireT) {
				_lateAnsCount++;
				_lateAns.add(ans);
				// remove this answer from the list of answers to avoid counting it again
				Assertion.test(_answersLeft.remove(ans), "delete did not work");
			}
			
			// if the fire time is roughly the same as the ideal fire time, 
			else {
				// then it is a good answer (fired roughly when it should have)
				_goodAnsCount++;
				// remove this answer from the list of answers to avoid counting it again
				Assertion.test(_answersLeft.remove(ans), "delete did not work");
			}
		}
	}
	
	private boolean isMissed(double idealT) {
		double tDiff = Math.abs(idealT - _trueFireT);
		// if the answer is off by less than the given amount, it was not missed
		if (tDiff <= _allowedDivergenceNotMissedFireT) {
			return false;
		// if the answer is off by more than the given amount, it was missed
		} else {
			_likelyMissedAnsCount++;
			_trueFireT = idealT;
			return true;
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
		// cutoff for deciding if on time
		map.addValue(ConstTags.REPORTS_RULE_ALLW_DEV_ONTIME, _allowedDivergenceOnTimeFireT,
				ConstTags.REPORTS_R_A_D_OT_TEXT);
		
		// counts of skipped, expired, powered off, and responded to answers 
		map.addValue(ConstTags.REPORTS_SKIPPED_RULE_FIRES, _numSkippedAnswerRuleFiresTotal * 1.0, 
				ConstTags.REPORTS_S_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_EXPIRED_RULE_FIRES, _numExpiredAnswerRuleFires * 1.0, 
				ConstTags.REPORTS_EX_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_POWEREDOFF_RULE_FIRES, _numPoweredOffAnswerRuleFires * 1.0, 
				ConstTags.REPORTS_PO_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_RESPONDED_RULE_FIRES, _numAnsweredRuleFiresTotal * 1.0, 
				ConstTags.REPORTS_R_R_F_TEXT);
		
		// counts of good, total, and ideal rule fires
		map.addValue(ConstTags.REPORTS_TOTAL_RULE_FIRES, _numRuleFiresTotal * 1.0, ConstTags.REPORTS_T_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_IDEAL_RULE_FIRES, _idealWorldNumRuleFires * 1.0, ConstTags.REPORTS_I_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_GOOD_RULE_FIRES, _goodAnsCount * 1.0, ConstTags.REPORTS_G_R_F_TEXT);

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
		
		map.addValue(ConstTags.REPORTS_RULE_TRUE_FILTER_FALSE, (double) _ruleTrueFilterFalseCount, ConstTags.REPORTS_R_T_F_F);

		return map;
	}

	private OneReport getLateAndMissedFireCounts(OneReport map) {
		// cutoff for deciding if missed
		map.addValue(ConstTags.REPORTS_RULE_ALLW_DEV_NOTMISSED, _allowedDivergenceNotMissedFireT,
				ConstTags.REPORTS_R_A_D_M_TEXT);
		// counts of late and missed fires
		map.addValue(ConstTags.REPORTS_LATE_RULE_FIRES, (double) _lateAnsCount, ConstTags.REPORTS_L_R_F_TEXT);
		map.addValue(ConstTags.REPORTS_MISSED_RULE_FIRES, (double) _likelyMissedAnsCount, ConstTags.REPORTS_M_R_F_TEXT);
		// all late or missed answer times
		if (_lateAnsCount > 0) {
			for (int i = 0; i < _lateAns.size(); i++) {
				long millis = _lateAns.get(i).getRuleFiredTime().getTimeInMillis();
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(millis);
				map.addValue(ConstTags.REPORTS_LATE_ANS(i), (double) millis, ConstTags.REPORTS_L_A_TEXT(i));
			}
		}
		return map;
	}

	private OneReport getEarlyFireCounts(OneReport map) {
		// count of early fires
		map.addValue(ConstTags.REPORTS_EARLY_RULE_FIRES, (double) _earlyAnsCount, ConstTags.REPORTS_E_R_F_TEXT);
		// all early answer times
		if (_earlyAns.size() > 0) {
			for (int i = 0; i < _earlyAns.size(); i++) {
				map.addValue(ConstTags.REPORTS_EARLY_ANS(i),
						(double) _earlyAns.get(i).getRuleFiredTime().getTimeInMillis(),
						ConstTags.REPORTS_E_A_TEXT(i));
			}
		}
		// count of not late, missed, early, or on time fires
		map.addValue(ConstTags.REPORTS_OTHER_RULE_FIRES, (double) _answersLeft.size(), ConstTags.REPORTS_O_R_F_TEXT);

		return map;
	}
}
	
	/*private void findGoodAnswer(double tNowAndIdealFireT) {
		OneAnswer ans;
		int lenBefore = _answersLeft.size();
		boolean wasEarly = false;

		// loop through answers
		for (int i = 0; i < _answersLeft.size(); i++) {
			ans = _answersLeft.get(i);

			//_trueFireT = ans.getRuleFiredTime().getTimeInMillis() / 1000.0;
			_trueFireT = ans.getNotificationReceivedTime().getTimeInMillis() / 1000.0;

			// if this answer's fire time is later than or equal to the ideal fire time
			if (_trueFireT >= tNowAndIdealFireT) {

				// if this answer's fire time is also earlier than the allowed maximum time,
				double t = tNowAndIdealFireT + _allowedDivergenceOnTimeFireT;
				if (_trueFireT <= t) {
					// then it is a good answer (fired roughly when it should have)
					_goodAnsCount++;
				}
				// if this answer's fire time is also later than the allowed maximum time,
				// it is a late answer; but count this as the next fire time
				else {
					if (wasEarly) {
						break;  // if an early answer was found, do not continue on to a late answer
					}
					double tAmountLate = _trueFireT - tNowAndIdealFireT;
					// if the answer is late by less than the given amount, it is a late answer
					if (tAmountLate < _allowedDivergenceNotMissedFireT) {
						_lateAnsCount++;
						_lateAns.add(ans);
					// if the answer is late by more than the given amount, it is a missed answer
					} else {
						_likelyMissedAnsCount++;
						break;  // to avoid removing this answer from answers left because it may actually be an early fire
						// but then all missed would automatically be early
					}
				}
				// remove this answer from the list of answers to avoid counting it again
				_answersLeft.remove(i);
				Assertion.test(lenBefore == _answersLeft.size() + 1, "delete did not work");
				break;
			}
			
			// if this answer's fire time is too early (this also counts "early" answers due to a time filter)
			else {
				wasEarly = true;
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
	}*/
