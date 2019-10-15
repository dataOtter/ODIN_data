package sensors.data;

import orderedcollection.*;

public class OneCouponsData {
    private final IMJ_OC<AbsDataPoint> _data;
    private final int _couponId;
    
    public OneCouponsData(int cid){
        _couponId = cid;
        _data = new MJ_OC_Factory<AbsDataPoint>().create();
    }

    private OneCouponsData(int cid, IMJ_OC<AbsDataPoint> data){
        _couponId = cid;
        _data = data;
    }
    
    public OneCouponsData getDeepCopy(){
    	return new OneCouponsData(_couponId, _data.getDeepCopy());
    }
    
    public void addDataPoint(AbsDataPoint gdp){
        _data.add(gdp);
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
}
