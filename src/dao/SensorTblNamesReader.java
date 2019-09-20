package dao;

import orderedcollection.*;
import studysensors.sensors.*;

/***
 * This file reads the sensor table names
 *  
 * @author Maisha
 *
 */
public class SensorTblNamesReader {
    private IMJ_OC<String> _sensorTblNames;
    private final String _path;
    private final int _formatVersion;
    
    public SensorTblNamesReader(String path, int formatVersion) {
        _path = path;
        _formatVersion = formatVersion;
        _sensorTblNames = null;
    }
    
    public IMJ_OC<String> getSensorTblNames() {
    	if (_sensorTblNames == null) {
    		_sensorTblNames = new MJ_OC_Factory<String>().create();

            Study s = new StudyReader(_path, _formatVersion).getStudy();
            StudySensorsCollection studySensors = new SensorsReader(_path, _formatVersion)
            		.getStudySensorsCollection().getSensorsByStudyId(s.getStudyId());
            
            for (int i = 0 ; i<studySensors.getLength(); i++){
                String tblName = studySensors.getStudySensorAtIdx(i).getSensorType().getSensorTblName();
                _sensorTblNames.add(tblName);
            } 
    	}
    	/*if (Constants.TESTING_GPS_ONLY) {
    		IMJ_OC<String> gps = new MJ_OC_Factory<String>().create();
    		gps.append("sensor_GPS");
    		return gps;
    	}*/
        return _sensorTblNames.getDeepCopy();
    }
}
