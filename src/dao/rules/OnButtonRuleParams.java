package dao.rules;

import constants.Constants;

public class OnButtonRuleParams extends AbsRuleParams {
    private final Integer _id;
    private final String _label;

	OnButtonRuleParams(String ruleRow, int formatVersion) {
		super(ruleRow, formatVersion);
		_id = Integer.parseInt(_paramNameToVal.get(Constants.RULES_BUTTONID));
		_label = _paramNameToVal.get(Constants.RULES_BUTTONLABEL);
	}

	public Integer getId() {
		return _id;
	}

	public String getLabel() {
		return _label;
	}
}
