package reports.rules.OnDepBt;

import constants.ConstTags;
import reports.rules.AbsAnalysisRule;
import reports.rules.AnswersCollection;
import reports.rules.RulesCollection;
import sensors.data.SensorDataCollection;

public class AnalysisOnDepBt extends AbsAnalysisRule {

    // _answers contains all answers, regardless of cid and rid        
    public AnalysisOnDepBt(AnswersCollection answers, RulesCollection rules, 
    		SensorDataCollection allSensorData, double sensorFireTimeInterval, int cid, int rid) {
    	
    	super(ConstTags.REPORT_TYPE_ONDEPBT_RULE_ANALYSIS);
    	
        _eval = new OnDepBtPerformanceEval(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid);
    }
}