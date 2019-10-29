/**
 * 
 */
package filters;

import constants.Constants;
import reports.rules.AnswersCollection;

/**
 * @author Maisha Jauernig
 *
 */
public class QuestionFilterInput extends AbsFilterInput {
	private final AnswersCollection _allAns;
	
	/* the most recent response to question A was B.
		A = dropdown (question tag)  - field "alias" in "questions.csv", get "questionid"
	B = dropdown (choices for the selected question) - in "answers.csv" get most recent entry for "questionid", 
	AnswersCollection contains OneAnswers in chronological order, so can walk backward through it
	so need a time too and walk backward*/ 
	
	public QuestionFilterInput(AnswersCollection allAns, double timeNow) {
		super(Constants.FILTER_QUESTION, timeNow);
		_allAns = allAns;
	}

	/**
	 * @return the _allAns
	 */
	public AnswersCollection getAllAns() {
		return _allAns;
	}
}
