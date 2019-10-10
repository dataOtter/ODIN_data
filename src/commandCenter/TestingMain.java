package commandCenter;

import java.io.FileNotFoundException;

import constants.Constants;
import dao.AnswersReader;
import dao.CouponReader;
import dao.GpsSensorReader;
import dao.RulesReader;
import dao.SensorTblNamesReader;
import dao.SensorsReader;
import orderedcollection.IMJ_OC;
import reports.OneReport;
import reports.rules.AnswersCollection;
import reports.rules.RulesCollection;
import reports.rules.whileAt.AnalysisWhileAt;
import reports.sensors.gps.GpsAverageTimeInterval;
import reports.sensors.gps.GpsMaxTimeInterval;
import reports.sensors.gps.GpsMinTimeInterval;
import reports.sensors.gps.GpsRecordingsWithinGivenPercentOfTimeInterval;
import sensors.StudySensorsCollection;
import sensors.gps.GpsCoordinate;
import sensors.gps.GpsCoordsCluster;
import sensors.gps.GpsCoordsClusterCenterCalc;
import sensors.gps.GpsCoordsClustersGalaxiesUniverse;
import sensors.gps.GpsCoordsClustersGalaxy;
import sensors.gps.GpsDataCollection;
import sensors.gps.IGpsCoordsClusterObserver;
import sensors.gps.OneCouponsGpsData;

/**
 *
 * @author Maisha Jauernig
 */
public class TestingMain {
    final static int PERCENT_RANGE = 1;

    public static void main(String[] args) throws Exception {
    	String gpsSensorTblName = new SensorTblNamesReader(Constants.DIRECTORY_PATH, 
    			Constants.DEFAULT_FORMAT_VERSION).getSensorTblNames().get(0);
    	GpsDataCollection gpsSensorData = new GpsSensorReader(Constants.DIRECTORY_PATH, 
    			Constants.DEFAULT_FORMAT_VERSION).getAllGpsSensorData(gpsSensorTblName);
    	IMJ_OC<Integer> cids = new CouponReader(Constants.DIRECTORY_PATH, 
    			Constants.DEFAULT_FORMAT_VERSION).getActiveCouponIds();
    	StudySensorsCollection sensors = new SensorsReader(Constants.DIRECTORY_PATH, 
    			Constants.DEFAULT_FORMAT_VERSION).getStudySensorsCollection();
    	AnswersCollection answers = new AnswersReader(Constants.DIRECTORY_PATH, 
    			Constants.DEFAULT_FORMAT_VERSION).getAllAnswers();
    	RulesCollection rules = new RulesReader(Constants.DIRECTORY_PATH, 
    			Constants.DEFAULT_FORMAT_VERSION).getAllRules();
        int sensorId = 12;
        double sensorInterval = sensors.getSensorInterval(sensorId);
        
        for (int couponId: cids) {
            OneCouponsGpsData d = gpsSensorData.getCouponData(couponId);
            System.out.println("\nCoupon ID " + couponId);
            testAllGpsPerformanceEvaluations(d, sensorInterval);
            testGpsCoordinateClass(d);
            testGpsCoordsClusterClass(d);
            testGpsCoordsClustersGalaxyClass(d);
            testGpsCoordsClustersGalaxiesUniverseClass(d, 2);
            
            int cid = 1, rid = 1;        
            AnalysisWhileAt eval = new AnalysisWhileAt(answers, rules, gpsSensorData, sensorInterval, cid, rid);
            OneReport report = eval.getAnalysisReport();
            System.out.println(report);
        } 
        
        System.out.println(getStdev());
    }
    
    private static double getStdev(){
        double _val = 10.22;
        int numDataPoints = 10;
        double[] _data = {0.0,4.0,13.0,24.0,36.0,53.0,58.0,66.0,78.0,92.0};
        double t1 = _data[0];
        double t2;
        int numTimeIntervals = 9;
        double sumSq = 0.0;
        double t;
        
        for (int i = 1; i<numDataPoints; i++){
            t2 = _data[i];
            t = t2-t1;
            sumSq += (t - _val) * (t - _val);
            t1 = t2;
        }
        
        double stdev = Math.sqrt(sumSq / numTimeIntervals*1.0);
        return stdev;
    }
    
