package dao.rules;

import constants.Constants;
import maps.IMJ_Map;
import maps.MJ_Map_Factory;

public abstract class AbsRuleParams {
	IMJ_Map<String, String> _paramNameToVal;
    private final int _formatVersion;
	
	AbsRuleParams (String ruleRow, int formatVersion){
    	_formatVersion = formatVersion;
		String[] params = extractParameters(ruleRow);
		_paramNameToVal = new MJ_Map_Factory<String, String>().create();
		for (String s: params) {
			String[] kv = s.split(":");
			_paramNameToVal.put(kv[0],  kv[1]);
		}
	}
	
	public static AbsRuleParams parseFromString(String ruleRow, String ruleType, int formatVersion) {
		AbsRuleParams ruleParam = null;
		if (ruleType.contains(Constants.RULE_WHILEAT_NOTAT)) {
			ruleParam =  new WhileAtRuleParams(ruleRow, formatVersion);
		}
		else if (ruleType.contains(Constants.RULE_ONARRIVAL) || ruleType.contains(Constants.RULE_ONDEPARTURE)) {
			ruleParam =  new OnArrivalOrDepRuleParams(ruleRow, formatVersion);
		}
		else if (ruleType.contains(Constants.RULE_ONBUTTON)) {
			ruleParam =  new OnButtonRuleParams(ruleRow, formatVersion);
		}
		else if (ruleType.contains(Constants.RULE_FOLLOWUP)) {
			ruleParam =  new FollowUpRuleParams(ruleRow, formatVersion);
		}
		return ruleParam;
	}
	
	private String[] extractParameters(String ruleRow) {
        int paramIdx = 1; // default/format version 1
        if (_formatVersion == 2) {
        	paramIdx = 2;
        }
        String paramSection = ruleRow.split("\\{")[paramIdx].split("\\}")[0];
    	String[] params = paramSection.replaceAll("\\\\",  "").replaceAll("\"", "").split(",");
    	return params;
    }
}
