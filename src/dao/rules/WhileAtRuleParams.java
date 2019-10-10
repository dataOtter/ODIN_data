package dao.rules;

import constants.Constants;

public class WhileAtRuleParams extends AbsRuleParams {
    private final Integer _dist;
    private final Integer _minTime;
    private final Double _lat;
    private final Double _lon;

	WhileAtRuleParams(String ruleRow, int formatVersion) {
		super(ruleRow, formatVersion);
        _dist = Integer.parseInt(_paramNameToVal.get(Constants.RULES_DISTANCE));
        _lat = Double.parseDouble(_paramNameToVal.get(Constants.RULES_LATITUDE));
        _lon = Double.parseDouble(_paramNameToVal.get(Constants.RULES_LONGITUDE));
        _minTime = Integer.parseInt(_paramNameToVal.get(Constants.RULES_MINTLASTFIRE));
	}

	public Integer getDist() {
		return _dist;
	}

	public Integer getMinTimeSinceLastFire() {
		return _minTime;
	}

	public Double getLat() {
		return _lat;
	}

	public Double getLon() {
		return _lon;
	}
}
