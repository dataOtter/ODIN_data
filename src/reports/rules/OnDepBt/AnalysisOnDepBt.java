package reports.rules.OnDepBt;

import constants.ConstTags;
import dao.CouponCollection;
import maps.IMJ_Map;
import reports.rules.AbsAnalysisRule;
import reports.rules.AnswersCollection;
import reports.rules.RulesCollection;
import sensors.data.SensorDataCollection;

public class AnalysisOnDepBt extends AbsAnalysisRule {

    // _answers contains all answers, regardless of cid and rid        
    public AnalysisOnDepBt(AnswersCollection answers, RulesCollection rules, 
    		SensorDataCollection allSensorData, double sensorFireTimeInterval, int cid, int rid, CouponCollection coupons,
    		double stopTimeInSecs, double windowInHrs, IMJ_Map<Integer, String> cIdToNames) {
    	
    	super(ConstTags.REPORT_TYPE_ONDEPBT_RULE_ANALYSIS, new String[]{ConstTags.REPORTS_REL_DATA_BT});
    	
        _eval = new OnDepBtPerformanceEval(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, coupons,
        		stopTimeInSecs, windowInHrs, cIdToNames);
    }
}