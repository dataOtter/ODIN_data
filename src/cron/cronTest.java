package cron;

import org.joda.time.DateTime;

public class cronTest {
	public static void main (String[] args) {
		DateTime t = new DateTime();
		System.out.println("time now is "+t);
		
		DateTime farAhead = t.plusDays(1);
		
		// minute, hour, day of week, day of month, month
		CRONExpression ce = new CRONExpression("15 * * * *"); // should fire at 15 minutes past each hour
		int secondsUntilFire = ce.getSecondsToFire(t, farAhead, true);
		double minutesUntilFire = secondsUntilFire/60.0;
		
		//{"filtersList":"null","ruleParamToValue":"{\"cronRuleString_manual\":null,\"cronRuleString\":\"0 14 * * *\"}"}

		System.out.println("seconds to fire = "+secondsUntilFire);	
		System.out.println("minutes to fire = "+minutesUntilFire);		
	}
}
