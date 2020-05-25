package sensors.data;

import java.util.Calendar;

import maps.*;

/**
 *
 * @author Maisha Jauernig
 */
public class ActivityDataPoint extends AbsDataPoint {
	IMJ_Map<String, Integer> _activities;
    private final static int _sensorId = 13;
	
	public ActivityDataPoint(Calendar dateTime, IMJ_Map<String, Integer> activities) {
        super(dateTime, _sensorId);
        _activities = activities;
    }

	public IMJ_Map<String, Integer> getActivities() {
		return _activities;
	}
}