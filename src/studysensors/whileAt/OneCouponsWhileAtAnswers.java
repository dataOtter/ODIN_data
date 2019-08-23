package studysensors.whileAt;

import dao.OneAnswer;
import maps.*;
import orderedcollection.*;
import studysensors.rules.*;

/**
 *
 * @author Maisha
 */
public class OneCouponsWhileAtAnswers {
    private final IMJ_Map<Integer, AnswersCollection> _ruleIdToAnswersMap;
    private final int _couponId;
    
    public OneCouponsWhileAtAnswers(AllWhileAtRuleData cidsToWhileAtAnswers, int cid) {
        _couponId = cid;
        
        AnswersCollection whileAtAnswers = cidsToWhileAtAnswers.getWhileAtAnswersForOneCid(cid);
        IMJ_OC<Integer> whileAtRids = whileAtAnswers.getRids();
        _ruleIdToAnswersMap = new MJ_Map_Factory<Integer, AnswersCollection>().create();
        
        for (int i = 0; i<whileAtRids.length(); i++) {
        	int rid = whileAtRids.getItem(i);
        	AnswersCollection ans = whileAtAnswers.getAnswersByRids(rid);
        	_ruleIdToAnswersMap.add(rid, ans);
        }
    }
    
    void addOneAnswerEntry(Integer rid, OneAnswer a){
    	AnswersCollection answers;
        if (_ruleIdToAnswersMap.contains(rid)){
        	answers = _ruleIdToAnswersMap.getValueOfKey(rid);
        	answers.addAnswer(a);
        }
        else{
        	IMJ_OC<OneAnswer> ans = new MJ_OC_Factory<OneAnswer>().create();
        	ans.append(a);
        	answers = new AnswersCollection(ans);
            _ruleIdToAnswersMap.add(rid, answers);
        }
    }
    
    public AnswersCollection getOneRulesAnswersCollection(int rid){
    	if ( ! _ruleIdToAnswersMap.contains(rid)) {
    		return new AnswersCollection(new MJ_OC_Factory<OneAnswer>().create());
    	}
        return _ruleIdToAnswersMap.getValueOfKey(rid);
    }
    
    public int getCouponId(){
        return _couponId;
    }
}
