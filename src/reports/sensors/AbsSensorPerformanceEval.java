package reports.sensors;

import constants.ConstTags;
import reports.OneReport;
import sensors.data.SensorDataOfOneType;

/**
 *
 * @author Maisha Jauernig
 */
public abstract class AbsSensorPerformanceEval{
    protected final SensorDataOfOneType _data;
    protected Double _val = null;
    protected final double _sensorInterval;
    protected final SensorPerformances _sensorPerformances;

    public AbsSensorPerformanceEval(SensorDataOfOneType data, double si, SensorPerformances sps) {
        _data = data;
        _sensorInterval = si;     
        _sensorPerformances = sps;
    }
    
    public void printTimeInterval(){
        System.out.println("\n" + ConstTags.REPORTS_S_I_TEXT + ": " + Math.round(_sensorInterval));
    }
    
    
    public Double getTimeInterval(){
        return _sensorInterval;
    }
    
    public abstract void printAll();
    
    public abstract OneReport addToMap(OneReport map);
    
    public Double getValueInPercent(){
        Double rawNum = _val;
    	if (rawNum == null) {
    		return null;
    	}
        double per = rawNum / _sensorInterval * 100;
        per = Math.round(per * 100d) / 100d;  // this is to get 2 decimal places
        return per;
    }
}
