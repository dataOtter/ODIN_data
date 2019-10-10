package sensors.gps;

/**
 *
 * @author Maisha Jauernig
 */
public interface IGpsCoordsClusterObserver {
    void notifyCoordAdded(GpsCoordinate c);
}
