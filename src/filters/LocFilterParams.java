/**
 * 
 */
package filters;

import constants.Constants;
import orderedcollection.IMJ_OC;
import reports.rules.PredicateInLocRadius;
import sensors.gps.GpsCoordinate;

/**
 * @author Maisha Jauernig
 *
 */
public class LocFilterParams extends AbsFilterParams {
	private PredicateInLocRadius _pred;
	
	public LocFilterParams() {
		super(Constants.FILTER_LOCATION);
		_pred = new PredicateInLocRadius(new GpsCoordinate(41.819484, -97.6990014), 10.0);
	}
	
	public LocFilterParams(String params) {
		super(Constants.FILTER_LOCATION);
		_pred = null;
		double dist = 0.0, lat = 0.0, lon = 0.0;
		
		String[] p = params.split(",");
		for (String s: p) {
			double val = Double.parseDouble(s.substring(s.indexOf(':')+1));
			if (s.contains(Constants.FILTER_LOCATION_DIST)) {
				dist = val;
			}
			else if (s.contains(Constants.FILTER_LOCATION_LAT)){
				lat = val;
			}
			else  if (s.contains(Constants.FILTER_LOCATION_LON)){
				lon = val;
			}
		}
		_pred = new PredicateInLocRadius(new GpsCoordinate(lat, lon), dist);
	}

	@Override
	public boolean testInputs(IMJ_OC<AbsFilterInput> inputs) {
		GpsCoordinate locNow = null;
		for (AbsFilterInput i: inputs) {
			if (i.getType().equals(this.getType())) {
				locNow = ((LocFilterInput) i).getLocNow();
				break;
			}
		}
		return _pred.test(locNow);
	}
}
