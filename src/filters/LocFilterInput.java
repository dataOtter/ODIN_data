/**
 * 
 */
package filters;

import constants.Constants;
import reports.rules.GpsDataAdapter;
import sensors.data.GpsDataPoint;
import sensors.gps.GpsCoordinate;

/**
 * @author Maisha Jauernig
 *
 */
public class LocFilterInput extends AbsFilterInput {
	private GpsCoordinate _locNow;
	
	public LocFilterInput(double timeNowInSecs, GpsDataAdapter ad) {
		super(Constants.FILTER_LOCATION, timeNowInSecs);
		_locNow = null;
		if (ad != null) {
			_locNow = ( (GpsDataPoint) ad.getDataPointAtTime(timeNowInSecs) ).getGpsCoord();
		}
	}

	/**
	 * @return the _locNow
	 */
	public GpsCoordinate getLocNow() {
		return _locNow;
	}
}
