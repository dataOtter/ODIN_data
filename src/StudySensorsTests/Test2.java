//package StudySensorsTests;
//        
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//import java.io.FileNotFoundException;
//import java.text.ParseException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import maps.*;
//import orderedcollection.*;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import studysensors.AnalysisEngine;
//import studysensors.AnalysisEngineBuilder;
//import studysensors.general.Constants;
//
///**
// *
// * Sensor interval: 1 hour
//Min time since last fire: 3 hours
//Answers.csv: empty
//Couponid(s): 1
//	- one hour at each location
//	(12 recordings true, 12 false, 12 t, 12 f, 12 t, 12 f, 12 t, 12 f, 12 t)
//
//expected results:
//	- 0 rule fires
//	- rule should have fired 20 times (4 per 12 recordings interval = 4*5 = 20)
//
// * @author Maisha
// */
//public class Test2 {
//    
//    static String dir1 = "C:\\Users\\Bilal\\Dropbox\\MB_various\\UNL\\Data\\02-21-2019_120\\t1.2";
//    static int vers1 = 0;
//    
//    // outermost String = GPSSensor, WhileAtRule
//        //    OC is one per coupon or (coupon+ruleid)
//        //       innermost String: measurement name
//    static IMJ_Map<String, IMJ_OC<IMJ_Map<String, Double>>> allReports;
//        
//    public Test2() throws FileNotFoundException, ParseException {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//        
//        AnalysisEngineBuilder setup;
//        try {
//            setup = new AnalysisEngineBuilder(dir1, vers1);
//        
//            AnalysisEngine eng = new AnalysisEngine();
//            eng = setup.registerGpsAnalyses(eng);
//            eng = setup.registerWhileAtAnalyses(eng);
//            allReports = eng.getAllReports();
//        } catch (FileNotFoundException ex) {
//            fail("Could not setup test: "+ex.getMessage());
//        }catch (ParseException ex) {
//            fail("Could not setup test: "+ex.getMessage());
//        }
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//        allReports = null;
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
//    // TODO add test methods here.
//    // The methods must be annotated with annotation @Test. For example:
//    //
//    @Test
//    public void checkGPS() {
//        
//        IMJ_OC<IMJ_Map<String, Double>> gpsSensorReports = allReports.getValueOfKey(Constants.REPORT_TYPE_GPS_SENSOR_ANALYSIS);
//        
//        for (int i = 0; i<gpsSensorReports.length(); i++){
//            IMJ_Map<String, Double> oneRep = gpsSensorReports.getItem(i);
//            assertTrue(oneRep.getValueOfKey(Constants.REPORTS_SENSOR_INTERVAL) == 3600.0);
//            assertTrue(oneRep.getValueOfKey(Constants.REPORTS_AVERAGE_ONE_SENSOR) == 100.0);
//            assertTrue(oneRep.getValueOfKey(Constants.REPORTS_STANDARD_DEV_ONE_SENSOR) == 0.0);
//            assertTrue(oneRep.getValueOfKey("Total GPS recordings") == 108.0);
//        }
//    }
//    
//    @Test
//    public void checkWhileAt() {
//        IMJ_OC<IMJ_Map<String, Double>> WhileAtReports = allReports.getValueOfKey(Constants.REPORT_TYPE_WHILEAT_RULE_ANALYSIS);
//        
//        for (int i = 0; i<WhileAtReports.length(); i++){
//            IMJ_Map<String, Double> oneRep = WhileAtReports.getItem(i);
//            assertTrue(oneRep.getValueOfKey("Number of good rule fires") == 1.0);
//            assertTrue(oneRep.getValueOfKey("Number of ideal world rule fires") == 20.0);
//            assertTrue(oneRep.getValueOfKey(Constants.REPORTS_GOOD_FIRE_PERCENT) == 5.0);
//            assertTrue(oneRep.getValueOfKey("Rule required minimum time between fires") == 10800.0);
//        }
//    }
//}
