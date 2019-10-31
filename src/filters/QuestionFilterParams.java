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
	private int _qid;
	private int _chid;
	
	public QuestionFilterParams(String params) {
		super(Constants.FILTER_QUESTION);

		String[] p = params.split(",");
		for (String s: p) {
			int id = Integer.parseInt(s.substring(s.indexOf(':')+1));
			if (s.contains(Constants.FILTER_QUESTION_QID)) {
				_qid = id;
			}
			else if (s.contains(Constants.FILTER_QUESTION_CHID)) {
				_chid = id;
			}
		}
	}

	@Override
	public boolean testInput(IMJ_OC<AbsFilterInput> inputs) {
		AnswersCollection allAns = null;
		Double timeNow = null;
		
		for (AbsFilterInput i: inputs) {
			if (i.getType().equals(this.getType())) {
				timeNow = i.getTimeNowSecs();
				allAns = ((QuestionFilterInput) i).getAllAns();
				break;
			}
		}
		
		OneAnswer mostRecentAnsToQid = allAns.getMostRecentAnsToQid(_qid, timeNow);
		if (mostRecentAnsToQid != null && mostRecentAnsToQid.getChoiceId() == _chid) {
			return true;
		}
		return false;
	}
}
