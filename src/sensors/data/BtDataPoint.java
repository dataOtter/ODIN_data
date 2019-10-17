package sensors.data;

import java.util.Calendar;
import constants.*;

/**
 *
 * @author Maisha Jauernig
 */
public class BtDataPoint extends AbsDataPoint {
	private final String _deviceName;
	private final int _rawRSSI;
	private final double _smoothedRSSI;
    private final static int _sensorId = 3;
	
	public BtDataPoint(Calendar dateTime, String name, int raw, double smoothed) {
        super(dateTime, _sensorId);
        _deviceName = name;
    	_rawRSSI = raw;
    	_smoothedRSSI = smoothed;
    }

	public String getDeviceName() {
		return _deviceName;
	}

	public int getRawRSSI() {
		return _rawRSSI;
	}

	public double getSmoothedRSSI() {
		return _smoothedRSSI;
	}
}
