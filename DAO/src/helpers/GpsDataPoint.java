package helpers;

import java.util.Calendar;
import helpersHelpers.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsDataPoint {
    private final Calendar _dateTime;
    private final GpsCoordinate _coord;
    
    public GpsDataPoint(Calendar dateTime, double lat, double lon) {
        _dateTime = dateTime;
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
    
    public Calendar getDateTime(){
        return _dateTime;
    }
    
}
