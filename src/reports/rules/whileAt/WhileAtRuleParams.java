package reports.rules.whileAt;

import constants.Constants;
import reports.rules.AbsGpsRuleParams;

public class WhileAtRuleParams extends AbsGpsRuleParams {
    private final Integer _minTime;

	public WhileAtRuleParams(String ruleRow, int formatVersion) {
		super(ruleRow, formatVersion);
        _minTime = Integer.parseInt(this.getParamVal(Constants.RULES_MINTLASTFIRE));
	}

	public Integer getMinTimeSinceLastFire() {
		return _minTime;
	}
}
