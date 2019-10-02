package studysensors.whileAt;

import Constants.ConstTags;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import stats.*;
import studysensors.IAnalysis;
import studysensors.gps.GpsDataCollection;
import studysensors.rules.*;
/**
 *
 * @author Maisha Jauernig
 */
public class AnalysisWhileAt implements IAnalysis{
    private final WhileAtPerformanceEval _eval;

    // _answers contains all answers, regardless of cid and rid        
    public AnalysisWhileAt(AnswersCollection answers, RulesCollection rules, 
    		GpsDataCollection allGpsSensorData, double sensorFireTimeInterval, int cid, int rid) {
        _eval = new WhileAtPerformanceEval(answers, rules, allGpsSensorData, sensorFireTimeInterval, cid, rid);
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
