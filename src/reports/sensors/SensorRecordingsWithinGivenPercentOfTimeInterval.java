package reports.sensors;

import constants.ConstTags;
import constants.Constants;
import reports.OneReport;
import sensors.data.SensorDataOfOneType;

/**
 *
 * @author Maisha Jauernig
 */
public class SensorRecordingsWithinGivenPercentOfTimeInterval extends AbsSensorPerformanceEval {
	private final double _per = Constants.PERCENT_ALLOWED_DEVIATION_FROM_SI;
    
    public SensorRecordingsWithinGivenPercentOfTimeInterval(SensorDataOfOneType data, double si, SensorPerformances sps) {
        super(data, si, sps);
        _val = _sensorPerformances.getMin();
    }

    @Override
    public Double getValueInPercent(){
    	if (_val == null) {
    		return null;
    	}
        double per = _val / (double) _data.size() * 100;
        per = Math.round(per * 1d) / 1d;  // this is to get 2 decimal places
        return per;
    }
    
    private int getTotalNumOfSensorRecs(){
        return _data.size();
    }
    
    @Override
    public void printAll(){
        System.out.println("\n\tNum sensor recordings (of " + getTotalNumOfSensorRecs() + 
                    ") within " + _per*100 + "% of requested interval: "  + _val +
                    "\n\tPercent of recordings within interval: " + getValueInPercent());
    }

    @Override
    public OneReport addToMap(OneReport map) {
        map.addValue(ConstTags.REPORTS_PERC_ALLW_DEV_FRM_SI, _per * 100.0, ConstTags.REPORTS_P_A_D_F_SI_TEXT);
        map.addValue(ConstTags.REPORTS_SENSOR_RECS_WITHIN_DEV, _val, ConstTags.REPORTS_S_R_W_D_TEXT);
        map.addValue(ConstTags.REPORTS_PERC_SENSOR_RECS_WITHIN_DEV, getValueInPercent(), ConstTags.REPORTS_S_R_P_W_D_TEXT);
        return map;
    }
}
