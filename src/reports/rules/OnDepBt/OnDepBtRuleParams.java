package reports.rules.OnDepBt;

import constants.Constants;
import reports.rules.AbsRuleParams;
/**
*
* @author Maisha Jauernig
*/
public class OnDepBtRuleParams extends AbsRuleParams {
	private final String _proximity;
    private final int _count;
    private final int _delay;

    public OnDepBtRuleParams(String ruleRow, int formatVersion) {
		super(ruleRow, formatVersion);
		_proximity = this.getParamVal(Constants.RULES_PROXIMITY);
		_count = Integer.parseInt(this.getParamVal(Constants.RULES_COUNT));
		_delay = Integer.parseInt(this.getParamVal(Constants.RULES_DELAY));
	}

	public String getProximity() {
		return _proximity;
	}

	public int getCount() {
		return _count;
	}

	public int getDelay() {
		return _delay;
	}
}