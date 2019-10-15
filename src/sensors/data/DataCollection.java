package sensors.data;

import maps.*;
import orderedcollection.*;

/**
 *
 * @author Maisha Jauernig
 */
public class DataCollection {
	//IMJ_Map<Integer, OneCouponsData> _couponToData;
	IMJ_Map<Integer, IMJ_OC<OneCouponsData>> _couponToData;
    
    public DataCollection() {
        _couponToData = new MJ_Map_Factory<Integer, IMJ_OC<OneCouponsData>>().create();
    }

    public void addCouponAndItsData(int couponId, OneCouponsData data){
    	if ( ! _couponToData.containsKey(couponId) ) {
    		IMJ_OC<OneCouponsData> dataList = new MJ_OC_Factory<OneCouponsData>().create();
    		dataList.add(data);
    		_couponToData.put(couponId, dataList);
    	}
    	else {
    		_couponToData.get(couponId).add(data);
    	}
        
    }
    
    public IMJ_OC<OneCouponsData> getCouponData(int couponId){
        return _couponToData.get(couponId);
    }
    
    public boolean hasCouponEntry(int couponId){
        return _couponToData.containsKey(couponId);
    }
    
    public int numSensorDataCollections(){
        return _couponToData.size();
    }
    
    public int length(){
        return _couponToData.size();
    }
}
