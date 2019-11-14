package sensors.data;
/**
*
* @author Maisha Jauernig
*/
public class BtDeviceData {
	private final String _deviceName;
	private final int _rawRSSI;
	private final double _smoothedRSSI;
    
	public BtDeviceData(String name, int raw, double smoothed) {
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
