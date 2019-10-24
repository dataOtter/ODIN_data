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
	private final double _startTime;
	private final double _endTime;
	
	/**
	 * @param start - start of time frame as time of day in seconds 
	 * @param end - end of time frame as time of day in seconds 
	 */
	public TimeFilterParams(double start, double end) {  // times come in as hour of day, at least in ui, so convert to sec of day
		super(Constants.FILTER_TIME);
		_startTime = start * 60.0 * 60.0;
		_endTime = end * 60.0 * 60.0;
	}
	
	public TimeFilterParams() {
		super(Constants.FILTER_TIME);
		_startTime = 52200;  // 14.5
		_endTime = 68400;  // 19
	}

	@Override
	public boolean testInput(IMJ_OC<AbsFilterInput> inputs) {
		double timeNowSecs = 0.0;
		for (AbsFilterInput i: inputs) {
			if (i.getType().equals(this.getType())) {
				timeNowSecs = ((TimeFilterInput) i).getTimeNowSecs();
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
