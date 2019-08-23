package studysensors.rules;

import dao.OneRule;
import orderedcollection.*;

public class RulesCollection {
	private IMJ_OC<OneRule> _allRules;
	
	public RulesCollection(IMJ_OC<OneRule> rules) {
		_allRules = rules.getDeepCopy();
	}
	
	public RulesCollection getRulesCollectionByType(String ruleType){
		IMJ_OC<OneRule> newRules = new MJ_OC_Factory<OneRule>().create();
		OneRule r;
		for (int i=0; i<_allRules.length(); i++) {
			r = _allRules.getItem(i);
			if (r.getRuleType().contains(ruleType)) {
				newRules.append(r);
			}
		}
		RulesCollection c = new RulesCollection(newRules);
		return c;
    }
	
	public IMJ_OC<Integer> getAllRids() {
		IMJ_OC<Integer> rids = new MJ_OC_Factory<Integer>().create();
		OneRule r;
		for (int i = 0; i<_allRules.length(); i++) {
			r = _allRules.getItem(i);
			rids.append(r.getRuleId());
		}
		return rids;
	}
	
	public IMJ_OC<OneRule> getRules(){
		return _allRules;
	}
	
	public OneRule getRuleById(int rid) {
		OneRule rule;

		for (int i = 0; i<_allRules.length(); i++) {
			rule = _allRules.getItem(i);
            if (rule.getRuleId() == rid){
                return rule;
            }
        }
        return null;
	}
}
