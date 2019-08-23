package studysensors.gps;

import studysensors.gps.gpsDeepLayer.OneCouponsGpsData;
import maps.*;

/**
 *
 * @author Maisha
 */
public final class GpsDataCollection {
    IMJ_Map<Integer, OneCouponsGpsData> _couponToData;
    
    public GpsDataCollection() {
        _couponToData = new MJ_Map_Factory<Integer, OneCouponsGpsData>().create();
    }

    public void addCouponAndItsData(int couponId, OneCouponsGpsData data){
        _couponToData.add(couponId, data);
    }
    
    public OneCouponsGpsData getCouponData(int couponId){
        return _couponToData.getValueOfKey(couponId);
    }
    
    public boolean hasCouponEntry(int couponId){
        return _couponToData.contains(couponId);
    }
    
    public int length(){
        return _couponToData.length();
    }
}
