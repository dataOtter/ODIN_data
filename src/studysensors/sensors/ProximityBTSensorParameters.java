package studysensors.sensors;

public class ProximityBTSensorParameters extends AbsSensorParameters {
	private final double _interval;
	
	public ProximityBTSensorParameters(String line) {
		super (line);
		_interval = Double.parseDouble(_paramNameToVal.get("interval"));
	}
	
	@Override
	public double getInterval() {
		return _interval;
	}
}
