package studysensors.sensors;

public class GpsSensorParameters extends AbsSensorParameters {
	private final double _interval;
	private final double _distance;
	
	public GpsSensorParameters(String line) {
		super (line);
		_interval = Double.parseDouble(_paramNameToVal.get("time"));
		_distance = Double.parseDouble(_paramNameToVal.get("distance"));
	}
	
	@Override
	public double getInterval() {
		return _interval;
	}

	public double getDistance() {
		return _distance;
	}
}
