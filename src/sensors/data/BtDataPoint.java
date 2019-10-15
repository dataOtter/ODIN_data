package sensors.data;

import java.util.Calendar;

/**
 *
 * @author Maisha Jauernig
 */
public class BtDataPoint extends AbsDataPoint {
	private final String _deviceName;
	private final int _rawRSSI;
	private final double _smoothedRSSI;
	
	public BtDataPoint(Calendar dateTime, String name, int raw, double smoothed) {
        super(dateTime);
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
