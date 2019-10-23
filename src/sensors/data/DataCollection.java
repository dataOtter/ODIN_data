package sensors.data;

import maps.*;
import orderedcollection.*;

/**
 *
 * @author Maisha Jauernig
 */
public class DataCollection {
	//IMJ_Map<Integer, OneCouponsData> _couponToData;
	IMJ_Map<Integer, IMJ_OC<SensorDataOfOneType>> _couponToData;
    
    public DataCollection() {
        _couponToData = new MJ_Map_Factory<Integer, IMJ_OC<SensorDataOfOneType>>().create();
    }

    public void addCouponAndItsData(int couponId, SensorDataOfOneType data){
    	if ( ! _couponToData.containsKey(couponId) ) {
    		IMJ_OC<SensorDataOfOneType> dataList = new MJ_OC_Factory<SensorDataOfOneType>().create();
    		dataList.add(data);
    		_couponToData.put(couponId, dataList);
    	}
    	else {
    		_couponToData.get(couponId).add(data);
    	}
    }
    
    public IMJ_OC<SensorDataOfOneType> getCouponData(int couponId) {
        return _couponToData.get(couponId);
    }
    
    public SensorDataOfOneType getCouponDataOfType(int couponId, String type) {
    	IMJ_OC<SensorDataOfOneType> allData = _couponToData.get(couponId);
    	for (SensorDataOfOneType d: allData) {
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
    		IMJ_OC<SensorDataOfOneType> allCouponData = _couponToData.get(cid);
    		
    		// if the coupon has data of the desired type, add the given data point
        	String type = data.getDataType();
    		for (SensorDataOfOneType d: allCouponData) {
        		if (d.getDataType().equals(type)) {
        			d.addDataPoint(data);
        			return;
        		}
        	}
    		// if the coupon does not have data of the desired type
    		// add a new list of data with the given data point
        	allCouponData.add(new SensorDataOfOneType(data));
    	}
    	
    	// if the coupon does not have an entry yet, make one
    	else {
        	IMJ_OC<SensorDataOfOneType> allCouponData = new MJ_OC_Factory<SensorDataOfOneType>().create();
        	allCouponData.add(new SensorDataOfOneType(data));
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
