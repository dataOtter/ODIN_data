package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

import com.google.gson.Gson;

import Assert.Assertion;
import maps.*;
import orderedcollection.*;
import studysensors.Constants;
import studysensors.sensors.*;

public class SensorsReader {
    private final String _path;
    private final int _formatVersion;
    
    public SensorsReader(String path, int formatVersion) throws FileNotFoundException, ParseException {
        _path = path;
        _formatVersion = formatVersion;
    }
    
    public StudySensorsCollection getStudySensorsCollection(){
    	IMJ_Map<Integer, SensorType> sensorTypes = getSensorTypesCollection();
    	
        File f = new File(_path + "\\" + Constants.STUDYTOSENSOR_CSV);
        Scanner sc = null;
        try{
            sc = new Scanner(f);
        }
        catch(Exception e){
            Assertion.test(false, "Scanner failed to find file");
        }
        Assertion.test(sc.nextLine().split(",").length == Constants.STUDYTOSENSOR_NUM_COLS, 
                "No file or file does not have the correct number of columns");

        IMJ_OC<StudySensor> sensors = new MJ_OC_Factory<StudySensor>().create();
        
        while (sc.hasNextLine()){
        	String line = sc.nextLine();
            sensors.append( getStudySensorFromLine(line, sensorTypes) );
        }
        return new StudySensorsCollection(sensors);
    }
    
    private StudySensor getStudySensorFromLine(String line, IMJ_Map<Integer, SensorType> sensorTypes) {
    	String json = extractJsonSection(line);
        //System.out.println(json);
        //String nonJson = extractNonJsonSections(line);
        JsonSensorparams p = new Gson().fromJson(json, JsonSensorparams.class);
        double si = p.getTimeInterval();
        double dist = p.getDistance();
        SensorParameters param = new SensorParameters(si, dist);
        
        String[] arr = line.split(",");
    	int studyId = Integer.parseInt(arr[Constants.STUDYTOSENSOR_STUDYID_IDX]);
        int sensorId = Integer.parseInt(arr[Constants.STUDYTOSENSOR_SENSORID_IDX]);
        
        SensorType type = sensorTypes.getValueOfKey(sensorId);
        
        return new StudySensor(type, param, studyId);
    }
    
    private IMJ_Map<Integer, SensorType> getSensorTypesCollection(){
    	IMJ_Map<Integer, SensorType> sensors = new MJ_Map_Factory<Integer, SensorType>().create();
        File f = new File(_path + "\\" + Constants.SENSORTYPES_CSV);
        Scanner sc = null;
        try{
            sc = new Scanner(f);
        }
        catch(Exception e){
            Assertion.test(false, "Scanner failed to find file");
        }
        Assertion.test(sc.nextLine().split(",").length == Constants.SENSORTYPES_NUM_COLS, 
                "No file or file does not have the correct number of columns");
        
        while (sc.hasNextLine()){
        	String[] line = sc.nextLine().split(",");
            int sid = Integer.parseInt(line[Constants.SENSORTYPES_SENSORID_IDX]);
            sensors.add( sid, new SensorType(line) );
        }
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
