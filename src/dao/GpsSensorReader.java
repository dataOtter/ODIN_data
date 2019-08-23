package dao;

import java.io.File;
import java.io.FileNotFoundException;
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
	
	public GpsDataCollection getAllGpsSensorData(String gpsSensorTblName) throws FileNotFoundException, ParseException {
		File f = new File(_path + "\\" + gpsSensorTblName + ".csv");
        Scanner sc = new Scanner(f);
        sc.nextLine();
        
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
        Date d = sdf.parse(line[Constants.SENSOR_TIME_IDX]);
        dateTime.setTime(d);
        
        String lat = line[Constants.SENSOR_GPS_LAT_IDX].replaceAll("\"","");
        String lon = line[Constants.SENSOR_GPS_LON_IDX].replaceAll("\"","");
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);
        
        return new GpsDataPoint(dateTime, latitude, longitude);
	}

}
