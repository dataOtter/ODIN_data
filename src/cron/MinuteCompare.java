package cron;

public class MinuteCompare extends AbstractCronFieldCompare {

	private static int minValue = ConstantsRE.MINVALUE_MINUTE;
	private static int maxValue = ConstantsRE.MAXVALUE_MINUTE;

	public MinuteCompare(String minuteString)
	{
		super(minuteString);
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