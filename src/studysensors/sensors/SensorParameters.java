package studysensors.sensors;

public class SensorParameters {
	private double _interval;
	private double _distance;
	
	public SensorParameters(double inter, double dist) {
		_interval = inter;
		_distance = dist;
	}
	
	public double getInterval() {
		return _interval;
	}

	public double getDistance() {
		return _distance;
	}
}
