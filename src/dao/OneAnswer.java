package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import constants.Constants;
import orderedcollection.IMJ_OC;

/**
 * Contains all information found in one row of the answers.csv
 * @author Maisha Jauernig
 */
public class OneAnswer {
	private final Integer _couponId;
	private final Integer _ruleId;
	private final Integer _questionId;
	private final Integer _choiceId;
    private final String _answerText;
    private final Calendar _ruleFiredDateTime;
    private final int _formatVersion;
    
    /**
     * @param formatVersion
     * @param answerRow - one row of the answers.csv as a String[]
     * @throws ParseException
     */
    public OneAnswer(int formatVersion, IMJ_OC<String> answerRow) throws ParseException {
    	_formatVersion = formatVersion;
    	_couponId = Integer.parseInt(answerRow.get(Constants.ANSWERS_COUPONID_IDX));
		_ruleId = Integer.parseInt(answerRow.get(Constants.ANSWERS_RULEID_IDX));
		_questionId = Integer.parseInt(answerRow.get(Constants.ANSWERS_QUESTIONID_IDX));
		_choiceId = Integer.parseInt(answerRow.get(Constants.ANSWERS_CHOICEID_IDX));
		_answerText = answerRow.get(Constants.ANSWERS_ANSWERTEXT_IDX);
    	_ruleFiredDateTime = getRuleFiredTimeFromLine(answerRow);
    }
    
    /**
     * @return the time the rule associated with this answer fired as a Calendar object
     */
    public Calendar getRuleFiredTime(){
        return _ruleFiredDateTime;
    }
    
    /**
     * @return the text provided as an answer, if any, as a String
     */
    public String getAnswerText(){
        return _answerText;
    }
    
    /**
     * @return the coupon ID associated with this answer as an int
     */
    public int getCouponId(){
        return _couponId;
    }
    
    /**
     * @return the rule ID associated with this answer as an int
     */
    public int getRuleId(){
        return _ruleId;
    }
    
    /**
     * @return the question ID associated with this answer as an int
     */
    public int getQuestionId(){
        return _questionId;
    }
    
    /**
     * @return the choice ID associated with this answer as an int
     */
    public int getChoiceId(){
        return _choiceId;
    }
    
    private Calendar getRuleFiredTimeFromLine(IMJ_OC<String> line) throws ParseException{
        Calendar dateTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.S", Locale.US);
        if (line.get(Constants.ANSWERS_RULEFIREDTIME_IDX).length() < 5) {
        	System.out.println("wtf");
        }
        Date d = sdf.parse(line.get(Constants.ANSWERS_RULEFIREDTIME_IDX));
        dateTime.setTime(d);
        return dateTime;
    }
}
