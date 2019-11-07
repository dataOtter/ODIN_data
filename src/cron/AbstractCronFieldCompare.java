package cron;

import java.util.HashSet;

/*
 * A CRONExpression object creates various comparer for the different
 * elements of the CronString. These comparer have to ability to generate
 * a list of viable tokens which will satisfy that element of the CronString.
 * This abstract class is the framework with which the comparer use to
 * generate those tokens, and test to see if a given time matches a token.
 * 
 * Written by Alekhya Bellam
 * Revised by Anthony McIntosh 3/11/2019
 */
public abstract class AbstractCronFieldCompare {

	private final String _fieldString;
	private HashSet<Integer> tokenSet = new HashSet<Integer>();
	
	protected abstract int get_minValue();
	protected abstract int get_maxValue();

	public AbstractCronFieldCompare(String fieldString)
	{
		_fieldString = fieldString;
		this.setIntegerTokens();
	}
	
	public String get_String() {
		return _fieldString;
	}
	
	public HashSet<Integer> getTokenSet() {
		return tokenSet;
	}
	
	public Boolean testTimeMatches(int timeToCompare) {
		return _fieldString.equals("*") || tokenSet.contains(timeToCompare);
	}
	
	/*
	 * Converts one element of the ODIN CronString into a set of integers.
	 * This set encompasses all possible values, within the min and max value
	 * for that element position, that would satisfy the cron.
	 * 
	 * If the element is the wild card "*", the returned set only contains -1.
	 * 
	 * Examples:
	 * 
	 * For MinuteComparer:
	 * _fieldString = "5/10"
	 * setIntegerTokens() --> (5, 15, 25, 35, 45, 55)
	 * 
	 * For HourComparer:
	 * _fieldString = "5/10"
	 * setIntegerTokens() --> (5, 15)
	 */
	private void setIntegerTokens()
	{
		if (_fieldString.equals("*")) {
			tokenSet.add(-1);
			
		} else {
				
			for(String stringElement: _fieldString.split(",")) {
				String[] stringsSplitBySlash = stringElement.split("/"); 	// Iterated cron string
				String[] stringsSplitByHiphen = stringElement.split("-");	// Range cron string
				
				if(stringsSplitBySlash.length==1 && stringsSplitByHiphen.length==1) {
					tokenSet.add(Integer.parseInt(stringElement));
					
				} else if(stringsSplitBySlash.length == 2  && stringsSplitByHiphen.length==1 ) {
					String startingValue = stringsSplitBySlash[0];
					int incrementValue = Integer.parseInt(stringsSplitBySlash[1]);
					int tokenValue;

					if(startingValue.equals("*")) {
						tokenValue = this.get_minValue();
					} else {
						tokenValue = Integer.parseInt(startingValue);
					}
					
					assert tokenValue <= this.get_maxValue() : "The starting value of an incremented cron string element was greater than the max value"
                            + " for that element.";
					assert tokenValue >= this.get_minValue() : "The starting value of an incremented cron string element was less than the mni value"
                            + " for that element.";
					
					while(tokenValue <= get_maxValue()) {
						tokenSet.add(tokenValue);
						tokenValue += incrementValue;
					}
					
				} else if(stringsSplitBySlash.length == 1  && stringsSplitByHiphen.length==2) {
					int bottomOfRange = Integer.parseInt(stringsSplitByHiphen[0]);
					int topOfRange = Integer.parseInt(stringsSplitByHiphen[1]);
					int tokenValue = bottomOfRange;
					
					assert bottomOfRange <= topOfRange : "The bottom of a range cron string element was greater than the top of the range.";
					assert bottomOfRange <= this.get_maxValue() : "The bottom of a range cron string element was greater than the max value"
					        + " for that element.";
					assert bottomOfRange >= this.get_minValue() : "The bottom of a range cron string element was less than the min value"
                            + " for that element.";

					while (tokenValue <= topOfRange) {
						tokenSet.add(tokenValue);
						tokenValue++;
					}
				}
			}
		}
	}
	
}