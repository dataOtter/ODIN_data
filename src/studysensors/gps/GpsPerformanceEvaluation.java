package studysensors.gps;

import studysensors.gps.gpsDeepLayer.OneCouponsGpsData;
import stats.OneReport;

/**
 *
 * @author Maisha
 */
public abstract class GpsPerformanceEvaluation{
    
    final OneCouponsGpsData _data;
    final int _couponId;
    Long _val = null;
    final double _sensorInterval;

    public GpsPerformanceEvaluation(OneCouponsGpsData data, double si) {
        _data = data;
        _couponId = _data.getCouponId();
        _sensorInterval = si;     
    }
    
    public void printTimeInterval(){
        System.out.println("\nResearcher requested time interval: " + Math.round(_sensorInterval));
    }
    
    public Double getTimeInterval(){
        return _sensorInterval;
    }
    
    public abstract long getValue();
    
    public abstract void printAll();
    
    public abstract OneReport addToMap(OneReport map);
    
    public Double getValueInPercent(){
        long rawNum = getValue();
        double per = rawNum / _sensorInterval * 100;
        per = Math.round(per * 100d) / 100d;  // this is to get 2 decimal places
        return per;
    }
}