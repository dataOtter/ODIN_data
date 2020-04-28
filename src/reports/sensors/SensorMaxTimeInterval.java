package reports.sensors;

import constants.ConstTags;
import reports.OneReport;
import sensors.data.SensorDataOfOneType;

/**
 *
 * @author Maisha Jauernig
 */
public class SensorMaxTimeInterval extends AbsSensorPerformanceEval {
    
    public SensorMaxTimeInterval(SensorDataOfOneType data, double si, SensorPerformances sps) {
        super(data, si, sps);
        _val = _sensorPerformances.getMax();
    }
    
    @Override
    public void printAll(){
        double maxGap = _val;
        String message = "\n\t" + ConstTags.REPORTS_MAX_B_S_R_TEXT + ": " + maxGap;
        if (maxGap > (5 * this._sensorInterval)){
            message += " (" + maxGap/60 + " mins)";
        }
        if (maxGap/60 > 120){
            message += " (" + maxGap/60/60 + " hours)";
        }
        message += "\n\t" + ConstTags.REPORTS_MAX_B_S_R_A_P_TEXT+ ": " + this.getValueInPercent();
        System.out.println(message);
    }

    @Override
    public OneReport addToMap(OneReport map) {
        map.addValue(ConstTags.REPORTS_MAXT_BTW_SENSOR_RECS, _val, ConstTags.REPORTS_MAX_B_S_R_TEXT);
        map.addValue(ConstTags.REPORTS_MAXT_BTW_SENSOR_RECS_AS_PERC, getValueInPercent(), ConstTags.REPORTS_MAX_B_S_R_A_P_TEXT);
        return map;
    }
}
