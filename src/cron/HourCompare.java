package cron;

public class HourCompare extends AbstractCronFieldCompare {

	private static int minValue = ConstantsRE.MINVALUE_HOUR;
	private static int maxValue = ConstantsRE.MAXVALUE_HOUR;

	public HourCompare(String hourString)
	{
		super(hourString);
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