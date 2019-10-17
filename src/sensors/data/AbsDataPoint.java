package sensors.data;

import java.util.Calendar;

import constants.ConstTags;

public abstract class AbsDataPoint {
	private Calendar _dateTime;
	private String _dataType;
	
	protected AbsDataPoint(Calendar dateTime, int _sensorId) {
        _dateTime = dateTime;
        _dataType = ConstTags.SENSORID_TO_TYPE.get(_sensorId);
	}
	
    public Calendar getDateTime() {
        return _dateTime;
    }
    
	public String getDataType() {
		return _dataType;
	}
}
