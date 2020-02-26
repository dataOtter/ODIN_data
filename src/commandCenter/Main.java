package commandCenter;

import java.io.IOException;
import java.text.ParseException;

import constants.Constants;
import dao.OutputFileWriter;
import orderedcollection.*;
import reports.AnalysisEngine;
import reports.AnalysisEngineBuilder;
import reports.ReportsCollection;
import reports.stats.StatsBuilder;
import reports.stats.StatsEngine;

/**
 *
 * @author Maisha Jauernig
 */
public class Main {

    public static void main(String[] args) throws ParseException, IOException {
    	
    	//checkPredicate();
    	
        String path = Constants.DIRECTORY_PATH;
        int formatVersion = Constants.DEFAULT_FORMAT_VERSION;
        IMJ_OC<String> consentstatuses = new MJ_OC_Factory<String>().create();
        //consentstatuses.add(Constants.COUPON_CONSENTSTATUS_CONSENTREVOKED);
        //consentstatuses.add(Constants.COUPON_CONSENTSTATUS_CONSENTWITHDRAWN);
        consentstatuses.add(Constants.COUPON_CONSENTSTATUS_CONSENTAGREED);
        
        		
        AnalysisEngineBuilder bld = new AnalysisEngineBuilder(path, formatVersion, consentstatuses);
        //AnalysisEngine eng = bld.addSensorJobs().addRuleJobs().buildEngine();
        //AnalysisEngine eng = bld.addRuleJobs().buildEngine();
        AnalysisEngine eng = bld.addSensorJobs().buildEngine();
        
        ReportsCollection allReports = eng.getAllReports();
        
        StatsBuilder sb = new StatsBuilder(path, formatVersion, allReports).enableSensorsStats().enableRulesStats();
        StatsEngine stats = sb.build();
        
        ReportsCollection allStats = stats.getStats();
        
        OutputFileWriter out = new OutputFileWriter(allReports, path, formatVersion);
        out.writeAllDataToFiles();
        
        System.out.println(allReports);
        //System.out.println(allStats);

    }
    
    public static void checkPredicate() {

		final int R = 6371; // Radius of the earth

		double actualLatitude = 40.8443527;
		double actualLongitude = -96.682223;

		double expectedLatitude = 40.81854;
		double expectedLongitude = -96.70404;

		double dLat = Math.toRadians(expectedLatitude - actualLatitude);
		double dLon = Math.toRadians(expectedLongitude - actualLongitude);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(expectedLatitude))
				* Math.cos(Math.toRadians(expectedLatitude)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distanceInKM = R * c;
		double distanceInMeters = distanceInKM * 1000;

		System.out.println("Difference between two locations in meters is:" + distanceInMeters);
	}

}
