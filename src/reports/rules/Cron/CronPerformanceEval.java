package reports.rules.Cron;

import java.util.Calendar;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import Assert.Assertion;
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
			double sensorFireTimeInterval, int cid, int rid, CouponCollection coupons) {
		
		super(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, sensorFireTimeInterval*2);

		_maxAnsT = Calendar.getInstance().getTimeInMillis() / 1000.0;
		_maxTJoda = DateTime.now();
		if (_answersLeft.size() > 0) {
			Calendar maxT = _answersLeft.get(_answersLeft.size() - 1).getRuleFiredTime();
			_maxAnsT = maxT.getTimeInMillis() / 1000.0;
			TimeZone tz = maxT.getTimeZone();
			DateTimeZone maxAnsTTZ = DateTimeZone.forID(tz.getID());
			_maxTJoda = new DateTime((long)_maxAnsT*1000, maxAnsTTZ);
		}
		
		_coupon = coupons.getCouponById(_cid);
		CronRuleParams params = (CronRuleParams)rules.getRuleById(rid).getParams();
		_cron = new CRONExpression(params.getCron());
	}
	
	@Override
	protected void doTheWork() {
		// step through time by each next time that the cron rule dictates
		for (double fireT = getVeryFirstShouldFireTime(); fireT < _maxAnsT && fireT > 0; fireT = getNextShouldFireTime(fireT)) {
			//shouldFireRule(fireT, _sensorFireTimeInterval);
			shouldFireRule(fireT, 3000.0);
		}
		Assertion.test(
				_earlyAns.size() + _answersLeft.size() + _goodAnsCount + _lateAns.size() == _numRuleFiresTotal,
				"not all answers are accounted for");
	}
	
	@Override
	protected double getVeryFirstShouldFireTime() {
		Calendar t = _coupon.getLastRegistrationTime();
		DateTime tJoda = new DateTime(t);
		int secondsUntilFire = _cron.getSecondsToFire(tJoda, _maxTJoda, true);
		
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
}
