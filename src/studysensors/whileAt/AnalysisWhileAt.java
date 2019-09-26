package studysensors.whileAt;

import Constants.ConstTags;
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
    	OneReport rep = new OneRuleReport();
    	
    	rep = _eval.getWhileAtPerformanceEvalData(rep);
        return rep;
    }

	@Override
	public String getAnalysisType() {
		return ConstTags.REPORT_TYPE_WHILEAT_RULE_ANALYSIS;
	}
}
