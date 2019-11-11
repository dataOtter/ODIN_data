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
    
    /**
     * @param formatVersion
     * @param ruleRow - one row of the rules.csv as a String
     */
    public OneRule(int formatVersion, String ruleRow) {
    	_formatVersion = formatVersion;
    	String[] line = ruleRow.split(",");
        _ruleId = Integer.parseInt(line[Constants.RULES_RULEID_IDX]);
        _questionId = Integer.parseInt(line[Constants.RULES_QUESTIONID_IDX]);
        _ruleType = line[Constants.RULES_RULETYPE_IDX];
    	_params = AbsRuleParams.parseFromString(ruleRow, _ruleType, _formatVersion);
    	_filters = Filter.parseFromString(ruleRow);
    }
    
    /**
     * @return the parameters AbsRuleParams of this rule
     */
    public AbsRuleParams getParams() {
        return _params;
    }
    
    /**
     * @return the question ID associated with this rule as an int
     */
    public int getQuestionId() {
    	return _questionId;
    }

	/**
	 * @return this rule's filters as an IMJ_OC<Filter>
	 */
	public IMJ_OC<Filter> getFilters() {
		return _filters;
	}
	
	/**
     * @return  the rule type of this rule as a String
     */
    public String getRuleType(){
        return _ruleType;
    }
    
    /**
     * @return the rule ID of this rule as an int
     */
    public int getRuleId(){
        return _ruleId;
    }
}
