package studysensors.whileAt;

import dao.OneAnswer;
import maps.*;
import orderedcollection.*;
import studysensors.Constants;
import studysensors.rules.*;

/**
 *
 * @author Maisha
 */
public class AllWhileAtRuleData {
    // map of cid to answers to questions triggered by whileat rules
    private final IMJ_Map<Integer, AnswersCollection> _cidToAnswersCollection;
    // just answers to questions triggered by whileat rules
    private final AnswersCollection _whileAtAnswersCollection;
    
    // answers contains all answers, regardless of cid and rid
    public AllWhileAtRuleData(AnswersCollection answers, RulesCollection rules) {
    	RulesCollection whileAtRulesCollection = rules.getRulesCollectionByType(Constants.RULE_WHILEAT_NAME);
        IMJ_OC<Integer> whileAtRids = whileAtRulesCollection.getAllRids();
        _whileAtAnswersCollection = answers.getAnswersByRids(whileAtRids);

        _cidToAnswersCollection = new MJ_Map_Factory<Integer, AnswersCollection>().create();
        buildCidToWhileAtAnswersMaps();
    }

    /***
     * Returns just the answers to questions triggered by whileAt rules, for a specific Cid
     * @return
     */
    public AnswersCollection getWhileAtAnswersForOneCid(int cid) {
    	if ( ! _cidToAnswersCollection.contains(cid) ) {
			return new AnswersCollection(new MJ_OC_Factory<OneAnswer>().create());
		}
		else {
			return _cidToAnswersCollection.getValueOfKey(cid);
		}
    }
    
    private void buildCidToWhileAtAnswersMaps() {
    	IMJ_OC<OneAnswer> ansOc = _whileAtAnswersCollection.getAnswers();
    	
    	for (int i = 0; i<ansOc.length(); i++) {
    		OneAnswer ans = ansOc.getItem(i);
    		int cid = ans.getCouponId();
    		
    		if ( ! _cidToAnswersCollection.contains(cid) ) {
    			IMJ_OC<OneAnswer> newAnsOc = new MJ_OC_Factory<OneAnswer>().create();
    			AnswersCollection newAnswersCollection = new AnswersCollection(newAnsOc);
    			newAnswersCollection.addAnswer(ans);
    			_cidToAnswersCollection.add(cid,  newAnswersCollection);
    		}
    		else {
    			AnswersCollection existing = _cidToAnswersCollection.getValueOfKey(cid);
    			existing.addAnswer(ans);
    		}
    	}
    }
}
