package reports.sensors.gps;

import constants.ConstTags;
import reports.OneReport;
import sensors.gps.OneCouponsGpsData;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsMaxTimeInterval extends AbsGpsPerformanceEval {
    
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
        String message = "\n\t" + ConstTags.REPORTS_MAX_B_S_R_TEXT + ": " + maxGap;
        if (maxGap > (5 * this._sensorInterval)){
            message += " (" + maxGap/60 + " mins)";
        }
        if (maxGap/60 > 120){
            message += " (" + maxGap/60/60 + " hours)";
        }
        message += "\n\t" + ConstTags.REPORTS_MAX_B_S_R_A_P_TEXT+ ": " + this.getValueInPercent();
        System.out.println(message);
    }

    @Override
    public OneReport addToMap(OneReport map) {
        map.addValue(ConstTags.REPORTS_MAXT_BTW_SENSOR_RECS, getValue() * 1.0, ConstTags.REPORTS_MAX_B_S_R_TEXT);
        map.addValue(ConstTags.REPORTS_MAXT_BTW_SENSOR_RECS_AS_PERC, getValueInPercent(), ConstTags.REPORTS_MAX_B_S_R_A_P_TEXT);
        return map;
    }
}
