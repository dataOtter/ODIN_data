package cron;

public class DayOfWeekCompare extends AbstractCronFieldCompare{

	private static int minValue = ConstantsRE.MINVALUE_DAYOFWEEK;
	private static int maxValue = ConstantsRE.MAXVALUE_DAYOFWEEK;
	
	public DayOfWeekCompare(String dayOfWeekString)
	{
		super(dayOfWeekString);
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