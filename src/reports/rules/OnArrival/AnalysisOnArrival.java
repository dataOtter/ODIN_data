package reports.rules.OnArrival;

import constants.ConstTags;
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
    		SensorDataCollection allSensorData, double sensorFireTimeInterval, int cid, int rid) {
    	
    	super(ConstTags.REPORT_TYPE_ONARRIVAL_RULE_ANALYSIS);
    	
        _eval = new OnArrivalPerformanceEval(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid);
    }
}
