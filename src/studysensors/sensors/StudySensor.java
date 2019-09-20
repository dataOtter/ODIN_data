package studysensors.sensors;

import dao.SensorType;

public class StudySensor {
	private SensorType _type;
	private AbsSensorParameters _params;
	private int _studyId;
	
	public StudySensor(SensorType type, AbsSensorParameters params, int studyId) {
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
	
	public AbsSensorParameters getParams() {
		return _params;
	}
	
	public SensorType getSensorType() {
		return _type;
	}
}