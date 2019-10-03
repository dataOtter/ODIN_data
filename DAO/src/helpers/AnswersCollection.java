package helpers;

import dao.OneAnswer;
import orderedcollection.*;

/**
 *
 * @author Maisha Jauernig
 */
public class AnswersCollection {
	private IMJ_OC<OneAnswer> _allAnswers;

    public AnswersCollection(IMJ_OC<OneAnswer> answers) {
		_allAnswers = answers.getDeepCopy();
	}
    
    public IMJ_OC<Integer> getRids() {
    	IMJ_OC<Integer> rids = new MJ_OC_Factory<Integer>().create();
    	
		for (int i=0; i<_allAnswers.size(); i++) {
			int thisRid = _allAnswers.get(i).getRuleId();
			
			if ( ! rids.contains(thisRid)) {
				rids.add(thisRid);
			}
		}
		return rids;
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
	
	public IMJ_OC<OneAnswer> getAnswers(){
		return _allAnswers;
	}

	public void addAnswer(OneAnswer ans) {
		_allAnswers.add(ans);
	}
}
