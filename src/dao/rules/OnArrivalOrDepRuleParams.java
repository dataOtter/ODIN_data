package dao.rules;

import constants.Constants;

public class OnArrivalOrDepRuleParams extends AbsRuleParams {
	private final Integer _dist;
    private final Double _lat;
    private final Double _lon;

    OnArrivalOrDepRuleParams(String ruleRow, int formatVersion) {
		super(ruleRow, formatVersion);
        _dist = Integer.parseInt(_paramNameToVal.get(Constants.RULES_DISTANCE));
        _lat = Double.parseDouble(_paramNameToVal.get(Constants.RULES_LATITUDE));
        _lon = Double.parseDouble(_paramNameToVal.get(Constants.RULES_LONGITUDE));
	}

	public Integer getDist() {
		return _dist;
	}

	public Double getLat() {
		return _lat;
	}

	public Double getLon() {
		return _lon;
	}
}
