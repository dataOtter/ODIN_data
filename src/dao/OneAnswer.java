package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import studysensors.Constants;

/**
 *
 * @author Maisha
 */
public class OneAnswer {
	private final Integer _couponId;
	private final Integer _ruleId;
	private final Integer _questionId;
	private final Integer _choiceId;
    private final String _answerText;
    private final Calendar _ruleFiredDateTime;  //(dateTime) 
    private final int _formatVersion;
    
    public OneAnswer(int formatVersion, String[] answerRow) throws ParseException{
    	_formatVersion = formatVersion;
    	_couponId = Integer.parseInt(answerRow[Constants.ANSWERS_COUPONID_IDX]);
		_ruleId = Integer.parseInt(answerRow[Constants.ANSWERS_RULEID_IDX]);
		_questionId = Integer.parseInt(answerRow[Constants.ANSWERS_QUESTIONID_IDX]);
		_choiceId = Integer.parseInt(answerRow[Constants.ANSWERS_CHOICEID_IDX]);
		_answerText = answerRow[Constants.ANSWERS_ANSWERTEXT_IDX];
    	_ruleFiredDateTime = getRuleFiredTimeFromLine(answerRow);
    }
    
    public Calendar getRuleFiredTime(){
        return _ruleFiredDateTime;
    }
    
    public String getAnswerText(){
        return _answerText;
    }
    
    public int getCouponId(){
        return _couponId;
    }
    
    public int getRuleId(){
        return _ruleId;
    }
    
    private Calendar getRuleFiredTimeFromLine(String[] line) throws ParseException{
        Calendar dateTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.S", Locale.US);
        Date d = sdf.parse(line[Constants.ANSWERS_RULEFIREDTIME_IDX]);
        dateTime.setTime(d);
        return dateTime;
    }
}
