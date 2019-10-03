package studysensors.sensors;

import Constants.Constants;

/**
 *
 * @author Maisha Jauernig
 */
public class ProximityBTSensorParameters extends AbsSensorParameters {
	private final double _interval;
	
	public ProximityBTSensorParameters(String line) {
		super (line);
		_interval = Double.parseDouble(_paramNameToVal.get(Constants.STUDYTOSENSOR_BT_INTERVAL_LABEL));
	}
	
	@Override
	public double getInterval() {
		return _interval;
	}
}
