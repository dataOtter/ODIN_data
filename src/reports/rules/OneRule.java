package reports.rules;

import constants.Constants;
import filters.Filter;
import orderedcollection.*;

/**
 *
 * @author Maisha Jauernig
 */
public class OneRule {
    private final Integer _ruleId;
    private final Integer _questionId;
    private final String _ruleType;
    private final AbsRuleParams _params;
    private final IMJ_OC<Filter> _filters;
    private final int _formatVersion;
    
    public OneRule(int formatVersion, String ruleRow) {
    	_formatVersion = formatVersion;
    	String[] line = ruleRow.split(",");
        _ruleId = Integer.parseInt(line[Constants.RULES_RULEID_IDX]);
        _questionId = Integer.parseInt(line[Constants.RULES_QUESTIONID_IDX]);
        _ruleType = line[Constants.RULES_RULETYPE_IDX];
    	_params = AbsRuleParams.parseFromString(ruleRow, _ruleType, _formatVersion);
    	_filters = Filter.parseFromString(ruleRow);
    }
    
    public String getRuleType() {
        return _ruleType;
    }
    
    public int getRuleId() {
        return _ruleId;
    }
    
    public AbsRuleParams getParams() {
        return _params;
    }
    
    public int getQuestionId() {
    	return _questionId;
    }

	/**
	 * @return the _filters
	 */
	public IMJ_OC<Filter> getFilters() {
		return _filters;
	}
}
