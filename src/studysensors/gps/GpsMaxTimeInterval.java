package studysensors.gps;

import studysensors.Constants;
import studysensors.gps.gpsDeepLayer.OneCouponsGpsData;
import stats.OneReport;

/**
 *
 * @author Maisha
 */
public class GpsMaxTimeInterval extends GpsPerformanceEvaluation {
    
    public GpsMaxTimeInterval(OneCouponsGpsData data, double si) {
        super(data, si);
    }
    
    @Override
    public long getValue(){
        if (_val != null){
            return _val;
        }
        
        long t1 = _data.getDataAtIdx(0).getDateTime().getTimeInMillis();
        long t2;
        long maxDiff = 0;
        long tempDiff;
        int numDataPoints = _data.length();
        
        for (int i = 1; i<numDataPoints; i++){
            t2 = _data.getDataAtIdx(i).getDateTime().getTimeInMillis();
            tempDiff = t2-t1;
            if (tempDiff > maxDiff){
                maxDiff = tempDiff;
            }
            t1 = t2;
        }
        
        double answer = maxDiff / 1000.0;
        _val = Math.round(answer);
        return _val;
    }
    
    @Override
    public void printAll(){
        long maxGap = this.getValue();
        String message = "\n\tMax time between GPS recordings (sec): " + maxGap;
        if (maxGap > (5 * this._sensorInterval)){
            message += " (" + maxGap/60 + " mins)";
        }
        if (maxGap/60 > 120){
            message += " (" + maxGap/60/60 + " hours)";
        }
        message += "\n\tMax percentage variance from requested interval: " + this.getValueInPercent();
        System.out.println(message);
    }

    @Override
    public OneReport addToMap(OneReport map) {
        map.addValue(Constants.REPORTS_MAXT_BTW_SENSOR_RECS, getValue() * 1.0, 
        		"Maximum/largest time in seconds between actual GPS recordings");
        map.addValue(Constants.REPORTS_MAXT_BTW_SENSOR_RECS_AS_PERC, getValueInPercent(), 
        		"Percent deviation of largest actual time between GPS recordings from sensor interval");
        return map;
    }
}
