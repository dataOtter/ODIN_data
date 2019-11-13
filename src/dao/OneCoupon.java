package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import constants.Constants;

public class OneCoupon {
	private final int _id;
	private final String _number;
    private final Calendar _lastRegistrationTime;

    public OneCoupon(String couponRow) throws ParseException {
    	String[] line = couponRow.split(",");
        _id = Integer.parseInt(line[Constants.COUPON_COUPONID_IDX]);
        _number = line[Constants.COUPON_COUPONNUMBER_IDX];
        _lastRegistrationTime = getLastRegTime(line[Constants.COUPON_LASTREGISTRATION_IDX]);
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
}
