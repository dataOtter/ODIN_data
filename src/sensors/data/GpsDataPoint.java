package sensors.data;

import java.util.Calendar;

import sensors.gps.GpsCoordinate;

/**
 * Contains the information for one GPS data point
 * @author Maisha Jauernig
 */
public class GpsDataPoint extends AbsDataPoint {
    private final GpsCoordinate _coord;
    private final static int _sensorId = 12;
    
    public GpsDataPoint(Calendar dateTime, double lat, double lon) {
    	super(dateTime, _sensorId);
        _coord = new GpsCoordinate(lat, lon);
    }

    /**
     * @return the GPS coordinates of this GpsDataPoint as a GpsCoordinate
     */
    public GpsCoordinate getGpsCoord() {
    	if (this.equals(null)) {
    		return null;
    	}
    	return _coord;
    }
    
    /**
     * @return the latitude of this GpsDataPoint as a Double
     */
    public Double getLat(){
        return _coord.getLat();
    } 
    
    /**
     * @return the longitude of this GpsDataPoint as a Double
     */
    public Double getLon(){
        return _coord.getLon();
    } 
}
