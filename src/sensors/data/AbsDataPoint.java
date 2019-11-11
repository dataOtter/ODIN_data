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
	
	/**
     * @return the date and time associated with the recording of this AbsDataPoint as a Calendar object
     */
    public Calendar getDateTime() {
        return _dateTime;
    }
    
	/**
	 * @return the type of this AbsDataPoint as a String
	 */
	public String getDataType() {
		return _dataType;
	}
	
	@Override
	public String toString() {
		return "date in millis: " + _dateTime.getTime() + " type: " + _dataType;
	}
}
