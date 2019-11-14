package reports.rules;

import sensors.data.SensorDataCollection;
import sensors.gps.GpsCoordinate;
/**
*
* @author Maisha Jauernig
*/
public abstract class AbsGpsRulePerformanceEval extends AbsRulePerformanceEval {
	protected final Predicate _pred;

	public AbsGpsRulePerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double sensorFireTimeInterval, int cid, int rid, double minTReq) {

		super(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, minTReq);

		AbsGpsRuleParams params = (AbsGpsRuleParams)rules.getRuleById(rid).getParams();
		_pred = new PredicateInLocRadius(new GpsCoordinate(params.getLat(), params.getLon()), params.getDist());

		double gpsMaxT = _gpsAd.getLastRecordingTime() + _sensorFireTimeInterval - 1;  
		double ansMaxT = 0.0;
		if (_answersLeft.size() > 0) {
			ansMaxT = _answersLeft.get(_answersLeft.size() - 1).getRuleFiredTime().getTimeInMillis() / 1000.0;
		}
		_maxAnsT = Math.max(gpsMaxT, ansMaxT);
	}
}
