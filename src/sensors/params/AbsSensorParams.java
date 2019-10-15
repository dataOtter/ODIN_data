package sensors.params;

import constants.Constants;
import maps.*;

/**
 *
 * @author Maisha Jauernig
 */
public abstract class AbsSensorParams {
	IMJ_Map<String, String> _paramNameToVal;
	
	AbsSensorParams (String line){
		_paramNameToVal = new MJ_Map_Factory<String, String>().create();
		String json = extractParameters(line);
		String[] words = json.split(",");
		for (String w:words) {
			String[] kv = w.split(":");
			_paramNameToVal.put(kv[0],  kv[1]);
		}
	}
	
	public static AbsSensorParams parseFromString(String line) {
		AbsSensorParams answer;
		String[] row = line.split(",");
		int sid = Integer.parseInt(row[Constants.STUDYTOSENSOR_SENSORID_IDX]);
		switch (sid) {
			case Constants.SENSORID_GPS: answer = new GpsSensorParams(line);
				break;
			case Constants.SENSORID_BT: answer = new BTSensorParams(line);
				break;
			case Constants.SENSORID_BEACON: answer = new BeaconSensorParams(line);
				break;
			case Constants.SENSORID_ACTIVITY: answer = new ActivitySensorParams(line);
			break;
			default: answer = null;
				break;
		}
		return answer;
	}
	
	public abstract double getTimeInterval();
	
	private String extractParameters(String s) {
        int startIndex = s.indexOf("{") + 1;
        int endIndex = s.indexOf("}");
        String subString = s.substring(startIndex, endIndex);
        subString = subString.replaceAll("\"","");
        return subString;
    }
}
