package reports.rules;

import java.util.Calendar;

import sensors.data.AbsDataPoint;
import sensors.data.SensorDataOfOneType;

public abstract class AbsDataAdapter {
	private final long _sizeGpsBuffer;
    protected final SensorDataOfOneType _data;
    
    public AbsDataAdapter(SensorDataOfOneType d, double si, double minT) {
        _data = d;
        // this will be used for deleting past, already "used" data recordings
        // they are deleted up to _sizeGpsBuffer recordings before the given time
        _sizeGpsBuffer = Math.round(minT/si) * 10;
    }
    
    public double getFirstRecordingTime() {
        int i = 0;
        Calendar d = _data.get(i).getDateTime();
        while (d == null){
            i++;
            d = _data.get(i).getDateTime();
        }
        return d.getTimeInMillis() / 1000.0;
    }
    
    public double getLastRecordingTime() {
        int i = _data.size()-1;
        Calendar d = _data.get(i).getDateTime();
        while (d == null) {
            i--;
            d = _data.get(i).getDateTime();
        }
        return d.getTimeInMillis() / 1000.0;
    }

    public AbsDataPoint getDataPointAtTime(double tInSecs) {
        int i = getIdxOfDataAtTime(tInSecs);
        if (i < 0) {
        	return null;
        }
        return _data.get(i);
    }
    
    public AbsDataPoint getDataPointAndClearPreceeding(double tInSecs) {
        int i = getIdxOfDataAtTime(tInSecs);
        // this deletes past, already "used" data recordings
        // they are deleted up to _sizeGpsBuffer recordings before the given time
        if (i > _sizeGpsBuffer) {
            while (i > _sizeGpsBuffer) {
                i--;
                _data.remove(0);
            }
        }
        return _data.get(i);
    }
    
    protected int getIdxOfDataAtTime(double tInSecs){
    	if (tInSecs==0.0) {
    		return 0;
    	}
        double t = _data.get(_data.size()-1).getDateTime().getTimeInMillis()/1000.0;
        if (tInSecs >= t) {
            return _data.size()-1;
        }

        for (int i = 0; i<_data.size(); i++) {
            t = _data.get(i).getDateTime().getTimeInMillis()/1000.0;
            if (t > tInSecs) {
                return i-1;
            }
        }
        return -1;
    }
}
