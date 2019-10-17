package sensors.data;

import orderedcollection.*;

public class OneCouponsData {
    private final IMJ_OC<AbsDataPoint> _data;
    private final int _couponId;
    
    public OneCouponsData(int cid){
        _couponId = cid;
        _data = new MJ_OC_Factory<AbsDataPoint>().create();
    }
    
    public OneCouponsData(int cid, AbsDataPoint dp){
        _couponId = cid;
        _data = new MJ_OC_Factory<AbsDataPoint>().create();
        _data.add(dp);
    }

    private OneCouponsData(int cid, IMJ_OC<AbsDataPoint> data){
        _couponId = cid;
        _data = data;
    }
    
    public OneCouponsData getDeepCopy(){
    	return new OneCouponsData(_couponId, _data.getDeepCopy());
    }
    
    public void addDataPoint(AbsDataPoint dp){
        _data.add(dp);
    }
    
    public AbsDataPoint getDataAtIdx(int i){
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
    
    public String getDataType() {
    	String s = null;
    	try {
    		s = _data.get(0).getDataType();
    	} 
    	catch (Exception e) {}
    	return s;
    }
}
