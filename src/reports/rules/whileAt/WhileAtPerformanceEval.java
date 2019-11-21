package reports.rules.whileAt;

import Assert.Assertion;
import reports.rules.*;
import sensors.data.SensorDataCollection;
import sensors.data.GpsDataPoint;
import sensors.gps.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public class WhileAtPerformanceEval extends AbsGpsRulePerformanceEval {
	private double _curMinTBetweenFires = 0.0;
	private double _totalTimeAtLoc = 0.0;
	private double _prevIdealFireT = 0.0;
	private int _atLocConsecCount = 0;

	public WhileAtPerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double sensorFireTimeInterval, int cid, int rid) {
		
		super(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, getMinTReq(rules, rid));
	}
	
	private static double getMinTReq(RulesCollection rules, int rid) {
		return ( (WhileAtRuleParams) rules.getRuleById(rid).getParams() ).getMinTimeSinceLastFire() * 1.0;
	}

	@Override
	protected void doTheWork() {
		boolean atLoc = false;
		double veryFirstFireT = getVeryFirstShouldFireTime();
		double firstFireT = veryFirstFireT;
		
		// loop through time by minT intervals, skipping absences from the rule location
		for (double fireT = veryFirstFireT; fireT <= _maxAnsT; fireT += _curMinTBetweenFires) {
			GpsDataPoint p = (GpsDataPoint)_gpsAd.getDataPointAndClearPreceeding(fireT);
			GpsCoordinate c = p.getGpsCoord();

			// if we are roughly at the required location
			if (_pred.test(c)) {
				shouldFireRule(fireT);
				updateTotalTimeAtLoc(fireT);
				// set fireT to when it actually fired
				fireT = _trueFireT;
				if (atLoc == false) {  // if this is the first occurrence of being at this loc (again)
					firstFireT = fireT;  // set the first fire time at this loc to the ideal fire time
				}
				atLoc = true;
			} 
			else {
				atLoc = false;
				_atLocConsecCount = 0;
				// use fireT as ideal fire time to get count of ideal world fires, note that
				// this may still not be entirely accurate as each fireT is based on the previous trueFireT and is not an absolute measure
				locNotWithinRadius(firstFireT);  
				// set fireT to the time of the next gps recording at the location
				fireT = _gpsAd.getNextStartTime(fireT, _pred);
				if (fireT <= 0.0) { // if there are no more recordings at the location
					break;
				}
			}
		}
		
		// if the last recording was at the location, round off the calculations of total time spent there
		if (atLoc) {
			locNotWithinRadius(firstFireT);
		}
	}

	@Override
	protected double getVeryFirstShouldFireTime() {
		double t = _gpsAd.getFirstRecordingTime();
		GpsDataPoint p = (GpsDataPoint)_gpsAd.getDataPointAndClearPreceeding(t);
		GpsCoordinate c = p.getGpsCoord();
		// if the first gps recording time is not at the rule location,
		if (!_pred.test(c)) {
			// get the time for the first recording at the rule location
			t = _gpsAd.getNextStartTime(t, _pred);
		}
		// add the initial 2*SI to the first time at the rule location
		// to get the first time the rule should fire
		t += 2 * _gpsSensorFireTimeInterval;
		return t;
	}

	private void updateTotalTimeAtLoc(double tNowAndIdealFireT) {
		_atLocConsecCount++;
		// if this is the first time the rule should fire as per the location predicate,
		// add 2*SI to the total time spent there as no previous fire time exists yet
		// and in case this fire was late, also add the amount it was late by (+ _trueFireT - tNowAndIdealFireT)
		if (_atLocConsecCount == 1) {
			_totalTimeAtLoc += 2 * _gpsSensorFireTimeInterval + _trueFireT - tNowAndIdealFireT;
		} else {
			_totalTimeAtLoc += _trueFireT - _prevIdealFireT;
		}
		_prevIdealFireT = _trueFireT;
		_curMinTBetweenFires = _minTReqRule;
	}

	private void locNotWithinRadius(double firstFireT) {
		// presence at the location is counted until just before the first GPS recording not at the location
		// the below calculation is not quite accurate, as we do not know the exact time we switched location,
		// just that the next time we check based on the minT intervals was not at the location.
		// to get a rough estimate, taking half of the minT interval during which we switched locations
		_totalTimeAtLoc += _curMinTBetweenFires / 2.0;
		// if the time spent at this location is more than 2 * SI
		// (i.e. there should be at least one rule fire at this location, not taking filters into account),
		if (_totalTimeAtLoc >= _gpsSensorFireTimeInterval * 2) {
			// subtract 2*SI from total time because the first minT between rule fires is 2*SI
			_totalTimeAtLoc -= _gpsSensorFireTimeInterval * 2;
			// then get the count of times it should have fired
			// by looping from the first fire time to the last fire time, by minTReq
			for (double i = firstFireT; i <= (firstFireT+_totalTimeAtLoc); i+=_minTReqRule) {
				if ( filtersPassed(i) ) {
					_idealWorldNumRuleFires++;
				}
			}
			// then get the remaining count of times it should have fired, if any
			//_idealWorldNumRuleFires += Math.floor(_totalTimeAtLoc / _minTReq);
		}
		_curMinTBetweenFires = 2 * _gpsSensorFireTimeInterval;
		_totalTimeAtLoc = 0.0;
	}
}
