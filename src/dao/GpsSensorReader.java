package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import studysensors.Constants;
import studysensors.gps.GpsDataCollection;
import studysensors.gps.gpsDeepLayer.*;

public class GpsSensorReader {
	private final String _path;
	private final int _formatVersion;
	
	public GpsSensorReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
	}
	
	public GpsDataCollection getAllGpsSensorData(String gpsSensorTblName) throws ParseException {
        Scanner sc = new ScannerHelper(_path, gpsSensorTblName + ".csv", Constants.SENSOR_NUM_COLS).getScanner();
        
        GpsDataCollection sensorCollection = new GpsDataCollection();
		
        while ( sc.hasNextLine() ){
        	String[] line = sc.nextLine().split(",");
            int cid = Integer.parseInt(line[Constants.SENSOR_COUPONID_IDX]);
            GpsDataPoint gpsdp = getGpsDataPoint(line);
            
            if ( sensorCollection.hasCouponEntry(cid) ) {
            	sensorCollection.getCouponData(cid).addGpsDataPoint(gpsdp);
            }
            else{
            	OneCouponsGpsData couponData = new OneCouponsGpsData(cid);
                couponData.addGpsDataPoint(gpsdp);
                sensorCollection.addCouponAndItsData(cid, couponData);
            }
            
        }
        sc.close();
        return sensorCollection;
	}
	
	private GpsDataPoint getGpsDataPoint(String[] line) throws ParseException {
		Calendar dateTime = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm aa", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.S", Locale.US);
        Date d = sdf.parse(line[Constants.SENSOR_GPS_TIME_IDX]);
        dateTime.setTime(d);

        double latitude = -1;
        double longitude = -1;
        
        if (_formatVersion <= 1) {
            String lat = line[Constants.SENSOR_GPS_LAT_IDX].replaceAll("\"","");
            String lon = line[Constants.SENSOR_GPS_LON_IDX].replaceAll("\"","");
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lon);
        }
        else if (_formatVersion <= 2) {
            //System.out.println(line[Constants.SENSOR_GPS_LAT_IDX]);
            //System.out.println(line[Constants.SENSOR_GPS_LON_IDX]);
            
            String latString = line[Constants.SENSOR_GPS_LAT_IDX]
            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "");
            String lonString = line[Constants.SENSOR_GPS_LON_IDX]
            		.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "").replaceAll("\\]", "");

            latString = latString.split(":")[1];
            lonString = lonString.split(":")[1];
            
            //System.out.println(latString);
            //System.out.println(lonString);
            
            latitude = Double.parseDouble(latString);
            longitude = Double.parseDouble(lonString);
        }
        
        return new GpsDataPoint(dateTime, latitude, longitude);
	}

}