    private static void testAllGpsPerformanceEvaluations(OneCouponsGpsData d, double sensorInterval) throws FileNotFoundException{
        GpsAverageTimeInterval avg;
        GpsMinTimeInterval min;
        GpsMaxTimeInterval max;
        GpsRecordingsWithinGivenPercentOfTimeInterval numInRange;
        
        avg = new GpsAverageTimeInterval(d, sensorInterval);
        avg.printTimeInterval();
        avg.printAll();
        min = new GpsMinTimeInterval(d, sensorInterval);
        min.printAll();
        max = new GpsMaxTimeInterval(d, sensorInterval);
        max.printAll();
        numInRange = new GpsRecordingsWithinGivenPercentOfTimeInterval(d, sensorInterval);
        numInRange.printAll();
    }
    
    private static void testGpsCoordinateClass(OneCouponsGpsData d){
        GpsCoordinate c;
        for (int i = 0; i<d.length(); i++){
            c = d.getDataAtIdx(i).getGpsCoord();
            c.print();
        }
    }
    
    private static void testGpsCoordsClusterClass(OneCouponsGpsData d){
        GpsCoordinate c = d.getDataAtIdx(0).getGpsCoord();
        GpsCoordsCluster cl = new GpsCoordsCluster(c);
        IGpsCoordsClusterObserver obs = new GpsCoordsClusterCenterCalc();
        cl.registerObserver(obs);
        
        for (int i = 1; i<d.length(); i++){
            c = d.getDataAtIdx(i).getGpsCoord();
            cl.addCoordToCluster(c);
        }
        
        
        GpsCoordsClusterCenterCalc cal;
        IGpsCoordsClusterObserver o = cl.getSpecifiedObserver(GpsCoordsClusterCenterCalc.class);
        cal = (GpsCoordsClusterCenterCalc) o;
        c = cal.getCenter();
        System.out.println("\n\n");
        c.print();
    }
    
    private static void testGpsCoordsClustersGalaxyClass(OneCouponsGpsData d){
        GpsCoordsClustersGalaxy g = new GpsCoordsClustersGalaxy();
        g.makeFirstGalaxy(d);
        GpsCoordsCluster cl;
        GpsCoordsClusterCenterCalc cal;
        
        for (int i = 0; i<d.length()-1; i++){
            g = g.getNextGalaxy();
            for (int j = 0; j<g.length(); j++){
                cl = g.getClusterAtIdx(j);
                for (int x = 0; x<cl.length(); x++){
                    cl.getCoordAtIdx(x).print();
                }
                System.out.println("Center: ");
                IGpsCoordsClusterObserver o = cl.getSpecifiedObserver(GpsCoordsClusterCenterCalc.class);
                cal = (GpsCoordsClusterCenterCalc) o;
                cal.getCenter().print();
                System.out.println("\n");
            }
            System.out.println("\n\n");
        }
    }
    
    private static void testGpsCoordsClustersGalaxiesUniverseClass(OneCouponsGpsData d, int numClusters){
        GpsCoordsClustersGalaxiesUniverse u = new GpsCoordsClustersGalaxiesUniverse(d);
        GpsCoordsClustersGalaxy g = u.getGalaxyWithNClusters(numClusters);
        GpsCoordsCluster cl;
        GpsCoordsClusterCenterCalc cal;
        
        for (int j = 0; j<g.length(); j++){
            cl = g.getClusterAtIdx(j);
            for (int x = 0; x<cl.length(); x++){
                //cl.getCoordAtIdx(x).print();
            }
            System.out.println("Number of points in this cluster: " + cl.length());
            System.out.println("Center: ");
            IGpsCoordsClusterObserver o = cl.getSpecifiedObserver(GpsCoordsClusterCenterCalc.class);
            cal = (GpsCoordsClusterCenterCalc) o;
            cal.getCenter().print();
            System.out.println("\n");
        }
    }
}
