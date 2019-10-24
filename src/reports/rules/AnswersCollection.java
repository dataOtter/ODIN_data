package reports.rules;

import dao.OneAnswer;
import orderedcollection.*;
import java.util.Iterator;

/**
 *
 * @author Maisha Jauernig
 */
public class AnswersCollection implements Iterable<OneAnswer> {
	private IMJ_OC<OneAnswer> _allAnswers;
	private IMJ_OC<Integer> _rids = null;
	private IMJ_OC<Integer> _cids = null;

    public AnswersCollection() {
		_allAnswers = new MJ_OC_Factory<OneAnswer>().create();
	}
    
    private AnswersCollection(IMJ_OC<OneAnswer> answers) {
		_allAnswers = answers.getDeepCopy();
	}
    
    public void addOneAnser(OneAnswer a) {
    	_allAnswers.add(a);
    }
    
    public AnswersCollection getAnsForRuleAndCid(int cid, int rid) {
    	return this.getAnswersByRids(rid).getAnswersByCids(cid);
    }
    
    public OneAnswer getMostRecentAnsToQid(int qid, double timeNow) {
    	double t = 0.0;
    	OneAnswer a;
    	OneAnswer toReturn = null;
    	Iterator<OneAnswer> iter = _allAnswers.iterator();
    	
    	while (t <= timeNow && iter.hasNext()) {
    		a = iter.next();
    		t = a.getRuleFiredTime().getTimeInMillis() / 1000.0;
    		if (a.getQuestionId() == qid) {
    			toReturn = a;
    		}
    	}
    	
    	return toReturn;
    }
    
    /***
     * @return just the answers to questions triggered by whileAt rules, for a specific Cid
     */
    public AnswersCollection getOneRuleTypesAnswersForCid(int cid, RulesCollection rules, String ruleType) {
    	RulesCollection oneRuleTypeRulesCollection = rules.getRulesCollectionByType(ruleType);
        IMJ_OC<Integer> ruleRids = oneRuleTypeRulesCollection.getAllRids();
    	AnswersCollection ruleAns = this.getAnswersByRids(ruleRids);
    	
    	if (_cids == null) {
    		computeCids();
    	}
  
    	if ( ! _cids.contains(cid) ) {
			return new AnswersCollection();
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
		AnswersCollection ans = new AnswersCollection();
		
		for (OneAnswer a: _allAnswers) {
    		if ( rids.contains(a.getRuleId()) ) {
    			ans.addAnswer(a);
    		}
		}
		return ans;
	}
	
	public AnswersCollection getAnswersByRids(int rid) {
		IMJ_OC<Integer> rids = new MJ_OC_Factory<Integer>().create();
		rids.add(rid);
		return getAnswersByRids(rids);
	}
	
	public AnswersCollection getAnswersByCids(IMJ_OC<Integer> cids) {
		AnswersCollection ans = new AnswersCollection();
		
		for (OneAnswer a: _allAnswers) {
    		if ( cids.contains(a.getCouponId()) ) {
    			ans.addAnswer(a);
    		}
		}
		return ans;
	}
	
	public AnswersCollection getAnswersByCids(int cid) {
		IMJ_OC<Integer> cids = new MJ_OC_Factory<Integer>().create();
		cids.add(cid);
		return getAnswersByCids(cids);
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
