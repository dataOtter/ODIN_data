package reports.rules;

import orderedcollection.*;

/**
 * A collection of OneRule, extends MJ_OC<OneRule>
 * @author Maisha Jauernig
 */
public class RulesCollection extends MJ_OC<OneRule> {
	private IMJ_OC<OneRule> _allRules;
	
	/**
	 * @param rules - IMJ_OC<OneRule> of rules to put into this RulesCollection
	 */
	public RulesCollection(IMJ_OC<OneRule> rules) {
		_allRules = rules.getDeepCopy();
	}
	
	/**
	 * @param ruleType - type of the rule, found in Constants, as String
	 * @return a RulesCollection subset that contains only rules of the given type
	 */
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
	
	/**
	 * @return an IMJ_OC<Integer> of all rule IDs found in this RulesCollection
	 */
	public IMJ_OC<Integer> getAllRids() {
		IMJ_OC<Integer> rids = new MJ_OC_Factory<Integer>().create();
		for (OneRule r: _allRules) {
			rids.add(r.getRuleId());
		}
		return rids;
	}
	
	/**
	 * @param rid - the rule ID for which to get OneRule
	 * @return the OneRule associated with the given rule ID
	 */
	public OneRule getRuleById(int rid) {
		for (OneRule r: _allRules) {
            if (r.getRuleId() == rid){
                return r;
            }
        }
        return null;
	}

	@Override
	public IMJ_OC<OneRule> getDeepCopy() {
		return _allRules.getDeepCopy();
	}

	@Override
	public void prepend(OneRule arg0) {
		_allRules.prepend(arg0);
	}

	@Override
	public void printAll() {
		_allRules.printAll();
	}

	@Override
	public boolean add(OneRule e) {
		return _allRules.add(e);
	}

	@Override
	public void add(int index, OneRule element) {
		_allRules.add(index, element);
	}

	@Override
	public void clear() {
		_allRules.clear();
	}

	@Override
	public OneRule get(int index) {
		return _allRules.get(index);
	}

	@Override
	public OneRule remove(int index) {
		return _allRules.remove(index);
	}

	@Override
	public OneRule set(int index, OneRule element) {
		return _allRules.set(index, element);
	}

	@Override
	public int size() {
		return _allRules.size();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return _allRules.toArray(a);
	}
}
