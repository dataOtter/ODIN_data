package reports.sensors;

import constants.ConstTags;
import constants.Constants;
import reports.OneReport;
import sensors.data.SensorDataOfOneType;

/**
 *
 * @author Maisha Jauernig
 */
public class SensorRecordingsWithinGivenPercentOfTimeInterval extends AbsSensorPerformanceEval {
    private final double _per = Constants.PERCENT_ALLOWED_DEVIATION_FROM_SI;
    
    public SensorRecordingsWithinGivenPercentOfTimeInterval(SensorDataOfOneType data, double si) {
        super(data, si);
    }

    @Override
    protected long getValue() {
        if (_val != null){
            return _val;
        }
        
        long upperBound = Math.round((_sensorInterval + (_per * _sensorInterval)) * 1000);
        long lowerBound = Math.round((_sensorInterval - (_per * _sensorInterval)) * 1000);
        long t1 = _data.get(0).getDateTime().getTimeInMillis();
        int numDataPoints = _data.size();
        long count = 1;
        
        for (int i = 1; i<numDataPoints; i++){
            long t2 = _data.get(i).getDateTime().getTimeInMillis();
            long tempDiff = t2-t1;
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
        double per = _val / (double) _data.size() * 100;
        per = Math.round(per * 1d) / 1d;  // this is to get 2 decimal places
        return per;
    }
    
    private int getTotalNumOfSensorRecs(){
        return _data.size();
    }
    
    @Override
    public void printAll(){
        System.out.println("\n\tNum sensor recordings (of " + getTotalNumOfSensorRecs() + 
                    ") within " + _per*100 + "% of requested interval: "  + getValue() +
                    "\n\tPercent of recordings within interval: " + getValueInPercent());
    }

    @Override
    public OneReport addToMap(OneReport map) {
        map.addValue(ConstTags.REPORTS_TOTAL_SENSOR_RECS, getTotalNumOfSensorRecs() * 1.0, ConstTags.REPORTS_T_S_R_TEXT);
        map.addValue(ConstTags.REPORTS_PERC_ALLW_DEV_FRM_SI, _per * 100.0, ConstTags.REPORTS_P_A_D_F_SI_TEXT);
        map.addValue(ConstTags.REPORTS_SENSOR_RECS_WITHIN_DEV, getValue() * 1.0, ConstTags.REPORTS_S_R_W_D_TEXT);
        map.addValue(ConstTags.REPORTS_PERC_SENSOR_RECS_WITHIN_DEV, getValueInPercent(), ConstTags.REPORTS_S_R_P_W_D_TEXT);
        return map;
    }
}
