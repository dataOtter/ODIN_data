/**
 * 
 */
package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import constants.Constants;
import maps.*;

/**
 * @author Maisha Jauernig
 *
 */
public class ServieToHeartbeatReader {
	private final String _path;
	private final int _formatVersion;
	
	/**
	 * Reads all data from the ServieToHeartbeat.csv file 
	 * @param path
	 * @param formatVersion
	 */
	public ServieToHeartbeatReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
	}
	
	public IMJ_Map<Integer, String> getServicesNames() {
		IMJ_Map<Integer, String> map = new MJ_Map_Factory<Integer, String>().create();
		
        Scanner sc = new ScannerHelper(_path, Constants.SERVICES_CSV, Constants.SERVICES_NUM_COLS).getScanner();
		
        while ( sc.hasNextLine() ){
        	String[] line = sc.nextLine().split(",");
        	int serviceId = Integer.parseInt(line[Constants.SERVICES_SERVICEID_IDX]);
        	String serviceName = line[Constants.SERVICES_SERVICENAME_IDX];
        	map.put(serviceId, serviceName);
        	
        } 
        sc.close();
        
        return map;
	}
	
	IMJ_Map<Integer, IMJ_Map<String, Calendar>> getUploadTimes() throws ParseException {
		IMJ_Map<Integer, String> servicesNames = getServicesNames();
		
		IMJ_Map<Integer, IMJ_Map<String, Calendar>> map = new MJ_Map_Factory<Integer, IMJ_Map<String, Calendar>>().create();
		
        Scanner sc = new ScannerHelper(_path, Constants.SERVICETOHEARTBEAT_CSV, Constants.SERVICETHB_NUM_COLS).getScanner();
		
        while ( sc.hasNextLine() ){
        	String[] line = sc.nextLine().split(",");
        	int cid = Integer.parseInt(line[Constants.SERVICETHB_COUPONID_IDX]);
        	int serviceId = Integer.parseInt(line[Constants.SERVICETHB_SERVICEID_IDX]);
        	String serviceName = servicesNames.get(serviceId);
        	Calendar lastUploadTime = getTimeFromLine(line, Constants.SERVICETHB_LASTUPLOADTIME_IDX);
        	
        	if (map.containsKey(cid)) {
        		map.get(cid).put(serviceName, lastUploadTime);
        	}
        	else {
        		IMJ_Map<String, Calendar> map2 = new MJ_Map_Factory<String, Calendar>().create();
        		map2.put(serviceName, lastUploadTime);
        		map.put(cid, map2);
        	}
        } 
        sc.close();
        
        return map;
	}
	
	private Calendar getTimeFromLine(String[] line, int idx) throws ParseException {
        Calendar dateTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.S", Locale.US);
        Date d = sdf.parse(line[idx]);
        dateTime.setTime(d);
        return dateTime;
    }
}
