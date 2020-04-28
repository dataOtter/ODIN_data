package reports.sensors;

import constants.ConstTags;
import reports.OneReport;
import sensors.data.SensorDataOfOneType;

/**
 *
 * @author Maisha Jauernig
 */
public class SensorMinTimeInterval extends AbsSensorPerformanceEval {
    
    public SensorMinTimeInterval(SensorDataOfOneType data, double si, SensorPerformances sps) {
        super(data, si, sps);
        _val = _sensorPerformances.getMin();
    }
    
    @Override
    public void printAll(){
        System.out.println("\n\t" + ConstTags.REPORTS_MIN_B_S_R_TEXT + ": " + _val +
                    "\n\t" + ConstTags.REPORTS_MIN_B_S_R_A_P_TEXT + ": " + getValueInPercent());
            
    }

    @Override
    public OneReport addToMap(OneReport map) {
        map.addValue(ConstTags.REPORTS_MINT_BTW_SENSOR_RECS, _val, ConstTags.REPORTS_MIN_B_S_R_TEXT);
        map.addValue(ConstTags.REPORTS_MINT_BTW_SENSOR_RECS_AS_PERC, getValueInPercent(), ConstTags.REPORTS_MIN_B_S_R_A_P_TEXT);
        return map;
    }
}
