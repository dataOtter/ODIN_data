package sensors.data;

import java.util.Calendar;
import orderedcollection.*;

/**
 *
 * @author Maisha Jauernig
 */
public class BtDataPoint extends AbsDataPoint {
	private final IMJ_OC<BtDeviceData> _data;
    private final static int _sensorId = 3;
    
	public BtDataPoint(Calendar dateTime, IMJ_OC<BtDeviceData> data) {
        super(dateTime, _sensorId);
        _data = data;
    }

	public IMJ_OC<BtDeviceData> getData() {
		return _data;
	}
}
