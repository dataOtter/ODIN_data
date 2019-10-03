package studysensors.sensors;

import Constants.Constants;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsSensorParameters extends AbsSensorParameters {
	private final double _interval;
	private final double _distance;
	
	public GpsSensorParameters(String line) {
		super (line);
		_interval = Double.parseDouble(_paramNameToVal.get(Constants.STUDYTOSENSOR_GPS_TIME_LABEL));
		_distance = Double.parseDouble(_paramNameToVal.get(Constants.STUDYTOSENSOR_GPS_DISTANCE_LABEL));
	}
	
	@Override
	public double getInterval() {
		return _interval;
	}

	public double getDistance() {
		return _distance;
	}
}
