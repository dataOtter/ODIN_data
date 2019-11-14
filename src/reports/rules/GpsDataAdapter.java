package reports.rules;

import java.util.Calendar;

import sensors.data.GpsDataPoint;
import sensors.data.SensorDataOfOneType;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsDataAdapter extends AbsDataAdapter {
    
    public GpsDataAdapter(SensorDataOfOneType d, double si, double minT) {
    	super(d, si, minT);
    }
    
    public double getNextStartTime(double t, Predicate p) { 
    	boolean stillThere = true;
    	if ( t == 0.0) {
    		stillThere = false;
    	}
    	
        for (int i = this.getIdxOfDataAtTime(t); i<_data.size(); i++) {
        	GpsDataPoint g = (GpsDataPoint) _data.get(i);
        	
            if ( p.test(g.getGpsCoord()) && ! stillThere) {
                Calendar d = g.getDateTime();
                
                if (d != null) {
                    return d.getTimeInMillis() / 1000.0;
                }
            }
            else if ( ! p.test(g.getGpsCoord() )) {
            	stillThere = false;
            }
        }
        return 0.0;
    }

}
