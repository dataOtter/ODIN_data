package cron;

public class DayOfMonthCompare extends AbstractCronFieldCompare{

	private static int minValue = ConstantsRE.MINVALUE_DAYOFMONTH;
	private static int maxValue = ConstantsRE.MAXVALUE_DAYOFMONTH;
	
	public DayOfMonthCompare(String dayOfMonthString)
	{
		super(dayOfMonthString);
	}

	@Override
	protected int get_minValue() {
		return minValue;
	}

	@Override
	protected int get_maxValue() {
		return maxValue;
	}
}