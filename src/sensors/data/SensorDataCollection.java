package sensors.data;

import maps.*;
import orderedcollection.*;

/**
 * This class contains all sensor data for all coupons
 * @author Maisha Jauernig
 */
public class SensorDataCollection {
	IMJ_Map<Integer, IMJ_OC<SensorDataOfOneType>> _couponToData;
    
    public SensorDataCollection() {
        _couponToData = new MJ_Map_Factory<Integer, IMJ_OC<SensorDataOfOneType>>().create();
    }

    /**
     * @param couponId - ID of the coupon to/for which to add the given data
     * @param data - SensorDataOfOneType to add 
     */
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
    
    /**
     * @param couponId - ID of the coupon for which to get all data, as an int
     * @return the data associated with the given coupon as an IMJ_OC<SensorDataOfOneType>
     */
    public IMJ_OC<SensorDataOfOneType> getCouponData(int couponId) {
        return _couponToData.get(couponId);
    }
    
    /**
     * @param couponId - ID of the coupon for which to get all data of the given type, as an int
     * @param type - type of sensor data, found in Constants, for which the get all data for the given coupon ID
     * @return the data associated with the given coupon and data type as SensorDataOfOneType 
     */
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
     * Adds the given AbsDataPoint to this SensorDataCollection as appropriate
     * @param cid - ID as int of the coupon whose data point to add
     * @param data - concrete instance of AbsDataPoint to add to this SensorDataCollection
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
    
    /**
     * @param couponId - ID of a coupon for which to check if it exists in this SensorDataCollection
     * @return boolean whether or not the given coupon exists in this SensorDataCollection
     */
    public boolean hasCouponEntry(int couponId){
        return _couponToData.containsKey(couponId);
    }
    
    /**
     * @return the number of coupon IDs for which there are sensor data entries 
     */
    public int numSensorDataCollections(){
        return _couponToData.size();
    }
    
    /**
     * @return the number of coupon to data entries in this SensorDataCollection
     */
    public int length(){
        return _couponToData.size();
    }
}
