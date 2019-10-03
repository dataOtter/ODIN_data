package studysensors.sensors;

import Constants.Constants;
import maps.*;

/**
 *
 * @author Maisha Jauernig
 */
public abstract class AbsSensorParameters {
	IMJ_Map<String, String> _paramNameToVal;
	
	AbsSensorParameters (String line){
		_paramNameToVal = new MJ_Map_Factory<String, String>().create();
		String json = extractParameters(line);
		String[] words = json.split(",");
		for (String w:words) {
			String[] kv = w.split(":");
			_paramNameToVal.put(kv[0],  kv[1]);
		}
	}
	
	public abstract double getInterval();
	
	public static AbsSensorParameters parseFromString(String line) {
		AbsSensorParameters answer;
		String[] row = line.split(",");
		int sid = Integer.parseInt(row[Constants.STUDYTOSENSOR_SENSORID_IDX]);
		switch (sid) {
			case 12: answer = new GpsSensorParameters(line);
				break;
			case 3: answer = new ProximityBTSensorParameters(line);
				break;
			case 10: answer = new ProximityBeaconSensorParameters(line);
				break;
			default: answer = null;
				break;
		}
		return answer;
	}
	
	private String extractParameters(String s) {
        int startIndex = s.indexOf("{") + 1;
        int endIndex = s.indexOf("}");
        String subString = s.substring(startIndex, endIndex);
        subString = subString.replaceAll("\"","");
        return subString;
    }
}
