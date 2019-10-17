package sensors.data;

import java.util.Calendar;

public class BeaconDataPoint extends AbsDataPoint {
    private final static int _sensorId = 10;
	
	public BeaconDataPoint(Calendar dateTime) {
        super(dateTime, _sensorId);
    }
}
