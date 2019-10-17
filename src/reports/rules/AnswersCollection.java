package reports.rules;

import dao.OneAnswer;
import orderedcollection.*;
import java.util.Iterator;

import constants.Constants;

/**
 *
 * @author Maisha Jauernig
 */
public class AnswersCollection implements Iterable<OneAnswer> {
	private IMJ_OC<OneAnswer> _allAnswers;
	private IMJ_OC<Integer> _rids = null;
	private IMJ_OC<Integer> _cids;

    public AnswersCollection(IMJ_OC<OneAnswer> answers) {
		_allAnswers = answers.getDeepCopy();
		computeCids();
	}
    
    /***
     * @return just the answers to questions triggered by whileAt rules, for a specific Cid
     */
    public AnswersCollection getOneRuleTypesAnswersForCid(int cid, RulesCollection rules, String ruleType) {
    	RulesCollection oneRuleTypeRulesCollection = rules.getRulesCollectionByType(ruleType);
        IMJ_OC<Integer> ruleRids = oneRuleTypeRulesCollection.getAllRids();
    	AnswersCollection ruleAns = this.getAnswersByRids(ruleRids);
    	
    	if ( ! _cids.contains(cid) ) {
			return new AnswersCollection(new MJ_OC_Factory<OneAnswer>().create());
		}
		else {
			return ruleAns.getAnswersByCids(cid);
		}
    }
    
    public IMJ_OC<Integer> getRids() {
    	if (_rids == null) {
	    	_rids = new MJ_OC_Factory<Integer>().create();
	    	
			for (int i=0; i<_allAnswers.size(); i++) {
				int thisRid = _allAnswers.get(i).getRuleId();
				
				if ( ! _rids.contains(thisRid)) {
					_rids.add(thisRid);
				}
			}
    	}
		return _rids;
    }
	
	public AnswersCollection getAnswersByRids(IMJ_OC<Integer> rids) {
		IMJ_OC<OneAnswer> newAnswers = new MJ_OC_Factory<OneAnswer>().create();
		
		for (OneAnswer a: _allAnswers) {
			int thisRid = a.getRuleId();
    		if (rids.contains(thisRid)) {
    			newAnswers.add(a);
    		}
		}
		
		return new AnswersCollection(newAnswers);
	}
	
	public AnswersCollection getAnswersByRids(int rid) {
		IMJ_OC<OneAnswer> newAnswers = new MJ_OC_Factory<OneAnswer>().create();
		
		for (OneAnswer a: _allAnswers) {
			int thisRid = a.getRuleId();
    		if (rid == thisRid) {
    			newAnswers.add(a);
    		}
		}
		return new AnswersCollection(newAnswers);
	}
	
	public AnswersCollection getAnswersByCids(IMJ_OC<Integer> cids) {
		IMJ_OC<OneAnswer> newAnswers = new MJ_OC_Factory<OneAnswer>().create();
		
		for (OneAnswer a: _allAnswers) {
			int thisCid = a.getCouponId();
    		if (cids.contains(thisCid)) {
    			newAnswers.add(a);
    		}
		}
		
		return new AnswersCollection(newAnswers);
	}
	
	public AnswersCollection getAnswersByCids(int cid) {
		IMJ_OC<OneAnswer> newAnswers = new MJ_OC_Factory<OneAnswer>().create();
		
		for (OneAnswer a: _allAnswers) {
			int thisCid = a.getCouponId();
    		if (cid == thisCid) {
    			newAnswers.add(a);
    		}
		}
		return new AnswersCollection(newAnswers);
	}
	
	public OneAnswer getAnsAtIdx(int i) {
		return _allAnswers.get(i);
	}
	
	public void removeAnsAtIdx(int i) {
		_allAnswers.remove(i);
	}

	public void addAnswer(OneAnswer ans) {
		_allAnswers.add(ans);
	}
	
	public AnswersCollection getDeepCopy() {
		// the constructor already gets a deep copy of the oc
		return new AnswersCollection(_allAnswers);
	}
	
	public int size() {
		return _allAnswers.size();
	}

	@Override
	public Iterator<OneAnswer> iterator() {
		return _allAnswers.iterator();
	}
	
	private void computeCids() {
		_cids = new MJ_OC_Factory<Integer>().create();
	    	
		for (int i=0; i<_allAnswers.size(); i++) {
			int thisCid = _allAnswers.get(i).getCouponId();
			
			if ( ! _cids.contains(thisCid)) {
				_cids.add(thisCid);
			}
		}
    }
}
