/**
 * 
 */
package filters;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Maisha Jauernig
 *
 */
public class FilterTime {
	private double _startTime;
	private double _endTime;
	
	public static void main(String[] args) {
		FilterTime fil = new FilterTime(52200000, 61200000);  // 14.5 - 17
		FilterTime fil2 = new FilterTime(54000000, 61200000);  // 15 - 17

		System.out.println(fil.checkFilterCondition(new GregorianCalendar().getTimeInMillis()/1000));
		System.out.println(fil2.checkFilterCondition(new GregorianCalendar().getTimeInMillis()/1000));
	}
	
	/**
	 * @param start - start of time frame as time of day in milliseconds 
	 * @param end - end of time frame as time of day in milliseconds 
	 */
	public FilterTime(double start, double end) {
		_startTime = start;
		_endTime = end;
	}
	
	public FilterTime() {
		_startTime = 52200000;  // 14.5
		_endTime = 68400000;  // 19
	}
	
	public boolean checkFilterCondition(double time) {
		long timeMillis = (long) (time * 1000);
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(timeMillis);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		double midnight = cal.getTimeInMillis();
		
		double timeOfDay = timeMillis - midnight;
		//System.out.println(timeOfDay);
		//System.out.println(_startTime);
		//System.out.println(_endTime);
		if (timeOfDay >= _startTime && timeOfDay <= _endTime) {
			return true;
		}
		return false;
	}
}
