package studysensors.gps;

import studysensors.gps.gpsDeepLayer.OneCouponsGpsData;
import Constants.ConstTags;
import Constants.Constants;
import stats.OneReport;

/**
 *
 * @author Maisha
 */
public class GpsRecordingsWithinGivenPercentOfTimeInterval extends GpsPerformanceEvaluation {
    private final double _per = Constants.PERCENT_ALLOWED_DEVIATION_FROM_SI;
    
    public GpsRecordingsWithinGivenPercentOfTimeInterval
        (OneCouponsGpsData data, double si) {
        super(data, si);
    }

    @Override
    public long getValue() {
        if (_val != null){
            return _val;
        }
        
        long upperBound = Math.round((_sensorInterval + (_per * _sensorInterval)) * 1000);
        long lowerBound = Math.round((_sensorInterval - (_per * _sensorInterval)) * 1000);
        long t1 = _data.getDataAtIdx(0).getDateTime().getTimeInMillis();
        long t2, tempDiff;
        int numDataPoints = _data.length();
        long count = 0;
        
        for (int i = 1; i<numDataPoints; i++){
            t2 = _data.getDataAtIdx(i).getDateTime().getTimeInMillis();
            tempDiff = t2-t1;
            if (tempDiff >= lowerBound && tempDiff <= upperBound){
                count += 1;
            }
            t1 = t2;
        }
        _val = count;
        return _val;
    }
    
    @Override
    public Double getValueInPercent(){
        double per = _val / (double) _data.length() * 100;
        per = Math.round(per * 1d) / 1d;  // this is to get 2 decimal places
        return per;
    }
    
    public int getTotalNumOfGpsRecs(){
        return _data.length();
    }
    
    @Override
    public void printAll(){
        System.out.println("\n\tNum GPS recordings (of " + getTotalNumOfGpsRecs() + 
                    ") within " + _per*100 + "% of requested interval: "  + getValue() +
                    "\n\tPercent of recordings within interval: " + getValueInPercent());
    }

    @Override
    public OneReport addToMap(OneReport map) {
        map.addValue(ConstTags.REPORTS_TOTAL_SENSOR_RECS, getTotalNumOfGpsRecs() * 1.0, ConstTags.REPORTS_T_S_R_TEXT);
        map.addValue(ConstTags.REPORTS_PERC_ALLW_DEV_FRM_SI, _per * 100.0, ConstTags.REPORTS_P_A_D_F_SI_TEXT);
        map.addValue(ConstTags.REPORTS_SENSOR_RECS_WITHIN_DEV, getValue() * 1.0, ConstTags.REPORTS_S_R_W_D_TEXT);
        map.addValue(ConstTags.REPORTS_PERC_SENSOR_RECS_WITHIN_DEV, getValueInPercent(), ConstTags.REPORTS_S_R_P_W_D_TEXT);
        return map;
    }
}
