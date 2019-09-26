package StudySensorsTests;
        
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Constants.ConstTags;
import orderedcollection.*;
import stats.*;
import studysensors.*;

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
    static ReportsCollection allReports;
        
    public Test1() throws ParseException {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            AnalysisEngine eng = new AnalysisEngineBuilder(dir1, vers1).registerGpsAnalyses().registerWhileAtAnalyses().build();
            allReports = eng.getAllReports();
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

    @Test
    public void checkGPS() {
        
        IMJ_OC<OneReport> gpsSensorReports = allReports.getAnalysesByType(ConstTags.REPORT_TYPE_GPS_SENSOR_ANALYSIS);
        
        for (OneReport oneRep: gpsSensorReports) {
//            System.out.println(oneRep.getValue(Constants.REPORTS_SENSOR_INTERVAL));
//            System.out.println(oneRep.getValue(Constants.REPORTS_AVERAGE_ONE_SENSOR));
//            System.out.println(oneRep.getValue(Constants.REPORTS_STANDARD_DEV_ONE_SENSOR));
//            System.out.println(oneRep.getValue(Constants.REPORTS_TOTAL_SENSOR_RECS));
         
            assertTrue(oneRep.getValue(ConstTags.REPORTS_SENSOR_INTERVAL) == 3600.0);
            assertTrue(oneRep.getValue(ConstTags.REPORTS_AVERAGE_ONE_SENSOR) == 100.0);
            assertTrue(oneRep.getValue(ConstTags.REPORTS_STANDARD_DEV_ONE_SENSOR) == 0.0);
            assertTrue(oneRep.getValue(ConstTags.REPORTS_TOTAL_SENSOR_RECS) == 108.0);
        }
    }
    
    @Test
    public void checkWhileAt() {
        IMJ_OC<OneReport> WhileAtReports = allReports.getAnalysesByType(ConstTags.REPORT_TYPE_WHILEAT_RULE_ANALYSIS);
        
        for (OneReport oneRep: WhileAtReports) {
            assertTrue(oneRep.getValue(ConstTags.REPORTS_GOOD_RULE_FIRES) == 0.0);
            assertTrue(oneRep.getValue(ConstTags.REPORTS_IDEAL_RULE_FIRES) == 20.0);
            assertTrue(oneRep.getValue(ConstTags.REPORTS_GOOD_FIRE_PERCENT) == 0.0);
            assertTrue(oneRep.getValue(ConstTags.REPORTS_RULE_MIN_T) == 10800.0);
        }
    }
}
