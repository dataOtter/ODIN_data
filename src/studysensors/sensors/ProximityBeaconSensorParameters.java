package studysensors.sensors;

import Constants.Constants;

public class ProximityBeaconSensorParameters extends AbsSensorParameters {
	private final double _interval;
	
	public ProximityBeaconSensorParameters(String line) {
		super (line);
		_interval = Double.parseDouble(_paramNameToVal.get(Constants.STUDYTOSENSOR_BEACON_INTERVAL_LABEL));
	}
	
	@Override
	public double getInterval() {
		return _interval;
	}
}
