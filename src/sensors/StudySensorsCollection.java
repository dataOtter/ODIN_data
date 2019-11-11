package sensors;

import orderedcollection.*;

/**
 * A collection of StudySensor
 * @author Maisha Jauernig
 */
public class StudySensorsCollection extends MJ_OC<StudySensor> {
    private final IMJ_OC<StudySensor> _sensors;
    
    /**
     * @param sensors
     */
    public StudySensorsCollection(IMJ_OC<StudySensor> sensors) {
    	_sensors = sensors;
    }
    
    /**
     * @return an IMJ_OC<Integer> containing all sensor IDs
     */
    public IMJ_OC<Integer> getSensorIds() {
    	IMJ_OC<Integer> sids = new MJ_OC_Factory<Integer>().create();
    	for (int i = 0; i<_sensors.size(); i++) {
    		sids.add(_sensors.get(i).getSensorId());
    	}
    	return sids;
    }
    
    /**
     * @param studyid - ID of the study for which to get all enabled sensors
     * @return a StudySensorsCollection containing all sensors enabled in the given study
     */
    public StudySensorsCollection getSensorsByStudyId(int studyid) {
    	IMJ_OC<StudySensor> sen = new MJ_OC_Factory<StudySensor>().create();
    	for (StudySensor s: _sensors) {
    		if (s.getStudyId() == studyid) {
    			sen.add(s);
    		}
    	}
    	return new StudySensorsCollection(sen);
    }
    
    /**
     * @param sensorid - ID of the sensor for which to get the sensor interval
     * @return the sensor interval for the given sensor as a Double
     */
    public Double getSensorInterval(int sensorid){
    	for (StudySensor sen: _sensors) {
    		int id = sen.getSensorId();
    		if (id == sensorid) {
    			return sen.getParams().getTimeInterval();
    		}
    	}
    	return null;
    }

	@Override
	public IMJ_OC<StudySensor> getDeepCopy() {
		return _sensors.getDeepCopy();
	}

	@Override
	public void prepend(StudySensor arg0) {
		_sensors.prepend(arg0);
	}

	@Override
	public void printAll() {
		_sensors.printAll();
	}

	@Override
	public boolean add(StudySensor e) {
		return _sensors.add(e);
	}

	@Override
	public void add(int index, StudySensor element) {
		_sensors.add(index, element);
	}

	@Override
	public void clear() {
		_sensors.clear();
	}

	@Override
	public StudySensor get(int index) {
		return _sensors.get(index);
	}

	@Override
	public StudySensor remove(int index) {
		return _sensors.remove(index);
	}

	@Override
	public StudySensor set(int index, StudySensor element) {
		return _sensors.set(index, element);
	}

	@Override
	public int size() {
		return _sensors.size();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return _sensors.toArray(a);
	}
}
