package dao.rules;

import constants.Constants;

/**
 *
 * @author Maisha Jauernig
 */
public class OneRule {
    private final Integer _ruleId;
    private final Integer _questionId;
    private final String _ruleType;
    private final AbsRuleParams _params;
    private final int _formatVersion;
    
    public OneRule(int formatVersion, String ruleRow) {
    	_formatVersion = formatVersion;
    	String[] line = ruleRow.split(",");
        _ruleId = Integer.parseInt(line[Constants.RULES_RULEID_IDX]);
        _questionId = Integer.parseInt(line[Constants.RULES_QUESTIONID_IDX]);
        _ruleType = line[Constants.RULES_RULETYPE_IDX];
    	_params = AbsRuleParams.parseFromString(ruleRow, _ruleType, _formatVersion);
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
}
