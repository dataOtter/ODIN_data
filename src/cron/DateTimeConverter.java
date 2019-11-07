package cron;

import org.joda.time.DateTime;

/*
 * The DateTimeConverter performs similarly to an Adapter for the JodaDateTime class.
 * JodaDateTime is non-extendable, so this is not a true Adapter. The converter instead
 * hold an instance of JodaDateTime, and through abstraction, acts as a mutable version
 * JodaDateTime, while also converting the necessary times to ODIN standards (e.g. day
 * of week).
 * 
 * Written by Anthony McIntosh 3/11/2019
 */
public class DateTimeConverter {

	private DateTime jodaDateTime;
	
	public DateTimeConverter(DateTime jodaDateTime) {
		this.jodaDateTime = jodaDateTime;
	}
	
	public DateTime getJodaDateTime() {
		return this.jodaDateTime;
	}
	
	/* ****************** 
	   OVERRIDDEN GETTERS 
	   ****************** */
	
	public int getSecondOfMinute() {
		return this.jodaDateTime.getSecondOfMinute();
	}
	
	public int getMinuteOfHour() {
		return this.jodaDateTime.getMinuteOfHour();
	}
	
	public int getHourOfDay() {
		return this.jodaDateTime.getHourOfDay();
	}
	
	public int getDayOfMonth() {
		return this.jodaDateTime.getDayOfMonth();
	}
	
	public int getMonthOfYear() {
		return this.jodaDateTime.getMonthOfYear();
	}
	
	/* 
	 * Converts the JODA day of the week into the ODIN day of the week
	 * 
	 *       Sun, Mon, Tue, Wed, Thur, Fri, Sat
	 * ODIN  0    1    2    3    4     5    6
	 * JODA  7    1    2    3    4     5    6
	 */
	public int getDayOfWeek() {
		if (this.jodaDateTime.getDayOfWeek() == 7) {
			return 0;
		}
		return this.jodaDateTime.getDayOfWeek();
	}
	
	
	/* ****************** 
	   OVERRIDDEN PLUSSERS 
	   ****************** */
	
	public void plusMonths(int monthsToAdd) {
		this.jodaDateTime = jodaDateTime.plusMonths(monthsToAdd);
	}
	
	public void plusDays(int daysToAdd) {
		this.jodaDateTime = jodaDateTime.plusDays(daysToAdd);
	}
	
	public void plusHours(int hoursToAdd) {
		this.jodaDateTime = jodaDateTime.plusHours(hoursToAdd);
	}
	
	public void plusMinutes(int minutesToAdd) {
		this.jodaDateTime = jodaDateTime.plusMinutes(minutesToAdd);
	}
	
	
	/* ****************** 
	   OVERRIDDEN SETTERS
	   ****************** */
	
	public void withDayOfMonth(int dayToSet) {
		this.jodaDateTime = jodaDateTime.withDayOfMonth(dayToSet);
	}
	
	public void withHourOfDay(int hourToSet) {
		this.jodaDateTime = jodaDateTime.withHourOfDay(hourToSet);
	}
	
	public void withMinuteOfHour(int minuteToSet) {
		this.jodaDateTime = jodaDateTime.withMinuteOfHour(minuteToSet);
	}
	
	public void withSecondOfMinute(int secondsToSet) {
		this.jodaDateTime = jodaDateTime.withSecondOfMinute(secondsToSet);
	}
}