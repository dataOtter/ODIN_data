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
	private IMJ_OC<String> _relatedDataNames;
      
    public AbsAnalysisRule(String type, String[] relatedDataNames) {
    	_relatedDataNames = new MJ_OC_Factory<String>().create();
    	_type = type;
    	for (String s: relatedDataNames) {
    		_relatedDataNames.add(s);
    	}
    	_relatedDataNames.add(ConstTags.REPORTS_REL_DATA_ANSWERS);
    }
    
    @Override
    public final OneReport getAnalysisReport() {
    	OneReport rep = new OneRuleReport(_relatedDataNames);
    	
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
