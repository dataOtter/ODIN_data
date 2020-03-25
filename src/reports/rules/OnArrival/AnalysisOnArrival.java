package reports.rules.OnArrival;

import constants.ConstTags;
import dao.CouponCollection;
import maps.IMJ_Map;
import reports.rules.AbsAnalysisRule;
import reports.rules.AnswersCollection;
import reports.rules.RulesCollection;
import sensors.data.SensorDataCollection;
/**
*
* @author Maisha Jauernig
*/
public class AnalysisOnArrival extends AbsAnalysisRule {
	
    // _answers contains all answers, regardless of cid and rid        
    public AnalysisOnArrival(AnswersCollection answers, RulesCollection rules, 
    		SensorDataCollection allSensorData, double sensorFireTimeInterval, int cid, int rid, CouponCollection coupons,
    		double stopTimeInSecs, double windowInHrs, IMJ_Map<Integer, String> cIdToNames) {
    	
    	super(ConstTags.REPORT_TYPE_ONARRIVAL_RULE_ANALYSIS, new String[]{ConstTags.REPORTS_REL_DATA_GPS});
    	
        _eval = new OnArrivalPerformanceEval(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, coupons,
        		stopTimeInSecs, windowInHrs, cIdToNames);
    }
}
