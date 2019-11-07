package sensors.data;

import java.util.Calendar;

import sensors.gps.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsDataPoint extends AbsDataPoint {
    private final GpsCoordinate _coord;
    private final static int _sensorId = 12;
    
    public GpsDataPoint(Calendar dateTime, double lat, double lon) {
    	super(dateTime, _sensorId);
        _coord = new GpsCoordinate(lat, lon);
    }
    
    public GpsCoordinate getGpsCoord() {
    	if (this.equals(null)) {
    		return null;
    	}
    	return _coord;
    }
    
    public Double getLat(){
        return _coord.getLat();
    } 
    
    public Double getLon(){
        return _coord.getLon();
    } 
}
