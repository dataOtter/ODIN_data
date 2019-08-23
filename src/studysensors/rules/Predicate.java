package studysensors.rules;

import studysensors.gps.gpsDeepLayer.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public abstract class Predicate {
    public abstract boolean test(GpsCoordinate c);
}
