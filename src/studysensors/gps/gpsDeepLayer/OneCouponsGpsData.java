package studysensors.gps.gpsDeepLayer;

import orderedcollection.*;

/**
 *
 * @author Maisha
 */
public class OneCouponsGpsData {
    private final IMJ_OC<GpsDataPoint> _data;
    private final int _couponId;
    
    public OneCouponsGpsData(int cid){
        _couponId = cid;
        _data = new MJ_OC_Factory<GpsDataPoint>().create();
    }
    
    private OneCouponsGpsData(int cid, IMJ_OC<GpsDataPoint> data){
        _couponId = cid;
        _data = data;
    }
    
    public OneCouponsGpsData getDeepCopy(){
    	return new OneCouponsGpsData(_couponId, _data.getDeepCopy());
    }
    
    public void addGpsDataPoint(GpsDataPoint gdp){
        _data.add(gdp);
    }
    
    public GpsDataPoint getDataAtIdx(int i){
        return _data.get(i);
    }
    
    public int getCouponId(){
        return _couponId;
    }
    
    public int length(){
        return _data.size();
    }
    
    public void deleteItem(int idx){
        _data.remove(idx);
    }
}
