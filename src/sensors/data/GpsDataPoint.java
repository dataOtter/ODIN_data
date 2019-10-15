package sensors.data;

import java.util.Calendar;

import sensors.gps.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsDataPoint extends AbsDataPoint {
    private final GpsCoordinate _coord;
    
    public GpsDataPoint(Calendar dateTime, double lat, double lon) {
    	super(dateTime);
        _coord = new GpsCoordinate(lat, lon);
    }
    
    public GpsCoordinate getGpsCoord() {
    	return _coord;
    }
    
    public Double getLat(){
        return _coord.getLat();
    } 
    
    public Double getLon(){
        return _coord.getLon();
    } 
}
