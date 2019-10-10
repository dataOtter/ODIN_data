package dao.rules;

import constants.Constants;

public class FollowUpRuleParams extends AbsRuleParams {
    private final Integer _questionId;
    private final String _choicesList;

    FollowUpRuleParams(String ruleRow, int formatVersion) {
		super(ruleRow, formatVersion);
		_questionId = Integer.parseInt(_paramNameToVal.get(Constants.RULES_QUESTIONID));
        _choicesList = _paramNameToVal.get(Constants.RULES_CHOICESLIST);
	}

	public Integer getQuestionId() {
		return _questionId;
	}

	public String getChoicesList() {
		return _choicesList;
	}
}
