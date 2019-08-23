package studysensors.gps.gpsDeepLayer;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsCoordsClusterCenterCalc implements IGpsCoordsClusterObserver {
    
    Double _centerLat, _centerLon;
    int _numCoords;
    
    public GpsCoordsClusterCenterCalc(){
        _centerLat = 0.0;
        _centerLon = 0.0;
        _numCoords = 0;
    }
    
    @Override
    public void notifyCoordAdded(GpsCoordinate c) {
        _centerLat = ( (double) _numCoords * _centerLat + c.getLat() ) / ( (double) _numCoords + 1.0 );
        _centerLon = ( (double) _numCoords * _centerLon + c.getLon() ) / ( (double) _numCoords + 1.0 );
        _numCoords++;
    }
    
    public GpsCoordinate getCenter(){
        return new GpsCoordinate(_centerLat, _centerLon);
    }
    
}
