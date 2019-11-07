package reports.rules.OnArrival;

import constants.ConstTags;
import orderedcollection.*;
import reports.IAnalysis;
import reports.OneReport;
import reports.rules.AnswersCollection;
import reports.rules.OneRuleReport;
import reports.rules.RulesCollection;
import sensors.data.SensorDataCollection;
/**
*
* @author Maisha Jauernig
*/
public class AnalysisOnArrival implements IAnalysis {
	private final OnArrivalPerformanceEval _eval;

    // _answers contains all answers, regardless of cid and rid        
    public AnalysisOnArrival(AnswersCollection answers, RulesCollection rules, 
    		SensorDataCollection allSensorData, double sensorFireTimeInterval, int cid, int rid) {
        _eval = new OnArrivalPerformanceEval(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid);
    }
    
    @Override
    public OneReport getAnalysisReport() {

    	IMJ_OC<String> relatedDataNames = new MJ_OC_Factory<String>().create();
    	relatedDataNames.add(ConstTags.REPORTS_REL_DATA_ANSWERS);
    	relatedDataNames.add(ConstTags.REPORTS_REL_DATA_GPS);
    	
    	OneReport rep = new OneRuleReport(relatedDataNames);
    	
    	rep = _eval.getPerformanceEvalData(rep);
        return rep;
    }

	@Override
	public String getAnalysisType() {
		return ConstTags.REPORT_TYPE_WHILEAT_RULE_ANALYSIS;
	}
}
