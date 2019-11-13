package reports.rules;

import constants.Constants;
import maps.*;
import reports.rules.Cron.CronRuleParams;
import reports.rules.OnArrival.OnArrivalOrDepRuleParams;
import reports.rules.whileAt.WhileAtRuleParams;

public abstract class AbsRuleParams {
	IMJ_Map<String, String> _paramNameToVal;
    private final int _formatVersion;
	
	protected AbsRuleParams (String ruleRow, int formatVersion){
    	_formatVersion = formatVersion;
    	_paramNameToVal = extractParameters(ruleRow);
	}
	
	public static AbsRuleParams parseFromString(String ruleRow, String ruleType, int formatVersion) {
		AbsRuleParams ruleParam = null;
		if (ruleType.contains(Constants.RULE_WHILEAT_NOTAT)) {
			ruleParam =  new WhileAtRuleParams(ruleRow, formatVersion);
		}
		else if ((ruleType.contains(Constants.RULE_ONARRIVAL) || ruleType.contains(Constants.RULE_ONDEPARTURE)) 
				&& ! ruleType.contains(Constants.RULE_BLUETOOTH)) {
			ruleParam =  new OnArrivalOrDepRuleParams(ruleRow, formatVersion);
		}
		else if (ruleType.contains(Constants.RULE_CRON)) {
			ruleParam =  new CronRuleParams(ruleRow, formatVersion);
		}
		else if (ruleType.contains(Constants.RULE_ONBUTTON)) {
			ruleParam =  new OnButtonRuleParams(ruleRow, formatVersion);
		}
		else if (ruleType.contains(Constants.RULE_FOLLOWUP)) {
			ruleParam =  new FollowUpRuleParams(ruleRow, formatVersion);
		}
		return ruleParam;
	}
	
	protected void addParam(String key, String val) {
		_paramNameToVal.put(key, val);
	}
	
	protected String getParamVal(String key) {
		return _paramNameToVal.get(key);
	}
	
	private IMJ_Map<String, String> extractParameters(String ruleRow) {
		ruleRow = ruleRow.replaceAll("\\\\",  "").replaceAll("\"", "");
        //int paramIdx = 1; // default/format version 1
        if (_formatVersion == 2) {
    		ruleRow = ruleRow.substring(ruleRow.indexOf(Constants.RULE_PARAM_TO_VAL));
        }  
        String paramSection = ruleRow.split("\\{")[1].split("\\}")[0];
        
        IMJ_Map<String, String> params = new MJ_Map_Factory<String, String>().create();
        
        while (paramSection.length() > 0) {
	        int i = paramSection.lastIndexOf(":");
	        String val = paramSection.substring(i+1);
	        paramSection = paramSection.substring(0, i);
	        int j = paramSection.lastIndexOf(",");
	        String key = paramSection.substring(j+1);
	        if (j < 0) { j = 0; }
	        paramSection = paramSection.substring(0, j);
	        params.put(key,  val); 
        }
        
    	return params;
    }
}
