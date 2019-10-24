/**
 * 
 */
package filters;

import constants.Constants;
import dao.OneAnswer;
import orderedcollection.IMJ_OC;
import reports.rules.AnswersCollection;

/**
 * @author Maisha Jauernig
 *
 */
public class QuestionFilterParams extends AbsFilterParams {
	private final AnswersCollection _allAns;
	
	public QuestionFilterParams(AnswersCollection allAns) {
		super(Constants.FILTER_QUESTION);
		_allAns = allAns;
	}

	/**
	 * @return the _allAns
	 */
	public AnswersCollection getAllAns() {
		return _allAns;
	}

	@Override
	public boolean testInput(IMJ_OC<AbsFilterInput> inputs) {
		Integer qid = null;
		Integer choiceId = null;
		Double timeNow = null;
		
		for (AbsFilterInput i: inputs) {
			if (i.getType().equals(this.getType())) {
				qid = ((QuestionFilterInput) i).getQid();
				choiceId = ((QuestionFilterInput) i).getChoiceId();
				timeNow = ((QuestionFilterInput) i).getTimeNowSecs();
				break;
			}
		}
		
		OneAnswer mostRecentAnsToQid = _allAns.getMostRecentAnsToQid(qid, timeNow);
		if (mostRecentAnsToQid.getChoiceId() == choiceId) {
			return true;
		}
		return false;
	}

}
