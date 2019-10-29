package reports.rules;

import reports.rules.whileAt.WhileAtRuleParams;
import sensors.gps.GpsCoordinate;

/**
 *
 * @author Maisha Jauernig
 */
public class PredicateInLocRadius extends Predicate {
    private final GpsCoordinate _coordReq;
    private final double _dist;
    
    public PredicateInLocRadius(OneRule r){
    	WhileAtRuleParams param = (WhileAtRuleParams) r.getParams();
        _coordReq = new GpsCoordinate(param.getLat(), param.getLon());
        _dist = param.getDist();
    }
    
    public PredicateInLocRadius(GpsCoordinate locReq, double dist){
        _coordReq = locReq;
        _dist = dist;
    }
    
    @Override
    public boolean test(GpsCoordinate c){
        return getCoordsDistance(_coordReq, c) <= _dist;
    }
    
    private double getCoordsDistance(GpsCoordinate a, GpsCoordinate b){
        final int R = 6371; // Radius of the earth
        double lat1 = a.getLat(), lon1 = a.getLon();
        double lat2 = b.getLat(), lon2 = b.getLon();
        
        // Using Haversine method as base to get distance, ignoring altitude
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double x = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double y = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
        
        double distance = R * y * 1000; // convert to meters
        
        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
