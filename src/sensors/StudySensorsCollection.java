package sensors;

import orderedcollection.*;

/**
 *
 * @author Maisha Jauernig
 */
public class StudySensorsCollection {
    private final IMJ_OC<StudySensor> _sensors;
    
    public StudySensorsCollection(IMJ_OC<StudySensor> sensors) {
    	_sensors = sensors;
    }
    
    public IMJ_OC<Integer> getSensorIds() {
    	IMJ_OC<Integer> sids = new MJ_OC_Factory<Integer>().create();
    	for (int i = 0; i<_sensors.size(); i++) {
    		sids.add(_sensors.get(i).getSensorId());
    	}
    	return sids;
    }
    
    public StudySensorsCollection getSensorsByStudyId(int studyid) {
    	IMJ_OC<StudySensor> sen = new MJ_OC_Factory<StudySensor>().create();
    	for (StudySensor s: _sensors) {
    		if (s.getStudyId() == studyid) {
    			sen.add(s);
    		}
    	}
    	return new StudySensorsCollection(sen);
    }
    
    public Double getSensorInterval(int sensorid){
    	for (StudySensor sen: _sensors) {
    		int id = sen.getSensorId();
    		if (id == sensorid) {
    			return sen.getParams().getTimeInterval();
    		}
    	}
    	return null;
    }
	
	public int getLength() {
		return _sensors.size();
	}
	
	public StudySensor getStudySensorAtIdx(int i) {
		return _sensors.get(i);
	}
}
