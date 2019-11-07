package sensors.data;

import Assert.Assertion;
import orderedcollection.*;

public class SensorDataOfOneType {
    private final IMJ_OC<AbsDataPoint> _data;
    private String _type;
    
    public SensorDataOfOneType(String type){
        _data = new MJ_OC_Factory<AbsDataPoint>().create();
        _type = type;
    }
    
    public SensorDataOfOneType(AbsDataPoint dp){
        _data = new MJ_OC_Factory<AbsDataPoint>().create();
        _data.add(dp);
        _type = dp.getDataType();
    }

    private SensorDataOfOneType(IMJ_OC<AbsDataPoint> data){
        _data = data;
        _type = data.get(0).getDataType();
    }
    
    public SensorDataOfOneType getDeepCopy(){
    	return new SensorDataOfOneType(_data.getDeepCopy());
    }
    
    public void addDataPoint(AbsDataPoint dp){
    	String dpType = dp.getDataType();
    	Assertion.test( _type.equals(dpType), "AbsDataPoint type does not match type of this");
        _data.add(dp);
    }
    
    public AbsDataPoint getDataAtIdx(int i){
        return _data.get(i);
    }
    
    public int length(){
        return _data.size();
    }
    
    public void deleteItem(int idx){
        _data.remove(idx);
    }
    
    public String getDataType() {
    	return _type;
    }
    
    @Override
    public String toString() {
    	return "type: " + _type + " data: " + _data.toString();
    }
}
