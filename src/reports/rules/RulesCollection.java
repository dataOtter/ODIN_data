package reports.rules;

import dao.rules.OneRule;
import orderedcollection.*;

/**
 *
 * @author Maisha Jauernig
 */
public class RulesCollection {
	private IMJ_OC<OneRule> _allRules;
	
	public RulesCollection(IMJ_OC<OneRule> rules) {
		_allRules = rules.getDeepCopy();
	}
	
	public RulesCollection getRulesCollectionByType(String ruleType){
		IMJ_OC<OneRule> newRules = new MJ_OC_Factory<OneRule>().create();
		for (OneRule r: _allRules) {
			if (r.getRuleType().contains(ruleType)) {
				newRules.add(r);
			}
		}
		RulesCollection c = new RulesCollection(newRules);
		return c;
    }
	
	public IMJ_OC<Integer> getAllRids() {
		IMJ_OC<Integer> rids = new MJ_OC_Factory<Integer>().create();
		for (OneRule r: _allRules) {
			rids.add(r.getRuleId());
		}
		return rids;
	}
	
	public IMJ_OC<OneRule> getRules(){
		return _allRules;
	}
	
	public OneRule getRuleById(int rid) {
		for (OneRule r: _allRules) {
            if (r.getRuleId() == rid){
                return r;
            }
        }
        return null;
	}
}
