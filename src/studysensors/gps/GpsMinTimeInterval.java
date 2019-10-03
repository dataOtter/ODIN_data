package studysensors.gps;

import studysensors.gps.gpsDeepLayer.OneCouponsGpsData;
import Constants.ConstTags;
import stats.OneReport;

/**
 *
 * @author Maisha Jauernig
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
        System.out.println("\n\t" + ConstTags.REPORTS_MIN_B_S_R_TEXT + ": " + getValue() +
                    "\n\t" + ConstTags.REPORTS_MIN_B_S_R_A_P_TEXT + ": " + getValueInPercent());
            
    }

    @Override
    public OneReport addToMap(OneReport map) {
        map.addValue(ConstTags.REPORTS_MINT_BTW_SENSOR_RECS, getValue() * 1.0, ConstTags.REPORTS_MIN_B_S_R_TEXT);
        map.addValue(ConstTags.REPORTS_MINT_BTW_SENSOR_RECS_AS_PERC, getValueInPercent(), ConstTags.REPORTS_MIN_B_S_R_A_P_TEXT);
        return map;
    }
}
