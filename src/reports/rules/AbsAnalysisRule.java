package reports.rules;

import constants.ConstTags;
import orderedcollection.*;
import reports.IAnalysis;
import reports.OneReport;
/**
*
* @author Maisha Jauernig
*/
public class AbsAnalysisRule implements IAnalysis {
	protected AbsRulePerformanceEval _eval;
	private String _type;
      
    public AbsAnalysisRule(String type) {
    	_type = type;
    }
    
    @Override
    public final OneReport getAnalysisReport() {
    	IMJ_OC<String> relatedDataNames = new MJ_OC_Factory<String>().create();
    	relatedDataNames.add(ConstTags.REPORTS_REL_DATA_ANSWERS);
    	relatedDataNames.add(ConstTags.REPORTS_REL_DATA_GPS);
    	
    	OneReport rep = new OneRuleReport(relatedDataNames);
    	
    	rep = _eval.getPerformanceEvalData(rep);
        return rep;
    }

	@Override
	public final String getAnalysisType() {
		return _type;
	}
	
	@Override
	public final String toString() {
		return "contains " + _type + " PerformanceEval";
	}
}
