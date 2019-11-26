package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import constants.Constants;
import maps.IMJ_Map;

public class OneCoupon {
	private final int _id;
	private final String _number;
    private final Calendar _lastRegistrationTime;
    private final IMJ_Map<String, Calendar> _lastUploads;
    private final Calendar _lastQuestionCallTime;
    private final String _consentStatus;

    public OneCoupon(String couponRow, IMJ_Map<Integer, IMJ_Map<String, Calendar>> uploads) throws ParseException {
    	String[] line = couponRow.split(",");
        _id = Integer.parseInt(line[Constants.COUPON_COUPONID_IDX]);
        _number = line[Constants.COUPON_COUPONNUMBER_IDX];
        _lastRegistrationTime = getLastRegTime(line[Constants.COUPON_LASTREGISTRATION_IDX]);
        _lastUploads = uploads.get(_id);
		_lastRegistrationTime = getCalendar(line[Constants.COUPON_LASTREGISTRATION_IDX]);
        _lastQuestionCallTime = getCalendar(line[Constants.COUPON_LASTQUESTIONCALL_IDX]);
        _consentStatus = line[Constants.COUPON_CONSENTSTATUS_IDX];
    }
    
    private Calendar getLastRegTime(String date) throws ParseException {
        Calendar dateTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.S", Locale.US);
        Date d = sdf.parse(date);
        dateTime.setTime(d);
        return dateTime;
    }

	public int getId() {
		return _id;
	}

	public String getNumber() {
		return _number;
	}

	public Calendar getLastRegistrationTime() {
		return _lastRegistrationTime;
	}

	public IMJ_Map<String, Calendar> getLastUploads() {
		return _lastUploads;
	}

	public Calendar getLastUploadForService(String service) {
		return _lastUploads.get(service);
	}

	/**
	 * @return the _consentStatus
	 */
	public String getConsentStatus() {
		return _consentStatus;
	}

	/**
	 * @return the _lastQuestionCallTime
	 */
	public Calendar getLastQuestionCallTime() {
		return _lastQuestionCallTime;
	}
    
    private Calendar getCalendar(String date) throws ParseException {
    	if (date.equals("null")) {
    		return null;
    	}
        Calendar dateTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.S", Locale.US);
        Date d = sdf.parse(date);
        dateTime.setTime(d);
        return dateTime;
    }
}
