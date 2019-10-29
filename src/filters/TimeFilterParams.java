/**
 * 
 */
package filters;

import java.util.Calendar;
import java.util.GregorianCalendar;

import constants.Constants;
import orderedcollection.IMJ_OC;

/**
 * @author Maisha Jauernig
 *
 */
public class TimeFilterParams extends AbsFilterParams {
	private double _startTime;  // start of time frame as time of day in seconds 
	private double _endTime;  // end of time frame as time of day in seconds
	
	/**
	 * @param params - parameters of the filter 
	 */
	public TimeFilterParams(String params) {  // times come in as hour of day, so convert to sec of day
		super(Constants.FILTER_TIME);
		_startTime = 0.0;
		_endTime = 0.0;
		
		String[] p = params.split(",");
		for (String s: p) {
			double t = Double.parseDouble(s.substring(s.indexOf(':')+1)) * 60.0 * 60.0;
			if (s.contains(Constants.FILTER_TIME_START)) {
				_startTime = t;
			}
			else if (s.contains(Constants.FILTER_TIME_END)) {
				_endTime = t;
			}
		}
	}

	@Override
	public boolean testInput(IMJ_OC<AbsFilterInput> inputs) {
		double timeNowSecs = 0.0;
		for (AbsFilterInput i: inputs) {
			if (i.getType().equals(this.getType())) {
				timeNowSecs = i.getTimeNowSecs();
				break;
			}
		}
		
		if (timeNowSecs == 0.0) { return true; }
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis((long)timeNowSecs * 1000);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		double midnightSecs = cal.getTimeInMillis() / 1000.0;
		
		double timeOfDaySecs = timeNowSecs - midnightSecs;

		if (timeOfDaySecs >= _startTime && timeOfDaySecs <= _endTime) {
			return true;
		}
		return false;
	}
}
