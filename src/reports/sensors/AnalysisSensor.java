package reports.sensors;

import constants.ConstTags;
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
	private double _startTimeInSecs = -1.0;
	private double _stopTimeInSecs = -1.0;
    private final SensorDataOfOneType _data;
    
    public AnalysisSensor(int cid, int sensorId, SensorDataCollection allData, double si, 
    		double stopTimeInSecs, double windowInHrs,IMJ_Map<Integer, String> cIdToNames) {
    	_cIdToNames = cIdToNames;
        _sensorId = sensorId;
		if (stopTimeInSecs == -1) {
	        _data = allData.getCouponDataOfType(cid, getAnalysisType()).getDeepCopy();
		}
		else {
			_stopTimeInSecs = stopTimeInSecs;
			_startTimeInSecs = _stopTimeInSecs - (windowInHrs * 60.0 * 60.0);
			
	        _data = allData.getCouponDataOfTypeInTimeWindow(cid, getAnalysisType(), _startTimeInSecs, _stopTimeInSecs)
	        		.getDeepCopy();
		}
        _couponId = cid; 
        _sensorInterval = si;
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
        
        SensorAverageTimeInterval avg = new SensorAverageTimeInterval(_data, _sensorInterval);
        rep = avg.addToMap(rep);
        
        SensorMinTimeInterval min = new SensorMinTimeInterval(_data, _sensorInterval);
        rep = min.addToMap(rep);
        
        SensorMaxTimeInterval max = new SensorMaxTimeInterval(_data, _sensorInterval);
        rep = max.addToMap(rep);
        
        SensorRecordingsWithinGivenPercentOfTimeInterval numInRange = 
                new SensorRecordingsWithinGivenPercentOfTimeInterval(_data, _sensorInterval);
        rep = numInRange.addToMap(rep);
        
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
}
