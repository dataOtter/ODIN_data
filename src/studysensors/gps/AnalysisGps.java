package studysensors.gps;

import Constants.ConstTags;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import stats.*;
import studysensors.*;
import studysensors.gps.gpsDeepLayer.OneCouponsGpsData;

/**
 *
 * @author Maisha
 */
public class AnalysisGps implements IAnalysis{
    private final double _sensorInterval;
    private final int _sensorId;
    private final int _couponId;
    private final OneCouponsGpsData _data;
    
    public AnalysisGps(int cid, int sensorId, GpsDataCollection allGpsData, double si) {
        _sensorId = sensorId;
        _data = allGpsData.getCouponData(cid).getDeepCopy();
        _couponId = cid; 
        _sensorInterval = si;
    }
    
    @Override
    public OneReport getAnalysisReport() {
    	
    	IMJ_OC<String> relatedDataNames = new MJ_OC_Factory<String>().create();
    	relatedDataNames.add(ConstTags.REPORTS_REL_DATA_GPS);
    	
    	OneReport rep = new OneSensorReport(relatedDataNames);
    	
        rep.addValue(ConstTags.REPORTS_COUPONID, _couponId * 1.0);
        rep.addValue(ConstTags.REPORTS_SENSORID, _sensorId * 1.0);
        rep.addValue(ConstTags.REPORTS_SENSOR_INTERVAL, _sensorInterval);
        
        GpsAverageTimeInterval avg = new GpsAverageTimeInterval(_data, _sensorInterval);
        rep = avg.addToMap(rep);
        
        GpsMinTimeInterval min = new GpsMinTimeInterval(_data, _sensorInterval);
        rep = min.addToMap(rep);
        
        GpsMaxTimeInterval max = new GpsMaxTimeInterval(_data, _sensorInterval);
        rep = max.addToMap(rep);
        
        GpsRecordingsWithinGivenPercentOfTimeInterval numInRange = 
                new GpsRecordingsWithinGivenPercentOfTimeInterval(_data, _sensorInterval);
        rep = numInRange.addToMap(rep);
        
        return rep;
    }

	@Override
	public String getAnalysisType() {
		return ConstTags.REPORT_TYPE_GPS_SENSOR_ANALYSIS;
	}
}
