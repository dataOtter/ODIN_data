package StudySensorsTests;
        
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.text.ParseException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import orderedcollection.IMJ_OC;
import stats.OneReport;
import stats.ReportsCollection;
import studysensors.AnalysisEngine;
import studysensors.AnalysisEngineBuilder;
import studysensors.Constants;

/**
 *
 * Sensor interval: 1 hour
Min time since last fire: 3 hours
Answers.csv: empty
Couponid(s): 1
	- one hour at each location
	(12 recordings true, 12 false, 12 t, 12 f, 12 t, 12 f, 12 t, 12 f, 12 t)

expected results:
	- 0 rule fires
	- rule should have fired 20 times (4 per 12 recordings interval = 4*5 = 20)

 * @author Maisha
 */
public class Test1 {
    
    static String dir1 = "C:\\Users\\Bilal\\Dropbox\\MB_various\\UNL\\Data\\02-21-2019_120\\t1";
    static int vers1 = 0;
    
    // outermost String = GPSSensor, WhileAtRule
        //    OC is one per coupon or (coupon+ruleid)
        //       innermost String: measurement name
    static ReportsCollection allReports;
        
    public Test1() throws FileNotFoundException, ParseException {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
        AnalysisEngineBuilder setup;
        try {
            AnalysisEngine eng = new AnalysisEngineBuilder(dir1, vers1).registerGpsAnalyses().registerWhileAtAnalyses().build();
            allReports = eng.getAllReports();
        } catch (FileNotFoundException ex) {
            fail("Could not setup test: "+ex.getMessage());
        }catch (ParseException ex) {
            fail("Could not setup test: "+ex.getMessage());
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        allReports = null;
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void checkGPS() {
        
        IMJ_OC<OneReport> gpsSensorReports = allReports.getAnalysesByType(Constants.REPORT_TYPE_GPS_SENSOR_ANALYSIS);
        
        for (int i = 0; i<gpsSensorReports.length(); i++){
        	OneReport oneRep = gpsSensorReports.getItem(i);

//            System.out.println(oneRep.getValue(Constants.REPORTS_SENSOR_INTERVAL));
//            System.out.println(oneRep.getValue(Constants.REPORTS_AVERAGE_ONE_SENSOR));
//            System.out.println(oneRep.getValue(Constants.REPORTS_STANDARD_DEV_ONE_SENSOR));
//            System.out.println(oneRep.getValue(Constants.REPORTS_TOTAL_SENSOR_RECS));
            
            assertTrue(oneRep.getValue(Constants.REPORTS_SENSOR_INTERVAL) == 3600.0);
            assertTrue(oneRep.getValue(Constants.REPORTS_AVERAGE_ONE_SENSOR) == 100.0);
            assertTrue(oneRep.getValue(Constants.REPORTS_STANDARD_DEV_ONE_SENSOR) == 0.0);
            assertTrue(oneRep.getValue(Constants.REPORTS_TOTAL_SENSOR_RECS) == 108.0);
        }
    }
    
    @Test
    public void checkWhileAt() {
        IMJ_OC<OneReport> WhileAtReports = allReports.getAnalysesByType(Constants.REPORT_TYPE_WHILEAT_RULE_ANALYSIS);
        
        for (int i = 0; i<WhileAtReports.length(); i++){
        	OneReport oneRep = WhileAtReports.getItem(i);
            assertTrue(oneRep.getValue(Constants.REPORTS_GOOD_RULE_FIRES) == 0.0);
            assertTrue(oneRep.getValue(Constants.REPORTS_IDEAL_RULE_FIRES) == 20.0);
            assertTrue(oneRep.getValue(Constants.REPORTS_GOOD_FIRE_PERCENT) == 0.0);
            assertTrue(oneRep.getValue(Constants.REPORTS_RULE_MIN_T) == 10800.0);
        }
    }
}
