package reports.sensors;

import constants.ConstTags;
import dao.CouponCollection;
import maps.IMJ_Map;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.IAnalysis;
import reports.OneReport;
import sensors.data.SensorDataCollection;
import sensors.data.SensorDataOfOneType;

/**
 *
 * @author Maisha Jauernig
 */
public class AnalysisSensor implements IAnalysis {
    private final double _sensorInterval;
    private final int _sensorId;
    private final IMJ_Map<Integer, String> _cIdToNames;
    private final int _couponId;
	private double _windowInSecs;
    private SensorDataOfOneType _data;
    
    public AnalysisSensor(int cid, int sensorId, SensorDataCollection allData, double si, double stopTimeInSecs, 
    		double windowInHrs, IMJ_Map<Integer, String> cIdToNames, double startTimeInSecs, CouponCollection coupons) {
    	_cIdToNames = cIdToNames;
        _sensorId = sensorId;
        _couponId = cid; 
        _sensorInterval = si;
        setWindowAndData(cid, allData, stopTimeInSecs, windowInHrs, startTimeInSecs, coupons);
    }
    
    @Override
    public OneReport getAnalysisReport() {
    	
    	IMJ_OC<String> relatedDataNames = new MJ_OC_Factory<String>().create();
    	relatedDataNames.add(ConstTags.REPORTS_REL_DATA_GPS);
    	
    	OneReport rep = new OneSensorReport(relatedDataNames);
    	
    	rep.addValue(ConstTags.REPORTS_COUPONNAME, _cIdToNames.get(_couponId));
        rep.addValue(ConstTags.REPORTS_COUPONID, Integer.toString(_couponId));
        rep.addValue(ConstTags.REPORTS_SENSORID, Integer.toString(_sensorId));
        rep.addValue(ConstTags.REPORTS_SENSOR_INTERVAL, Double.toString(_sensorInterval));
        
    	SensorPerformances sps = new SensorPerformances(_data, _sensorInterval, _windowInSecs);
    	
    	rep.addValue(ConstTags.REPORTS_NUM_IDEAL_RECORDINGS, sps.getIdealRecs(), ConstTags.REPORTS_N_I_R_TEXT); 
    	rep.addValue(ConstTags.REPORTS_TOTAL_SENSOR_RECS, sps.getNumRecs(), ConstTags.REPORTS_T_S_R_TEXT); 
    	
        SensorRecordingsWithinGivenPercentOfTimeInterval numInRange = 
                new SensorRecordingsWithinGivenPercentOfTimeInterval(_data, _sensorInterval, sps);
        rep = numInRange.addToMap(rep);
        
        SensorAverageTimeInterval avg = new SensorAverageTimeInterval(_data, _sensorInterval, sps);
        rep = avg.addToMap(rep);
        
        SensorMinTimeInterval min = new SensorMinTimeInterval(_data, _sensorInterval, sps);
        rep = min.addToMap(rep);
        
        SensorMaxTimeInterval max = new SensorMaxTimeInterval(_data, _sensorInterval, sps);
        rep = max.addToMap(rep);
        
        return rep;
    }

	@Override
	public String getAnalysisType() {
		return ConstTags.SENSORID_TO_TYPE.get(_sensorId);
	}
	
	@Override
	public String toString() {
		return "SI: " + _sensorInterval + " sid: " + _sensorId + " cid: " + _couponId 
				+ " " + _data.toString();
	}
    
    private void setWindowAndData(int cid, SensorDataCollection allData, double stopTimeInSecs, 
    		double windowInHrs, double startTimeInSecs, CouponCollection coupons) {
    	// if no time restraints are given
        if (stopTimeInSecs == -1.0 && startTimeInSecs == -1.0 && windowInHrs == -1.0) {
			stopTimeInSecs = (coupons.getCouponById(cid).getStudyEndTime().getTimeInMillis() / 1000.0) + 9999.0;
			startTimeInSecs = coupons.getCouponById(cid).getLastRegistrationTime().getTimeInMillis() / 1000.0;
			_windowInSecs = stopTimeInSecs - startTimeInSecs;
	        _data = allData.getCouponDataOfType(cid, getAnalysisType()).getDeepCopy();
		}
        // if some time restraints are given
        else {
        	// if a time window is given
        	if (windowInHrs != -1.0) {
        		_windowInSecs = windowInHrs * 60 * 60;
        		
        		if (stopTimeInSecs == -1.0 && startTimeInSecs == -1.0) {
                	startTimeInSecs = coupons.getCouponById(cid).getLastRegistrationTime().getTimeInMillis() / 1000.0;
                	stopTimeInSecs = startTimeInSecs + _windowInSecs;
                }
        		else if (stopTimeInSecs == -1.0) {
                	stopTimeInSecs = startTimeInSecs + _windowInSecs;
                }
                else if (startTimeInSecs == -1.0) {
                	startTimeInSecs = stopTimeInSecs - _windowInSecs;
                }
        	}
        	// if no time window is given
            else {
            	if (stopTimeInSecs == -1.0 && windowInHrs == -1.0) {
                	stopTimeInSecs = (coupons.getCouponById(cid).getStudyEndTime().getTimeInMillis() / 1000.0) + 9999.0;
                }
                else if (startTimeInSecs == -1.0 && windowInHrs == -1.0) {
        			startTimeInSecs = coupons.getCouponById(cid).getLastRegistrationTime().getTimeInMillis() / 1000.0;
                }
            	_windowInSecs = stopTimeInSecs - startTimeInSecs;
            }
        	// data with time restraints 
        	_data = allData.getCouponDataOfTypeInTimeWindow(cid, getAnalysisType(), startTimeInSecs, stopTimeInSecs)
	        		.getDeepCopy();
        }
    }
}
