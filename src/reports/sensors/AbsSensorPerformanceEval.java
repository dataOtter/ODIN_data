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

    public AbsSensorPerformanceEval(SensorDataOfOneType data, double si) {
        _data = data;
        _sensorInterval = si;     
    }
    
    public void printTimeInterval(){
        System.out.println("\n" + ConstTags.REPORTS_S_I_TEXT + ": " + Math.round(_sensorInterval));
    }
    
    public Double getTimeInterval(){
        return _sensorInterval;
    }
    
    protected abstract Double getValue();
    
    public abstract void printAll();
    
    public abstract OneReport addToMap(OneReport map);
    
    public Double getValueInPercent(){
        Double rawNum = getValue();
    	if (rawNum == null) {
    		return null;
    	}
        double per = rawNum / _sensorInterval * 100;
        per = Math.round(per * 100d) / 100d;  // this is to get 2 decimal places
        return per;
    }
}
