package sensors.params;

import constants.Constants;

public class IntOnlySensorParams extends AbsSensorParams {
	private final double _interval;
	
	IntOnlySensorParams(String line) {
		super (line);
		_interval = Double.parseDouble(_paramNameToVal.get(Constants.STUDYTOSENSOR_INTERVAL));
	}
	
	@Override
	public double getTimeInterval() {
		return _interval;
	}
}
