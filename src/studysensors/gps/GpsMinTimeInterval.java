package studysensors.gps;

import studysensors.Constants;
import studysensors.gps.gpsDeepLayer.OneCouponsGpsData;
import stats.OneReport;

/**
 *
 * @author Maisha
 */
public class GpsMinTimeInterval extends GpsPerformanceEvaluation {
    
    public GpsMinTimeInterval(OneCouponsGpsData data, double si) {
        super(data, si);
    }
    
    @Override
    public long getValue(){
        if (_val != null){
            return _val;
        }
        
        long t1 = _data.getDataAtIdx(0).getDateTime().getTimeInMillis();
        long t2;
        long minDiff = 999999999;
        long tempDiff;
        int numDataPoints = _data.length();
        
        for (int i = 1; i<numDataPoints; i++){
            t2 = _data.getDataAtIdx(i).getDateTime().getTimeInMillis();
            tempDiff = t2-t1;
            if (tempDiff < minDiff){
                minDiff = tempDiff;
            }
            t1 = t2;
        }
        
        double answer = minDiff / 1000.0;
        _val = Math.round(answer);
        return _val;
    }
    
    @Override
    public void printAll(){
        System.out.println("\n\tMin time between GPS recordings (sec): " + getValue() +
                    "\n\tMin percentage variance from requested interval: " + getValueInPercent());
            
    }

    @Override
    public OneReport addToMap(OneReport map) {
        map.addValue(Constants.REPORTS_MINT_BTW_SENSOR_RECS, getValue() * 1.0, 
        		"Minimum/smallest time in seconds between actual GPS recordings");
        map.addValue(Constants.REPORTS_MINT_BTW_SENSOR_RECS_AS_PERC, getValueInPercent(), 
        		"Percent deviation of smallest actual time between GPS recordings from sensor interval");
        return map;
    }
}
