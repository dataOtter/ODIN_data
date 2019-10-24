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
	private final PredicateInLocRadius _pred;
	
	public LocFilterParams(GpsCoordinate loc, double dist) {
		super(Constants.FILTER_LOCATION);
		_pred = new PredicateInLocRadius(loc, dist);
	}
	
	public LocFilterParams() {
		super(Constants.FILTER_LOCATION);
		_pred = new PredicateInLocRadius(new GpsCoordinate(41.819484, -97.6990014), 10.0);
	}

	@Override
	public boolean testInput(IMJ_OC<AbsFilterInput> inputs) {
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
