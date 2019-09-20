package studysensors.sensors;

public class ProximityBeaconSensorParameters extends AbsSensorParameters {
	private final double _interval;
	
	public ProximityBeaconSensorParameters(String line) {
		super (line);
		_interval = Double.parseDouble(_paramNameToVal.get("interval"));
	}
	
	@Override
	public double getInterval() {
		return _interval;
	}
}
