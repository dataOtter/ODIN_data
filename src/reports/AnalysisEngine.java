package reports;

import orderedcollection.*;
import reports.stats.*;

/**
 *
 * @author Maisha Jauernig
 */
public class AnalysisEngine {
    private final IMJ_OC<IAnalysis> _analyses;
    
    public AnalysisEngine(){
    	_analyses = new MJ_OC_Factory<IAnalysis>().create();
    }
    
    public void register(IAnalysis an) {
    	// TODO: should we check if this is a duplicate - 
    	// ie. there is another analysis with the same name?
    	_analyses.add(an);
    }
    
    public ReportsCollection getAllReports() {

    	ReportsCollection allReportsMap = new ReportsCollection();
    	
    	for (IAnalysis an: _analyses) {
    		if (an != null) {
    			String type = an.getAnalysisType();
        		OneReport rep = an.getAnalysisReport();
        		allReportsMap.addAnalysisByType(type, rep);
    		}
    	}
    	
        return allReportsMap;
    } 
//    
//    private IMJ_OC<IMJ_Map<String, Double>> getCidsToSensorsAvgAndStdev(){
//        
//        IMJ_Map<String, IMJ_Map<Double, IMJ_OC<Double>>> calcTypeToCidToSensorsValues = 
//                getCalcTypeToCidToSensorsValues();
//        IMJ_Map<Double, IMJ_OC<Double>> cidToSensorsAvgPercentDev = 
//                calcTypeToCidToSensorsValues.getValueOfKey(Constants.REPORTS_AVERAGE_ONE_SENSOR);
//        
//        IMJ_OC<IMJ_Map<String, Double>> cidToSensorsAvgAndStdev = 
//                new MJ_OC_Factory<IMJ_Map<String, Double>>().create();
//        
//        // loop through all cids
//        for (int i = 0; i<cidToSensorsAvgPercentDev.length(); i++){
//            double cid = cidToSensorsAvgPercentDev.getKeyAtIdx(i);
//            IMJ_OC<Double> sensorsAvgPer = cidToSensorsAvgPercentDev.getValueOfKey(cid);
//            Double avgAllSensors = getAvg(sensorsAvgPer);
//            Double stdevAllSensors = getStdev(sensorsAvgPer, avgAllSensors);
//            
//            IMJ_Map<String, Double> map = new MJ_Map_Factory<String, Double>().create();
//            map.add(Constants.REPORTS_COUPONID, cid);
//            map.add(Constants.REPORTS_AVERAGE_ALL_SENSORS, avgAllSensors);
//            map.add(Constants.REPORTS_STANDARD_DEV_ALL_SENSORS, stdevAllSensors);
//            cidToSensorsAvgAndStdev.append(map);
//        }
//        return cidToSensorsAvgAndStdev;
//    }
//    
//    private IMJ_OC<IMJ_Map<String, Double>> getCidsToRulesAvgAndStdev(){
//        
//        IMJ_Map<Double, IMJ_OC<Double>> cidToRulesPercentGood = 
//                getCidToRulesPercentGood();
//        
//        IMJ_OC<IMJ_Map<String, Double>> cidToRulesAvgAndStdev = 
//                new MJ_OC_Factory<IMJ_Map<String, Double>>().create();
//        
//        // loop through all cids
//        for (int i = 0; i<cidToRulesPercentGood.length(); i++){
//            double cid = cidToRulesPercentGood.getKeyAtIdx(i);
//            IMJ_OC<Double> rulesPer = cidToRulesPercentGood.getValueOfKey(cid);
//            Double avg = getAvg(rulesPer);
//            Double stdev = getStdev(rulesPer, avg);
//            
//            IMJ_Map<String, Double> map = new MJ_Map_Factory<String, Double>().create();
//            map.add(Constants.REPORTS_COUPONID, cid);
//            map.add(Constants.REPORTS_AVERAGE_RULES, avg);
//            map.add(Constants.REPORTS_STANDARD_DEVS_RULES, stdev);
//            cidToRulesAvgAndStdev.append(map);
//        }
//        return cidToRulesAvgAndStdev;
//    }
//    
//     private Double getAvg(IMJ_OC<Double> values){
//        double count = values.length();
//        double sum = 0;
//        for (int j = 0; j<count; j++){
//            sum += values.getItem(j);
//        }
//        return sum/count;
//    }
//    
//    private Double getStdev(IMJ_OC<Double> values, double avg){
//        double count = values.length();
//        double sumSqDiff = 0;
//        for (int j = 0; j<count; j++){
//            double per = values.getItem(j);
//            sumSqDiff += (per - avg) * (per - avg);
//        }
//        return Math.sqrt(sumSqDiff/count);
//    }
//    
//    private IMJ_Map<Double, IMJ_OC<Double>> getCidToRulesPercentGood(){
//            
//        IMJ_Map<Double, IMJ_OC<Double>> cidToRulesPercentGood = 
//                new MJ_Map_Factory<Double, IMJ_OC<Double>>().create();
//        
//        // for each cid and for each rule, 
//        // get the rule's percent of good answers out of ideal world answers
//        for (int i = 0; i<_allReportsMap.length(); i++){  // loop through all the reports 
//            String reportType = _allReportsMap.getKeyAtIdx(i);
//            
//            // only looking for rule reports, not sensor reports
//            if (! reportType.contains("ensor")){  
//                IMJ_OC<IMJ_Map<String, Double>> oneRulesReports = 
//                        _allReportsMap.getValueOfKey(reportType);
//                
//                // for each report of this rule
//                for (int j = 0; j<oneRulesReports.length(); j++){  
//                    double cid = oneRulesReports.getItem(j).
//                            getValueOfKey(Constants.REPORTS_COUPONID);
//                    double per = oneRulesReports.getItem(j).
//                            getValueOfKey(Constants.REPORTS_GOOD_FIRE_PERCENT);
//                    
//                    // if we already have some rule data for this cid
//                    if (cidToRulesPercentGood.contains(cid)){
//                        cidToRulesPercentGood.getValueOfKey(cid).append(per);
//                    }
//                    else{
//                        IMJ_OC<Double> cidData = new MJ_OC_Factory<Double>().create();
//                        cidData.append(per);
//                        cidToRulesPercentGood.add(cid, cidData);
//                    }
//                }
//            }
//        }
//        return cidToRulesPercentGood;
//    }
//    
//    private IMJ_Map<String, IMJ_Map<Double, IMJ_OC<Double>>> getCalcTypeToCidToSensorsValues(){
//            
//        IMJ_Map<Double, IMJ_OC<Double>> cidToSensorsAvgPercentDeviation = 
//                new MJ_Map_Factory<Double, IMJ_OC<Double>>().create();
//        IMJ_Map<Double, IMJ_OC<Double>> cidToSensorsStdevPercent = 
//                new MJ_Map_Factory<Double, IMJ_OC<Double>>().create();
//        
//        // loop through all sensor reports and gather avgs and stdevs
//        for (int i = 0; i<_allReportsMap.length(); i++){ 
//            String reportType = _allReportsMap.getKeyAtIdx(i);
//            
//            // only looking for sensor reports, not rule reports
//            if (reportType.contains("ensor")){  
//                IMJ_OC<IMJ_Map<String, Double>> oneSensorsReports = 
//                        _allReportsMap.getValueOfKey(reportType);
//        
//                // for each report of this sensor
//                for (int j = 0; j<oneSensorsReports.length(); j++){  
//                    IMJ_Map<String, Double> oneReport = oneSensorsReports.getItem(j);
//                    double cid = oneReport.getValueOfKey(Constants.REPORTS_COUPONID);
//                    double avgPer = oneReport.getValueOfKey(Constants.REPORTS_AVERAGE_ONE_SENSOR);   
//                    double stdev = oneReport.getValueOfKey(Constants.REPORTS_STANDARD_DEV_ONE_SENSOR); 
//                
//                    // if we already have some sensor data for this cid
//                    if (cidToSensorsAvgPercentDeviation.contains(cid)){
//                        cidToSensorsAvgPercentDeviation.getValueOfKey(cid).append(avgPer);
//                        cidToSensorsStdevPercent.getValueOfKey(cid).append(stdev);
//                    }
//                    else{
//                        IMJ_OC<Double> cidDataAvg = new MJ_OC_Factory<Double>().create();
//                        cidDataAvg.append(avgPer);
//                        cidToSensorsAvgPercentDeviation.add(cid, cidDataAvg);
//                        
//                        IMJ_OC<Double> cidDataStdev = new MJ_OC_Factory<Double>().create();
//                        cidDataStdev.append(stdev);
//                        cidToSensorsStdevPercent.add(cid, cidDataStdev);
//                    }
//                }
//            }
//        }
//        IMJ_Map<String, IMJ_Map<Double, IMJ_OC<Double>>> calcToCidToValues = 
//                new MJ_Map_Factory<String, IMJ_Map<Double, IMJ_OC<Double>>>().create();
//        calcToCidToValues.add(Constants.REPORTS_AVERAGE_ONE_SENSOR, cidToSensorsAvgPercentDeviation);
//        calcToCidToValues.add(Constants.REPORTS_STANDARD_DEV_ONE_SENSOR, cidToSensorsStdevPercent);
//        return calcToCidToValues;
//    }
}
