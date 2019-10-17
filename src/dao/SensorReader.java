package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import constants.Constants;
import maps.IMJ_Map;
import maps.MJ_Map_Factory;
import sensors.data.AbsDataPoint;
import sensors.data.ActivityDataPoint;
import sensors.data.BeaconDataPoint;
import sensors.data.BtDataPoint;
import sensors.data.DataCollection;
import sensors.data.GpsDataPoint;
import sensors.data.OneCouponsData;

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
	
	public void addAllSensorDataToDataColl(String sensorTblName, DataCollection coll) throws ParseException {
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
	            	OneCouponsData couponData = new OneCouponsData(cid);
	                couponData.addDataPoint(dp);
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
        
        if (_formatVersion <= 1) {
            String lat = line[Constants.SENSOR_GPS_LAT_IDX].replaceAll("\"","");
            String lon = line[Constants.SENSOR_GPS_LON_IDX].replaceAll("\"","");
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lon);
        }
        else if (_formatVersion <= 2) {
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
        String name = "";
        int rawRSSI = -1;
        double smoothedRSSI = -1.0;
        
        if (_formatVersion == 2) {
            name = line[Constants.SENSOR_BT_DEVNAME_IDX]
            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "")
            		.split(":")[1];
            rawRSSI = Integer.parseInt(line[Constants.SENSOR_BT_RAW_IDX]
            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "")
            		.split(":")[1]);
            smoothedRSSI = Double.parseDouble(line[Constants.SENSOR_BT_RAW_IDX]
            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "")
            		.split(":")[1]);
        }
        
        return new BtDataPoint(getDateTime(line, Constants.SENSOR_BT_TIME_IDX), name, rawRSSI, smoothedRSSI);
	}
	
	private BeaconDataPoint getBeaconDataPoint(String[] line) throws ParseException {
        return new BeaconDataPoint(getDateTime(line, Constants.SENSOR_BEACON_TIME_IDX));
	}
	
	private Calendar getDateTime(String[] line, int idx) throws ParseException {
		Calendar dateTime = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm aa", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.S", Locale.US);
        Date d = sdf.parse(line[idx]);
        dateTime.setTime(d);
        return dateTime;
	}
}
