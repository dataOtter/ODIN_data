package reports.rules;

import sensors.gps.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public abstract class Predicate {
    public abstract boolean test(GpsCoordinate c);
}
