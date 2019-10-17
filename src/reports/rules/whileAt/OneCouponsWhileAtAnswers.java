package reports.rules.whileAt;

import dao.OneAnswer;
import maps.IMJ_Map;
import maps.MJ_Map_Factory;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.rules.AnswersCollection;

/**
 *
 * @author Maisha Jauernig
 */
public class OneCouponsWhileAtAnswers {
    private final IMJ_Map<Integer, AnswersCollection> _ruleIdToAnswersColl;
    private final int _couponId;
    
    public OneCouponsWhileAtAnswers(AllWhileAtRuleData cidsToWhileAtAnswers, int cid) {
        _couponId = cid;
        
        AnswersCollection whileAtAnswers = cidsToWhileAtAnswers.getWhileAtAnswersForOneCid(cid);
        IMJ_OC<Integer> whileAtRids = whileAtAnswers.getRids();
        _ruleIdToAnswersColl = new MJ_Map_Factory<Integer, AnswersCollection>().create();
        
        for (int rid: whileAtRids) {
        	AnswersCollection ans = whileAtAnswers.getAnswersByRids(rid);
        	_ruleIdToAnswersColl.put(rid, ans);
        }
    }
    
    void addOneAnswerEntry(Integer rid, OneAnswer a){
    	AnswersCollection answers;
        if (_ruleIdToAnswersColl.containsKey(rid)){
        	answers = _ruleIdToAnswersColl.get(rid);
        	answers.addAnswer(a);
        }
        else{
        	IMJ_OC<OneAnswer> ans = new MJ_OC_Factory<OneAnswer>().create();
        	ans.add(a);
        	answers = new AnswersCollection(ans);
            _ruleIdToAnswersColl.put(rid, answers);
        }
    }
    
    public AnswersCollection getOneRulesAnswersCollection(int rid){
    	if ( ! _ruleIdToAnswersColl.containsKey(rid)) {
    		return new AnswersCollection(new MJ_OC_Factory<OneAnswer>().create());
    	}
        return _ruleIdToAnswersColl.get(rid);
    }
    
    public int getCouponId(){
        return _couponId;
    }
}
