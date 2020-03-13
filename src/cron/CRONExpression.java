package cron;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

/*
 * Given an ODIN CronString, this class will convert the string to a usable
 * CronExpression, which can call the method getSecondsToFire, to determine
 * its next firing time.
 * 
 * Written by Alekhya Bellam
 * Revised by Anthony McIntosh 3/11/2019
 */
public class CRONExpression {

	private String _minute;
	private String _hour;
	private String _dayOfMonth;
	private String _month;
	private String _dayOfWeek;

	MinuteCompare minuteCompare = null;
	HourCompare hourCompare = null;
	DayOfMonthCompare dayOfMonthCompare= null;
	MonthCompare monthCompare = null;
	DayOfWeekCompare dayOfWeekCompare = null;


	public CRONExpression(String cronExpression) {
		String[] strings = cronExpression.split("\\s+");
		_minute = strings[0];
		_hour = strings[1];
		_dayOfMonth = strings[2];
		_month = strings[3];
		_dayOfWeek = strings[4];

		minuteCompare = new MinuteCompare(_minute);
		hourCompare = new HourCompare(_hour);
		dayOfMonthCompare = new DayOfMonthCompare(_dayOfMonth);
		monthCompare = new MonthCompare(_month);
		dayOfWeekCompare = new DayOfWeekCompare(_dayOfWeek);
	}

	/*
	 * Compares all possibilities for each element of the ODIN CronString to the currentTime, if
	 * all elements of the CronString are satisfied, then the time is a match. Otherwise, when a
	 * mismatch is found, that element is incremented up, while all further elements are reset to
	 * their minValue.
	 * 
	 * Order of precedence: month, day of month, day of week, hour, minute
	 * 
	 * Returns -1 if no matching date was found before the furthestTimeToCheck.
	 * 
	 * Written by Anthony McIntosh 3/11/2019
	 */
	public int getSecondsToFire(DateTime currentDateTime, DateTime furthestTimeToCheck, boolean wasRuleFiredBefore) {

		int secondsToFire = -1;
		long maximumSeconds = Seconds.secondsBetween(currentDateTime, furthestTimeToCheck).getSeconds();
		
		DateTimeConverter nextFiringDateTime;
		
		if (wasRuleFiredBefore || currentDateTime.getSecondOfMinute() != 0) {
			nextFiringDateTime = new DateTimeConverter(currentDateTime.plusMinutes(1).withSecondOfMinute(0));
		} else {
			nextFiringDateTime = new DateTimeConverter(currentDateTime);
		}
		
		int diff = Seconds.secondsBetween(currentDateTime, nextFiringDateTime.getJodaDateTime()).getSeconds();

		while (Seconds.secondsBetween(currentDateTime, nextFiringDateTime.getJodaDateTime()).getSeconds() <= maximumSeconds) {
			if(!monthCompare.testTimeMatches(nextFiringDateTime.getMonthOfYear())) {
				nextFiringDateTime.plusMonths(1);
				nextFiringDateTime.withDayOfMonth(1);
				nextFiringDateTime.withHourOfDay(0);
				nextFiringDateTime.withMinuteOfHour(0);
				continue;
			}
			if(!dayOfMonthCompare.testTimeMatches(nextFiringDateTime.getDayOfMonth())) {
				nextFiringDateTime.plusDays(1);
				nextFiringDateTime.withHourOfDay(0);
				nextFiringDateTime.withMinuteOfHour(0);
				continue;
			}
			if(!dayOfWeekCompare.testTimeMatches(nextFiringDateTime.getDayOfWeek())) {
				nextFiringDateTime.plusDays(1);
				nextFiringDateTime.withHourOfDay(0);
				nextFiringDateTime.withMinuteOfHour(0);
				continue;
			}
			if(!hourCompare.testTimeMatches(nextFiringDateTime.getHourOfDay())) {
				nextFiringDateTime.plusHours(1);
				nextFiringDateTime.withMinuteOfHour(0);
				continue;
			}
			if(!minuteCompare.testTimeMatches(nextFiringDateTime.getMinuteOfHour())) {
				nextFiringDateTime.plusMinutes(1);
				continue;
			}
			secondsToFire = Seconds.secondsBetween(currentDateTime, nextFiringDateTime.getJodaDateTime()).getSeconds();
			break;
		}
		return secondsToFire;
	}
	
	public MinuteCompare getMinuteCompare( ) {
		return this.minuteCompare;
	}
	
	public HourCompare getHourCompare( ) {
		return this.hourCompare;
	}
	
	public DayOfMonthCompare getDayOfMonthCompare( ) {
		return this.dayOfMonthCompare;
	}
	
	public MonthCompare getMonthCompare( ) {
		return this.monthCompare;
	}
	
	public DayOfWeekCompare getDayOfWeekCompare( ) {
		return this.dayOfWeekCompare;
	}

}