package sensors.params;

import constants.Constants;

/**
 *
 * @author Maisha Jauernig
 */
public class BeaconSensorParams extends AbsSensorParams {
	private final double _interval;
	private final int _major;
	private Integer _minor;
	private final String _password;
	private final String _standard;
	private final String _uuid;
	
	public BeaconSensorParams(String line) {
		super (line);
		_interval = Double.parseDouble(_paramNameToVal.get(Constants.STUDYTOSENSOR_INTERVAL));
		_major = Integer.parseInt(_paramNameToVal.get(Constants.STUDYTOSENSOR_MAJOR));
		try {
		_minor = Integer.parseInt(_paramNameToVal.get(Constants.STUDYTOSENSOR_MINOR));
		} 
		catch(Exception e) {
			_minor = null;
		}
		_password = _paramNameToVal.get(Constants.STUDYTOSENSOR_PASSWORD);
		_standard = _paramNameToVal.get(Constants.STUDYTOSENSOR_STANDARD);
		_uuid = _paramNameToVal.get(Constants.STUDYTOSENSOR_UUID);
	}
	
	public int getMajor() {
		return _major;
	}

	public int getMinor() {
		return _minor;
	}

	public String getPassword() {
		return _password;
	}

	public String getStandard() {
		return _standard;
	}

	public String getUuid() {
		return _uuid;
	}

	@Override
	public double getTimeInterval() {
		return _interval;
	}
}
