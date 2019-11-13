package reports.rules.Cron;

import java.util.Calendar;
import org.joda.time.DateTime;

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

	public CronPerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double sensorFireTimeInterval, int cid, int rid, CouponCollection coupons) {
		
		super(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, sensorFireTimeInterval*2);

		_maxAnsT = 0.0;
		if (_answersLeft.size() > 0) {
			_maxAnsT = _answersLeft.get(_answersLeft.size() - 1).getRuleFiredTime().getTimeInMillis() / 1000.0;
		}
		_coupon = coupons.getCouponById(_cid);
		CronRuleParams params = (CronRuleParams)rules.getRuleById(rid).getParams();
		_cron = new CRONExpression(params.getCron());
	}
	
	@Override
	protected void doTheWork() {
		// step through time by each next time that the cron rule dictates
		for (double fireT = getVeryFirstShouldFireTime(); fireT < _maxAnsT; fireT = getNextShouldFireTime(fireT)) {
			shouldFireRule(fireT, _sensorFireTimeInterval);
			// set fireT to when it actually fired
			fireT = _trueFireT;
		}
		Assertion.test(
				_earlyAns.size() + _answersLeft.size() + _goodAnsCount + _lateAns.size() == _numRuleFiresTotal,
				"not all answers are accounted for");
	}
	
	@Override
	protected double getVeryFirstShouldFireTime() {
		Calendar t = _coupon.getLastRegistrationTime();
		//TimeZone tz = t.getTimeZone();
		//DateTimeZone jodaTz = DateTimeZone.forID(tz.getID());
		
		return getNextShouldFireTime(t.getTimeInMillis());
	}
	
	private double getNextShouldFireTime(double prevFireT) {
		DateTime tJoda = new DateTime(prevFireT+1.0 * 1000.0);
		DateTime maxTJoda = new DateTime(_maxAnsT*1000);
		
		int secondsUntilFire = _cron.getSecondsToFire(tJoda, maxTJoda, true);
		
		return prevFireT + secondsUntilFire;
	}
}
