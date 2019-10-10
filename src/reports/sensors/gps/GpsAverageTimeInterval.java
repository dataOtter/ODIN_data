package reports.sensors.gps;

import constants.ConstTags;
import reports.OneReport;
import sensors.gps.OneCouponsGpsData;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsAverageTimeInterval extends AbsGpsPerformanceEval {

    public GpsAverageTimeInterval(OneCouponsGpsData data, double si) {
        super(data, si);
    }
    
    @Override
    public long getValue(){
        if (_val != null){
            return _val;
        }
        
        long t1 = _data.getDataAtIdx(0).getDateTime().getTimeInMillis();
        long t2;
        int numDataPoints = _data.length();
        int numTimeIntervals = numDataPoints - 1;
        long sum = 0;
        
        for (int i = 1; i<numDataPoints; i++){
            t2 = _data.getDataAtIdx(i).getDateTime().getTimeInMillis();
            sum += t2-t1;
            t1 = t2;
        }
        
        long avg = sum / numTimeIntervals;
        double avgInSecs = avg / 1000.0;
        _val = Math.round(avgInSecs);
        return _val;
    }
    
    private double getStdev(){
        if (_val == null){
            _val = getValue();
        }
        
        long t1 = _data.getDataAtIdx(0).getDateTime().getTimeInMillis();
        long t2;
        int numDataPoints = _data.length(); 
        int numTimeIntervals = numDataPoints - 1;
        long sumSq = 0;
        long t;
        
        for (int i = 1; i<numDataPoints; i++){
            t2 = _data.getDataAtIdx(i).getDateTime().getTimeInMillis();
            t = t2-t1;
            sumSq += (t - _val*1000) * (t - _val*1000);
            t1 = t2;
        }
        
        double stdev = Math.sqrt(sumSq / numTimeIntervals*1.0);
        double answerInSecs = Math.round(stdev / 1000.0);
        double answerInPer = answerInSecs / _sensorInterval * 100.0;
        return answerInPer;
    }
    
    @Override
    public void printAll(){
        System.out.println("\n\t" + ConstTags.REPORTS_A_O_S_I_S_TEXT + ": " + getValue() +
                "\n\t" + ConstTags.REPORTS_A_O_S_TEXT + ": " + getValueInPercent() +
                "\n\t" + ConstTags.REPORTS_S_D_O_S_TEXT + ": " + getStdev());
    }
    
    @Override
    public OneReport addToMap(OneReport map) {
        double avg = getValue() * 1.0;
        double stdev = getStdev();
        double avgInPer = getValueInPercent();
        map.addValue(ConstTags.REPORTS_AVERAGE_ONE_SENSOR_IN_SECS, avg, ConstTags.REPORTS_A_O_S_I_S_TEXT);
        map.addValue(ConstTags.REPORTS_AVERAGE_ONE_SENSOR, avgInPer, ConstTags.REPORTS_A_O_S_TEXT);
        map.addValue(ConstTags.REPORTS_STANDARD_DEV_ONE_SENSOR, stdev, ConstTags.REPORTS_S_D_O_S_TEXT);
        return map;
    }
}
