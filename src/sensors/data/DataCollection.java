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
    
    public IMJ_OC<OneCouponsData> getCouponData(int couponId) {
        return _couponToData.get(couponId);
    }
    
    public OneCouponsData getCouponDataOfType(int couponId, String type) {

    	//at this point not all data has been entered, and there seems to be 2 of gps and bt but no beacon
    	IMJ_OC<OneCouponsData> allData = _couponToData.get(couponId);
    	for (OneCouponsData d: allData) {
    		if (d.getDataType().equals(type)) {
    			return d;
    		}
    	}
    	return null;
    }
    
    /**
     * Adds the given AbsDataPoint to this DataCollection as appropriate
     * @param cid - ID as int of the coupon whose data point to add
     * @param data - concrete instance of AbsDataPoint to add to this DataCollection
     */
    public void addDataPointToCouponData(int cid, AbsDataPoint data) {
    	// if the coupon already has an entry i.e. already has some data 
    	if (_couponToData.containsKey(cid)) {
    		IMJ_OC<OneCouponsData> allCouponData = _couponToData.get(cid);
    		
    		// if the coupon has data of the desired type, add the given data point
        	String type = data.getDataType();
    		for (OneCouponsData d: allCouponData) {
        		if (d.getDataType().equals(type)) {
        			d.addDataPoint(data);
        			return;
        		}
        	}
    		// if the coupon does not have data of the desired type
    		// add a new list of data with the given data point
        	allCouponData.add(new OneCouponsData(cid, data));
    	}
    	
    	// if the coupon does not have an entry yet, make one
    	else {
        	IMJ_OC<OneCouponsData> allCouponData = new MJ_OC_Factory<OneCouponsData>().create();
        	allCouponData.add(new OneCouponsData(cid, data));
        	_couponToData.put(cid, allCouponData);
    	}
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
