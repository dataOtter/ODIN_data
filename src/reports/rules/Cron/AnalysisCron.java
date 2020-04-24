package reports.rules.Cron;

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
public class AnalysisCron extends AbsAnalysisRule {

    // _answers contains all answers, regardless of cid and rid        
    public AnalysisCron(AnswersCollection answers, RulesCollection rules, 
    		SensorDataCollection allSensorData, double sensorFireTimeInterval, int cid, int rid, CouponCollection coupons,
    		double stopTimeInSecs, double windowInHrs, IMJ_Map<Integer, String> cIdToNames, double startTimeInSecs) {
    	
    	super(ConstTags.REPORT_TYPE_CRON_RULE_ANALYSIS, new String[]{});
    	
        _eval = new CronPerformanceEval(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, coupons,
        		stopTimeInSecs, windowInHrs, cIdToNames, startTimeInSecs);
    }
}
