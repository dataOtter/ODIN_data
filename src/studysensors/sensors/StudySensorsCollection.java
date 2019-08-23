package studysensors.sensors;

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
    	for (int i = 0; i<_sensors.length(); i++) {
    		sids.append(_sensors.getItem(i).getSensorId());
    	}
    	return sids;
    }
    
    public StudySensorsCollection getSensorsByStudyId(int studyid) {
    	IMJ_OC<StudySensor> sen = new MJ_OC_Factory<StudySensor>().create();
    	StudySensor s;
    	for (int i = 0; i<_sensors.length(); i++) {
    		s = _sensors.getItem(i);
    		if (s.getStudyId() == studyid) {
    			sen.append(s);
    		}
    	}
    	return new StudySensorsCollection(sen);
    }
    
    public Double getSensorInterval(int sensorid){
    	StudySensor sen;
    	for (int i = 0; i<_sensors.length(); i++) {
    		sen = _sensors.getItem(i);
    		int id = sen.getSensorId();
    		if (id == sensorid) {
    			return sen.getParams().getInterval();
    		}
    	}
    	return null;
    }
	
	public int getLength() {
		return _sensors.length();
	}
	
	public StudySensor getStudySensorAtIdx(int i) {
		return _sensors.getItem(i);
	}
}
