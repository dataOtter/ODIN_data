package sensors;

import sensors.params.AbsSensorParams;

/**
 *
 * @author Maisha Jauernig
 */
public class StudySensor {
	private SensorType _type;
	private AbsSensorParams _params;
	private int _studyId;
	
	public StudySensor(SensorType type, AbsSensorParams params, int studyId) {
		_type = type;
		_params = params;
		_studyId = studyId;
	}
	
	public int getStudyId() {
		return _studyId;
	}
	
	public int getSensorId() {
		return _type.getSensorId();
	}
	
	public AbsSensorParams getParams() {
		return _params;
	}
	
	public SensorType getSensorType() {
		return _type;
	}
}
