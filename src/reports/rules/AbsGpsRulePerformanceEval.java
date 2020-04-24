package reports.rules;

import constants.Constants;
import dao.CouponCollection;
import maps.IMJ_Map;
import sensors.data.SensorDataCollection;
import sensors.gps.GpsCoordinate;
/**
*
* @author Maisha Jauernig
*/
public abstract class AbsGpsRulePerformanceEval extends AbsRulePerformanceEval {
	protected final Predicate _pred;

	public AbsGpsRulePerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double sensorFireTimeInterval, int cid, int rid, double minTReq, CouponCollection coupons, 
			double stopTimeInSecs, double windowInHrs, IMJ_Map<Integer, String> cIdToNames, double startTimeInSecs) {

		super(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, minTReq, 
				Constants.PERC_ALLOWED_DEV_FROM_GIVEN_TIME_ONTIME * minTReq, 
				Constants.PERC_ALLOWED_DEV_FROM_GIVEN_TIME_NOTMISSED * minTReq, coupons, stopTimeInSecs, 
				windowInHrs, cIdToNames, startTimeInSecs);

		AbsGpsRuleParams params = (AbsGpsRuleParams)rules.getRuleById(rid).getParams();
		_pred = new PredicateInLocRadius(new GpsCoordinate(params.getLat(), params.getLon()), params.getDist());
	}
}
