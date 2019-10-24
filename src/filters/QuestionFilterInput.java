/**
 * 
 */
package filters;

import constants.Constants;

/**
 * @author Maisha Jauernig
 *
 */
public class QuestionFilterInput extends AbsFilterInput {
	private final int _qid;
	private final int _choiceId;
	
	/* the most recent response to question A was B.
		A = dropdown (question tag)  - field "alias" in "questions.csv", get "questionid"
	B = dropdown (choices for the selected question) - in "answers.csv" get most recent entry for "questionid", 
	AnswersCollection contains OneAnswers in chronological order, so can walk backward through it
	so need a time too and walk backward*/ 
	
	public QuestionFilterInput(int qid, int choiceId, double timeNow) {
		super(Constants.FILTER_QUESTION, timeNow);
		_qid = qid;
		_choiceId = choiceId;
	}

	/**
	 * @return the _qid
	 */
	public int getQid() {
		return _qid;
	}

	/**
	 * @return the _choiceId
	 */
	public int getChoiceId() {
		return _choiceId;
	}
}
