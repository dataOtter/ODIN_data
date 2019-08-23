package studysensors.gps;

import studysensors.Constants;
import studysensors.gps.gpsDeepLayer.OneCouponsGpsData;
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
        map.addValue(Constants.REPORTS_TOTAL_SENSOR_RECS, getTotalNumOfGpsRecs() * 1.0, "Total GPS recordings");
        map.addValue(Constants.PERC_ALLOWED_DEV_FROM_SI, _per * 100.0, 
        		"Maximum percent deviation from requested sensor interval");
        map.addValue(Constants.REPORTS_SENSOR_RECS_WITHIN_DEV, getValue() * 1.0,
        		"Number of GPS recordings within given allowed deviation of sensor interval");
        map.addValue(Constants.REPORTS_SENSOR_RECS_PERC_WITHIN_DEV, getValueInPercent(), 
        		"Percent of GPS recordings within given allowed deviation of sensor interval");
        return map;
    }
}
