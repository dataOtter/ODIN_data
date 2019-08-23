package dao;

import java.io.FileNotFoundException;
import java.text.ParseException;

import orderedcollection.*;
import studysensors.sensors.*;

public class SensorTblNamesReader {
    private IMJ_OC<String> _sensorTblNames;
    private final String _path;
    private final int _formatVersion;
    
    public SensorTblNamesReader(String path, int formatVersion) throws FileNotFoundException, ParseException{
        _path = path;
        _formatVersion = formatVersion;
        _sensorTblNames = null;
    }
    
    public IMJ_OC<String> getSensorTblNames() throws FileNotFoundException, ParseException {
    	if (_sensorTblNames == null) {
    		_sensorTblNames = new MJ_OC_Factory<String>().create();
            String tblName;

            Study s = new StudyReader(_path, _formatVersion).getStudy();
            StudySensorsCollection studySensors = new SensorsReader(_path, _formatVersion)
            		.getStudySensorsCollection().getSensorsByStudyId(s.getStudyId());
            
            for (int i = 0 ; i<studySensors.getLength(); i++){
                tblName = studySensors.getStudySensorAtIdx(i).getSensorType().getSensorTblName();
                _sensorTblNames.append(tblName);
            } 
    	}
        return _sensorTblNames.getDeepCopy();
    }
}
