package reports.rules.Cron;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import constants.Constants;
import cron.CRONExpression;
import dao.CouponCollection;
import dao.OneCoupon;
import reports.rules.AbsRulePerformanceEval;
import reports.rules.AnswersCollection;
import reports.rules.RulesCollection;
import sensors.data.SensorDataCollection;
/**
*
* @author Maisha Jauernig
*/
public class CronPerformanceEval extends AbsRulePerformanceEval {	
	private final OneCoupon _coupon;
	private final CRONExpression _cron;
	private DateTime _maxTJoda;

	public CronPerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double gpsSensorFireTimeInterval, int cid, int rid, CouponCollection coupons, 
			double stopTimeInSecs, double windowInHrs) {

		// get time to next fire as replacement for 3000
		super(answers, rules, allSensorData, gpsSensorFireTimeInterval, cid, rid, gpsSensorFireTimeInterval*2,
				getAllowedDevOnTime(rules, rid), getAllowedDevNotMissed(rules, rid), coupons, stopTimeInSecs, windowInHrs);

		_coupon = coupons.getCouponById(_cid);
		
		DateTimeZone maxAnsTTZ = DateTimeZone.forID( _coupon.getVeryLastUpload().getTimeZone().getID() );
		_maxTJoda = new DateTime((long)_maxAnsT*1000, maxAnsTTZ);
		
		CronRuleParams params = (CronRuleParams)rules.getRuleById(rid).getParams();
		_cron = new CRONExpression(params.getCron());
	}
	
	@Override
	protected void doTheWork() {
		// step through time by each next time that the cron rule dictates
		for (double fireT = getVeryFirstShouldFireTime(); fireT < _maxAnsT && fireT > 0; 
				fireT = getNextShouldFireTime(fireT)) {
			shouldFireRule(fireT);
		}
	}
	
	@Override
	protected double getVeryFirstShouldFireTime() {
		Calendar t = _coupon.getLastRegistrationTime();
		DateTime tJoda = new DateTime(t);
		int secondsUntilFire = _cron.getSecondsToFire(tJoda, _maxTJoda, false);
		
		if (secondsUntilFire < 0) {
			return -1;
		}
		return t.getTimeInMillis() / 1000.0 + secondsUntilFire;
	}
	
	private double getNextShouldFireTime(double prevFireT) {
		DateTime tJoda = new DateTime((long)prevFireT * 1000);
		int secondsUntilFire = _cron.getSecondsToFire(tJoda, _maxTJoda, true);
		
		if (secondsUntilFire < 0) {
			return -1;
		}
		return prevFireT + secondsUntilFire;
	}
	
	private static double getAllowedDevOnTime(RulesCollection rules, int rid) {
		return Constants.PERC_ALLOWED_DEV_FROM_GIVEN_TIME_ONTIME_CRON * getMaxSecsBetweenFires(rules, rid);
	}
	
	private static double getAllowedDevNotMissed(RulesCollection rules, int rid) {
		return Constants.PERC_ALLOWED_DEV_FROM_GIVEN_TIME_NOTMISSED_CRON * getMaxSecsBetweenFires(rules, rid);
	}
	
	private static double getMaxSecsBetweenFires(RulesCollection rules, int rid) {
		CronRuleParams params = (CronRuleParams)rules.getRuleById(rid).getParams();
		CRONExpression cron = new CRONExpression(params.getCron());
		
		DateTime now = DateTime.now();
		int s1 = cron.getSecondsToFire(now, now.plusMonths(13), false);
		DateTime fire1 = now.plusSeconds(s1);
		int maxSecsBetweenFires = cron.getSecondsToFire(fire1, fire1.plusMonths(13), true);
		return (double) maxSecsBetweenFires;
	}
}
