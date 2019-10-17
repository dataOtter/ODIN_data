package reports.rules.whileAt;

import constants.Constants;
import dao.OneAnswer;
import maps.IMJ_Map;
import maps.MJ_Map_Factory;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.rules.AnswersCollection;
import reports.rules.RulesCollection;

/**
 *
 * @author Maisha Jauernig
 */
public class AllWhileAtRuleData {
    // map of cid to while at answers (answers to questions triggered by whileat rules)
    AnswersCollection _allAnswers;
    RulesCollection _rules;
    
    // answers contains all answers, regardless of cid and rid
    public AllWhileAtRuleData(AnswersCollection answers, RulesCollection rules) {
        _allAnswers = answers;
        _rules = rules;
    }

    /***
     * @return just the answers to questions triggered by whileAt rules, for a specific Cid
     */
    public AnswersCollection getWhileAtAnswersForOneCid(int cid) {
    	return _allAnswers.getOneRuleTypesAnswersForCid(cid, _rules, Constants.RULE_WHILEAT_NOTAT);
    }
}
