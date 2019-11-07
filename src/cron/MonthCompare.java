package cron;

public class MonthCompare extends AbstractCronFieldCompare{


	private static int minValue = ConstantsRE.MINVALUE_MONTH;
	private static int maxValue = ConstantsRE.MAXVALUE_MONTH;
	
	public MonthCompare(String monthString)
	{
		super(monthString);
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