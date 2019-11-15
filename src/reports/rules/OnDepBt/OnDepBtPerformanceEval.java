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
	
	@Override
	protected void doTheWork() {
		ListIterator<AbsDataPoint> iter = _btData.listIterator();
		
		while (iter.hasNext()) {
			// check each bt data point recorded by the bt sensor
			BtDataPoint dpNow = (BtDataPoint) iter.next();
			IMJ_OC<BtDeviceData> groupNow = dpNow.getData();  // get devices
			// if not the right number of devices or if the devices fail proximity check, the dpNow fails
			if (groupNow.size() != _count+1 || ! checkIfInProximity(groupNow)) {  
				continue;
			}
			Calendar tNow = dpNow.getDateTime();  // get the time of the dpNow
			
			// if device 1si ahead are not the same (and/or are not close if so required) the dpNow fails
			if ( ! checkProximityReqAndIfSameDevices(groupNow, (Calendar) tNow.clone(), 1)) { 
				continue;
			}
			
			// if device 2si ahead are not the same (and/or are not close if so required) the dpNow fails
			if ( ! checkProximityReqAndIfSameDevices(groupNow, (Calendar) tNow.clone(), 2)) { 
				continue;
			}

			// if device 1si BEFORE ARE the same (and/or ARE close if so not required) the dpNow fails
			if (checkProximityReqAndIfSameDevices(groupNow, (Calendar) tNow.clone(), -1)) { 
				continue;
			}
			
			// if device 2si BEFORE ARE the same (and/or ARE close if so not required) the dpNow fails
			if (checkProximityReqAndIfSameDevices(groupNow, (Calendar) tNow.clone(), -2)) { 
				continue;
			}
			
			// if all the checks passed, then it should fire 
			// add the rule required delay to tNow
			tNow.add(Calendar.MINUTE, (int) (_minTReq));
			shouldFireRule(tNow.getTimeInMillis() / 1000.0, _minTReq);
		}	
	}
	
	private boolean checkProximityReqAndIfSameDevices(IMJ_OC<BtDeviceData> original, Calendar t,  double siMultiplierForTOffset) {
		t.add(Calendar.MINUTE, (int) (siMultiplierForTOffset*_sensorFireTimeInterval));
		// get the devices recorded at the given time 
		IMJ_OC<BtDeviceData> toCheck = ( (BtDataPoint) _btAd.getDataPointAtTime(t.getTimeInMillis() / 1000.0) ).getData();
		
		for (BtDeviceData d: original) {  // for each device in the original oc
			String name = d.getDeviceName();
			boolean found = false;
			for (BtDeviceData d2: toCheck) {  // find the same device in the oc to check against
				// first check if the name is the same, then, if it should be close, check that
				if ( name.equals(d2.getDeviceName()) && checkIfInProximity(d2) ) {
					found = true;
					break;
				}
			}
			if (! found) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkIfInProximity(BtDeviceData d) {
		if (! _proximity.equals(Constants.RULES_PROXIMITY_CLOSE)) {
			return true;
		}
		if (d.getRawRSSI() < -70) {
			return false;
		}
		return true;
	}
	
	private boolean checkIfInProximity(IMJ_OC<BtDeviceData> tempGroup) {
		if (! _proximity.equals(Constants.RULES_PROXIMITY_CLOSE)) {
			return true;
		}
		for (BtDeviceData d: tempGroup) {
			if (d.getRawRSSI() < -70) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected double getVeryFirstShouldFireTime() {
		// TODO Auto-generated method stub
		return 0;
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
	////// distance: raw rssi >= -70 is close and nearby, < -70 is nearby only 
	// loop through _btData and check each BtDataPoint first for how long oc is (== _count + 1)
	// if right length, get time, get the devices, get distances if close
	// then check data at time + si for oc count and then devices to match up exactly and distance if applicable,
	// repeat at time + 2si
	// then check data at time - si for oc count less than _count+1 (if not, check if devices are different or distance is diff is close),
	// repeat for time - 2si
	//  the last data recording holds until a new one comes in
}
