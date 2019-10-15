package reports.sensors;

import constants.ConstTags;
import reports.OneReport;
import sensors.data.OneCouponsData;

/**
 *
 * @author Maisha Jauernig
 */
public abstract class AbsSensorPerformanceEval{
    
    protected final OneCouponsData _data;
    protected final int _couponId;
    protected Long _val = null;
    protected final double _sensorInterval;

    public AbsSensorPerformanceEval(OneCouponsData data, double si) {
        _data = data;
        _couponId = _data.getCouponId();
        _sensorInterval = si;     
    }
    
    public void printTimeInterval(){
        System.out.println("\n" + ConstTags.REPORTS_S_I_TEXT + ": " + Math.round(_sensorInterval));
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
