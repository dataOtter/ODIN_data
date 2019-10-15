package sensors.data;

import java.util.Calendar;

import maps.*;

/**
 *
 * @author Maisha Jauernig
 */
public class ActivityDataPoint extends AbsDataPoint {
	IMJ_Map<String, Integer> _activities;
	
	public ActivityDataPoint(Calendar dateTime, IMJ_Map<String, Integer> activities) {
        super(dateTime);
        _activities = activities;
    }

	public IMJ_Map<String, Integer> getActivities() {
		return _activities;
	}
}