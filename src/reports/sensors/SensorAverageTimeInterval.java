package reports.sensors;

import constants.ConstTags;
import reports.OneReport;
import sensors.data.SensorDataOfOneType;

/**
 *
 * @author Maisha Jauernig
 */
public class SensorAverageTimeInterval extends AbsSensorPerformanceEval {

    public SensorAverageTimeInterval(SensorDataOfOneType data, double si, SensorPerformances sps) {
        super(data, si, sps);
        _val = _sensorPerformances.getAvg();
    }
    
    @Override
    public void printAll(){
        System.out.println("\n\t" + ConstTags.REPORTS_A_O_S_I_S_TEXT + ": " + _val +
                "\n\t" + ConstTags.REPORTS_A_O_S_TEXT + ": " + getValueInPercent() +
                "\n\t" + ConstTags.REPORTS_S_D_O_S_TEXT + ": " + getStdev());
    }
    
    @Override
    public OneReport addToMap(OneReport map) {
        Double stdev = getStdev();
        Double avgInPer = getValueInPercent();
        map.addValue(ConstTags.REPORTS_AVERAGE_ONE_SENSOR_IN_SECS, _val, ConstTags.REPORTS_A_O_S_I_S_TEXT);
        map.addValue(ConstTags.REPORTS_AVERAGE_ONE_SENSOR, avgInPer, ConstTags.REPORTS_A_O_S_TEXT);
        map.addValue(ConstTags.REPORTS_STANDARD_DEV_ONE_SENSOR, stdev, ConstTags.REPORTS_S_D_O_S_TEXT);
        return map;
    }
    
    private Double getStdev(){
        if (_data.isEmpty()) {
        	return null;
        }
        
        long t1 = _data.get(0).getDateTime().getTimeInMillis();
        long t2;
        int numDataPoints = _data.size(); 
        int numTimeIntervals = numDataPoints - 1;
        long sumSq = 0;
        long t;
        
        for (int i = 1; i<numDataPoints; i++){
            t2 = _data.get(i).getDateTime().getTimeInMillis();
            t = t2-t1;
            sumSq += (t - _val*1000) * (t - _val*1000);
            t1 = t2;
        }
        
        double stdev = Math.sqrt(sumSq / numTimeIntervals*1.0);
        double answerInSecs = Math.round(stdev / 1000.0);
        double answerInPer = answerInSecs / _sensorInterval * 100.0;
        return answerInPer;
    }
}
