package reports.rules.Cron;

import constants.Constants;
import reports.rules.AbsRuleParams;
/**
*
* @author Maisha Jauernig
*/
public class CronRuleParams extends AbsRuleParams {
	private final String _cronManual;
    private final String _cron;

    public CronRuleParams(String ruleRow, int formatVersion) {
		super(ruleRow, formatVersion);
		_cronManual = this.getParamVal(Constants.RULES_CRONMANUAL);
		_cron = this.getParamVal(Constants.RULES_CRON);
	}

	public String getCronManual() {
		return _cronManual;
	}

	public String getCron() {
		return _cron;
	}
}
