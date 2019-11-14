package sensors.data;

import Assert.Assertion;
import orderedcollection.*;

public class SensorDataOfOneType extends MJ_OC<AbsDataPoint> {
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
    
    public String getDataType() {
    	return _type;
    }
    
    @Override
    public SensorDataOfOneType getDeepCopy(){
    	return new SensorDataOfOneType(_data.getDeepCopy());
    }
    
    @Override
    public boolean add(AbsDataPoint dp){
    	String dpType = dp.getDataType();
    	Assertion.test( _type.equals(dpType), "AbsDataPoint type does not match type of this");
        return _data.add(dp);
    }
    
    @Override
    public AbsDataPoint get(int i){
        return _data.get(i);
    }
    
    @Override
    public int size(){
        return _data.size();
    }
    
    @Override
    public AbsDataPoint remove(int idx){
        return _data.remove(idx);
    }
    
    @Override
    public String toString() {
    	return "type: " + _type + " data: " + _data.toString();
    }

	@Override
	public void prepend(AbsDataPoint arg0) {
		_data.prepend(arg0);
	}

	@Override
	public void printAll() {
		_data.printAll();
	}

	@Override
	public void add(int arg0, AbsDataPoint dp) {
    	String dpType = dp.getDataType();
    	Assertion.test( _type.equals(dpType), "AbsDataPoint type does not match type of this");
		_data.add(arg0, dp);
	}

	@Override
	public void clear() {
		_data.clear();
	}

	@Override
	public AbsDataPoint set(int arg0, AbsDataPoint arg1) {
		return _data.set(arg0, arg1);
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return _data.toArray(arg0);
	}
}
