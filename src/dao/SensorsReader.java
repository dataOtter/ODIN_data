package dao;

import java.util.Scanner;

import constants.Constants;
import maps.*;
import orderedcollection.*;
import sensors.*;

/**
 *
 * @author Maisha Jauernig
 */
public class SensorsReader {
    private final String _path;
    private final int _formatVersion;
    
    public SensorsReader(String path, int formatVersion) {
        _path = path;
        _formatVersion = formatVersion;
    }
    
    public StudySensorsCollection getStudySensorsCollection() {
		Scanner sc = new ScannerHelper(_path, Constants.STUDYTOSENSOR_CSV, 
				Constants.STUDYTOSENSOR_NUM_COLS).getScanner();

    	IMJ_Map<Integer, SensorType> sensorTypes = getSensorTypesCollection();
        IMJ_OC<StudySensor> sensors = new MJ_OC_Factory<StudySensor>().create();
        
        while (sc.hasNextLine()){
        	String line = sc.nextLine();
            sensors.add( getStudySensorFromLine(line, sensorTypes) );
        }
        sc.close();
        return new StudySensorsCollection(sensors);
    }
    
    private StudySensor getStudySensorFromLine(String line, IMJ_Map<Integer, SensorType> sensorTypes) {
        AbsSensorParams param = AbsSensorParams.parseFromString(line);
        
        String[] arr = line.split(",");
    	int studyId = Integer.parseInt(arr[Constants.STUDYTOSENSOR_STUDYID_IDX]);
        int sensorId = Integer.parseInt(arr[Constants.STUDYTOSENSOR_SENSORID_IDX]);
        
        SensorType type = sensorTypes.get(sensorId);
        
        return new StudySensor(type, param, studyId);
    }
    
    private IMJ_Map<Integer, SensorType> getSensorTypesCollection() {
		Scanner sc = new ScannerHelper(_path, Constants.SENSORTYPES_CSV, 
				Constants.SENSORTYPES_NUM_COLS).getScanner();

    	IMJ_Map<Integer, SensorType> sensors = new MJ_Map_Factory<Integer, SensorType>().create();
    	
        while (sc.hasNextLine()){
        	String[] line = sc.nextLine().split(",");
            int sid = Integer.parseInt(line[Constants.SENSORTYPES_SENSORID_IDX]);
            sensors.put( sid, new SensorType(line) );
        }
        sc.close();
        return sensors;
    }

    private String extractJsonSection(String s) {
        int startIndex = s.indexOf("{");
        int endIndex = s.indexOf("}");
        String subString = s.substring(startIndex, endIndex+1);
        subString = subString.replaceAll("\"\"","\"");
        return subString;
    }
    
    private String extractNonJsonSections(String s) {
        int startIndex = s.indexOf("{");
        int endIndex = s.indexOf("}");
        String subString = s.substring(0,startIndex-1) +
                s.substring(endIndex+3);
        return subString;
    }
}
