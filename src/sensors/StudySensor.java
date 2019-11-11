package sensors;

import sensors.params.AbsSensorParams;

/**
 * Contains all information found in one row of the studytosensor.csv
 * @author Maisha Jauernig
 */
public class StudySensor {
	private SensorType _type;
	private AbsSensorParams _params;
	private int _studyId;
	
	/**
	 * @param type
	 * @param params
	 * @param studyId
	 */
	public StudySensor(SensorType type, AbsSensorParams params, int studyId) {
		_type = type;
		_params = params;
		_studyId = studyId;
	}
	
	/**
	 * @return the study ID of this study sensor's associated study 
	 */
	public int getStudyId() {
		return _studyId;
	}
	
	/**
	 * @return the ID of this study sensor
	 */
	public int getSensorId() {
		return _type.getSensorId();
	}
	
	/**
	 * @return the parameter of this study sensor as AbsSensorParameters
	 */
	public AbsSensorParams getParams() {
		return _params;
	}
	
	/**
	 * @return the type of this study sensor as SensorType
	 */
	public SensorType getSensorType() {
		return _type;
	}
}
