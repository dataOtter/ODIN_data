package reports.rules.OnDepBt;

import java.util.Calendar;
import java.util.ListIterator;

import constants.*;
import orderedcollection.*;
import reports.rules.*;
import sensors.data.AbsDataPoint;
import sensors.data.BtDataPoint;
import sensors.data.BtDeviceData;
import sensors.data.SensorDataCollection;
import sensors.data.SensorDataOfOneType;
/**
*
* @author Maisha Jauernig
*/
public class OnDepBtPerformanceEval extends AbsRulePerformanceEval {
	private SensorDataOfOneType _btData;
	private final int _count;
	private final String _proximity;
	private final BtDataAdapter _btAd;

	public OnDepBtPerformanceEval(AnswersCollection answers, RulesCollection rules, SensorDataCollection allSensorData,
			double sensorFireTimeInterval, int cid, int rid) {
		
		super(answers, rules, allSensorData, sensorFireTimeInterval, cid, rid, getMinTReq(rules, rid));
		
		String sensorId = ConstTags.SENSORID_TO_TYPE.get(Constants.SENSORID_BT);
		_btData = allSensorData.getCouponDataOfType(_cid, sensorId).getDeepCopy();
		if (_btData != null) {
			_btAd = new BtDataAdapter(_btData, _sensorFireTimeInterval, _minTReq);
		} else _btAd = null; 

		OnDepBtRuleParams params = (OnDepBtRuleParams)rules.getRuleById(rid).getParams();
		_count = params.getCount();
		_proximity = params.getProximity();
		
	}
	
	private static int getMinTReq(RulesCollection rules, int rid) {
		return ( (OnDepBtRuleParams)rules.getRuleById(rid).getParams() ).getDelay();
	}
	
	/* W seconds after the following:   // _minTReq
	 * the participant was WITHIN distance Y   // _proximity
	 * of X (or more) other participant(s)   // _count
	 * for at least [2*SI] minutes,   // _sensorFireTimeInterval * 2
	 * but immediately prior WAS NOT WITHIN distance Y 
	 * of these same participant(s) 
	 * for at least [2*SI] minutes.   // _sensorFireTimeInterval * 2
	 * */
	
	// _btData contains BtDataPoints which contain an oc of BtDeviceData
	////// how do i get distance? one kind is bt proximity so just if its in the list its close enough? 
	// loop through _btData and check each BtDataPoint first for how long oc is (== _count + 1)
	// if right length, get time, get the devices
	// then check data at time + si for oc count and then devices to match up exactly,
	// repeat at time + 2si
	////// how do i get distance?
	// then check data at time - si for oc count less than _count+1 (if not, check if devices are different),
	// repeat for time - 2si
	
	/* 
	 * for BtDataPoint in _btData:
	 	* 
	 * */ 
	

	@Override
	protected void doTheWork() {
		ListIterator<AbsDataPoint> iter = _btData.listIterator();
		
		while (iter.hasNext()) {
			BtDataPoint bdNow = (BtDataPoint) iter.next();
			
			if (bdNow.getData().size() == _count + 1) {
				IMJ_OC<BtDeviceData> tempGroup = bdNow.getData();
				Calendar tNow = bdNow.getDateTime();
				
				//  the last data recording holds until a new one comes in
				
				tNow.add(Calendar.MINUTE, (int) _sensorFireTimeInterval);  // set time to 1si ahead
				BtDataPoint bd1si = (BtDataPoint) _btAd.getDataPointAtTime(tNow.getTimeInMillis() / 1000.0);
				IMJ_OC<BtDeviceData> group1si = bd1si.getData();
				
				if (group1si.size() == _count + 1 && group1si.equals(tempGroup)) {  // check at 1si ahead
					tNow.add(Calendar.MINUTE, (int) _sensorFireTimeInterval);  // set time to 2si ahead
					BtDataPoint bd2si = (BtDataPoint) _btAd.getDataPointAtTime(tNow.getTimeInMillis() / 1000.0);
					IMJ_OC<BtDeviceData> group2si = bd2si.getData();
					
					if (group2si.size() == _count + 1 && group2si.equals(tempGroup)) {  // check at 2si ahead
						tNow.add(Calendar.MINUTE, (int) (-3*_sensorFireTimeInterval));  // set time to 1si before
							BtDataPoint bdNeg1si = (BtDataPoint) _btAd.getDataPointAtTime(tNow.getTimeInMillis() / 1000.0);
							IMJ_OC<BtDeviceData> groupNeg1si = bdNeg1si.getData();
							
						 // check at 1si before
						if (groupNeg1si.size() != _count + 1 && ! groupNeg1si.equals(tempGroup)) {
							tNow.add(Calendar.MINUTE, (int) (-1*_sensorFireTimeInterval));  // set time to 2si before
							BtDataPoint bdNeg2si = (BtDataPoint) _btAd.getDataPointAtTime(tNow.getTimeInMillis() / 1000.0);
							IMJ_OC<BtDeviceData> groupNeg2si = bdNeg2si.getData();
							
							 // check at 2si before
							if (groupNeg2si.size() != _count + 1 && ! groupNeg2si.equals(tempGroup)) {
								tNow.add(Calendar.MINUTE, (int) (2*_sensorFireTimeInterval + _minTReq));
								shouldFireRule(tNow.getTimeInMillis() / 1000.0, _minTReq);
							}
						}
					}
				}
			}
		}	
	}

	@Override
	protected double getVeryFirstShouldFireTime() {
		// TODO Auto-generated method stub
		return 0;
	}
		
		
}
