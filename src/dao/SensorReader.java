package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import constants.Constants;
import maps.*;
import orderedcollection.*;
import sensors.data.*;
/**
 *
 * @author Maisha Jauernig
 */
public class SensorReader {
	private final String _path;
	protected final int _formatVersion;
	
	public SensorReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
	}
	
	public void addAllSensorDataToDataColl(String sensorTblName, SensorDataCollection coll) throws ParseException {
        Scanner sc = new ScannerHelper(_path, sensorTblName + ".csv", Constants.SENSOR_NUM_COLS).getScanner();
        while ( sc.hasNextLine() ){
        	String[] line = sc.nextLine().split(",");
            int cid = Integer.parseInt(line[Constants.SENSOR_COUPONID_IDX]);
            AbsDataPoint dp = getDataPoint(line);
            
            if (dp != null) {
	            if ( coll.hasCouponEntry(cid) ) {
	            	coll.addDataPointToCouponData(cid, dp);
	            }
	            else{
	            	SensorDataOfOneType couponData = new SensorDataOfOneType(dp);
	                coll.addCouponAndItsData(cid, couponData);
	            }
            }
        }
        sc.close();
	}
	
	public AbsDataPoint getDataPoint(String[] line) throws ParseException {
		AbsDataPoint toReturn = null;
		
		String firstDataMember = line[Constants.SENSOR_SENSORID_IDX+1];
		firstDataMember = firstDataMember.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\\]", "").replaceAll("\\}", "").replaceAll("\"", "");
		firstDataMember = firstDataMember.split(":")[0];
		
		if ( firstDataMember.equals(Constants.SENSOR_GPS_FIRST_DATA_NAME) ) {
			toReturn = getGpsDataPoint(line);
		}
		else if (firstDataMember.equals(Constants.SENSOR_BT_FIRST_DATA_NAME)) {
			toReturn = getBtDataPoint(line);
		}
		else if (firstDataMember.equals(Constants.SENSOR_BEACON_FIRST_DATA_NAME)) {
			toReturn = getBeaconDataPoint(line);
		}
		else if (firstDataMember.equals(Constants.SENSOR_ACTIVITY_FIRST_DATA_NAME)) {
			toReturn = getActivityDataPoint(line);
		}
		
		return toReturn;
	}
	
	private GpsDataPoint getGpsDataPoint(String[] line) throws ParseException {
        double latitude = -1;
        double longitude = -1;
        
        if (_formatVersion == 1) {
            String lat = line[Constants.SENSOR_GPS_LAT_IDX].replaceAll("\"","");
            String lon = line[Constants.SENSOR_GPS_LON_IDX].replaceAll("\"","");
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lon);
        }
        else if (_formatVersion == 2 || _formatVersion == 0) {
        	latitude = Double.parseDouble(line[Constants.SENSOR_GPS_LAT_IDX]
            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "")
            		.split(":")[1]);
        	longitude = Double.parseDouble(line[Constants.SENSOR_GPS_LON_IDX]
            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "")
            		.split(":")[1]);
        }
		
        return new GpsDataPoint(getDateTime(line, Constants.SENSOR_GPS_TIME_IDX), latitude, longitude);
	}
	
	private ActivityDataPoint getActivityDataPoint(String[] line) throws ParseException {
		IMJ_Map<String, Integer> activities = new MJ_Map_Factory<String, Integer>().create();
    	for (String s: Constants.SENSOR_ACTIVITY_NAMES) {
    		activities.put(s, -1);
    	}

        if (_formatVersion <= 2 && line.length >= 10) {  // if activities are recorded at all
        	int[] indices = Constants.SENSOR_ACTIVITY_IDICES;
        	
        	for (int i = 0; i<activities.size(); i++) {
        		int fileIdx = indices[i];
        		int val = Integer.parseInt(line[fileIdx]
                		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "")
                		.split(":")[1]);
        		activities.replace(activities.getKey(i), val);
        	}
        }
		
        return new ActivityDataPoint(getDateTime(line, Constants.SENSOR_GPS_TIME_IDX), activities);
	}
	
	private BtDataPoint getBtDataPoint(String[] line) throws ParseException {
		IMJ_OC<BtDeviceData> data = new MJ_OC_Factory<BtDeviceData>().create();
        int timeIdx = Constants.SENSOR_BT_TIME_IDX;
        
		if (_formatVersion == 2) {
	        int nameIdx = Constants.SENSOR_BT_FIRST_DEVNAME_IDX;
	        int rawIdx = Constants.SENSOR_BT_FIRST_RAW_IDX;
	        int smoothIdx = Constants.SENSOR_BT_FIRST_SMOOTHED_IDX;
	        
	    	do {
	        	String name = line[nameIdx]
	            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "")
	            		.split(":")[1];
	            int rawRSSI = Integer.parseInt(line[rawIdx]
	            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "")
	            		.split(":")[1]);
	            double smoothedRSSI = Double.parseDouble(line[smoothIdx]
	            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "")
	            		.split(":")[1]);
	            
	            data.add(new BtDeviceData(name, rawRSSI, smoothedRSSI));
	            
	            nameIdx += 3;
	            rawIdx += 3;
	            smoothIdx += 3;
	            
	        } while (line[nameIdx].contains(Constants.SENSOR_BT_FIRST_DATA_NAME));
			timeIdx = nameIdx;
		}
        
        return new BtDataPoint(getDateTime(line, timeIdx), data);
	}
	
	private BeaconDataPoint getBeaconDataPoint(String[] line) throws ParseException {
        return new BeaconDataPoint(getDateTime(line, Constants.SENSOR_BEACON_TIME_IDX));
	}
	
	private Calendar getDateTime(String[] line, int idx) throws ParseException {
		Calendar dateTime = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm aa", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.S", Locale.US);
        String date = line[idx];
        if (date.contains("ODIN")) {
        	System.out.println(line);
        }
        Date d = sdf.parse(date);
        dateTime.setTime(d);
        return dateTime;
	}
}
