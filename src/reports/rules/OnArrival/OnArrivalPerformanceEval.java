package reports.rules.OnArrival;

import Assert.Assertion;
import reports.rules.*;
import sensors.data.GpsDataPoint;
import sensors.data.SensorDataCollection;
import sensors.gps.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public class OnArrivalPerformanceEval extends AbsGpsRulePerformanceEval {

	public OnArrivalPerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double sensorFireTimeInterval, int cid, int rid) {
		
		super(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, 2*sensorFireTimeInterval);
	}

	@Override
	protected void doTheWork() {
		// step through time by each next time having been at the rule location for at least 2si
		// after not having been at rule location for at least 2si
		for (double fireT = getVeryFirstShouldFireTime(); fireT < _maxAnsT; fireT = getNextShouldFireTime(fireT)) {
			shouldFireRule(fireT, _minTReq);
			// set fireT to when it actually fired
			fireT = _trueFireT;
		}
		Assertion.test(
				_earlyAns.size() + _answersLeft.size() + _goodAnsCount + _lateAns.size() == _numRuleFiresTotal,
				"not all answers are accounted for");
	}

	@Override
	protected double getVeryFirstShouldFireTime() {
		// get next should fire time after first recording time and after not being at rule location for at least 2si
		//return getNextShouldFireTime(_ad.getFirstRecordingTime(), maxTToCheck);
		return getNextShouldFireTime(0.0);
	}
	
	private double getNextShouldFireTime(double tNow) {
		double t = tNow;
		while (t <= _maxAnsT) {
			// get the time for the next first recording at the rule location
			double firstT = _ad.getNextStartTime(t, _pred);
			if (firstT <= 0.0) {
				return _maxAnsT;
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
		
		return _maxAnsT;
	}
}
