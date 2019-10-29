package reports.rules.whileAt;

import constants.ConstTags;
import filters.Filter;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
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
public class AnalysisWhileAt implements IAnalysis{
    private final WhileAtPerformanceEval _eval;

    // _answers contains all answers, regardless of cid and rid        
    public AnalysisWhileAt(AnswersCollection answers, RulesCollection rules, 
    		SensorDataCollection allSensorData, double sensorFireTimeInterval, int cid, int rid) {
        _eval = new WhileAtPerformanceEval(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid);
    }
    
    @Override
    public OneReport getAnalysisReport() {

    	IMJ_OC<String> relatedDataNames = new MJ_OC_Factory<String>().create();
    	relatedDataNames.add(ConstTags.REPORTS_REL_DATA_ANSWERS);
    	relatedDataNames.add(ConstTags.REPORTS_REL_DATA_GPS);
    	
    	OneReport rep = new OneRuleReport(relatedDataNames);
    	
    	rep = _eval.getWhileAtPerformanceEvalData(rep);
        return rep;
    }

	@Override
	public String getAnalysisType() {
		return ConstTags.REPORT_TYPE_WHILEAT_RULE_ANALYSIS;
	}
}
