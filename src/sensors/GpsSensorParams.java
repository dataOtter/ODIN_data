package sensors;

import constants.Constants;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsSensorParams extends AbsSensorParams {
	private final double _timeInt;
	private final double _distance;
	
	public GpsSensorParams(String line) {
		super (line);
		_timeInt = Double.parseDouble(_paramNameToVal.get(Constants.STUDYTOSENSOR_TIME));
		_distance = Double.parseDouble(_paramNameToVal.get(Constants.STUDYTOSENSOR_DISTANCE));
	}

	@Override
	public double getTimeInterval() {
		return _timeInt;
	}

	public double getDistance() {
		return _distance;
	}
}
