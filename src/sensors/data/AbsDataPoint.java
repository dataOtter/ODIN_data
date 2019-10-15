package sensors.data;

import java.util.Calendar;

public abstract class AbsDataPoint {
	private Calendar _dateTime;
	
	protected AbsDataPoint(Calendar dateTime) {
        _dateTime = dateTime;
	}
	
    public Calendar getDateTime(){
        return _dateTime;
    }
}
