package reports.rules.whileAt;

import java.util.Calendar;

import reports.rules.Predicate;
import sensors.data.GpsDataPoint;
import sensors.data.SensorDataOfOneType;
import sensors.gps.*;

/**
 *
 * @author Maisha Jauernig
 */
public class GpsDataAdapter {
    private final long _sizeGpsBuffer;
    private final SensorDataOfOneType _data;
    
    public GpsDataAdapter(SensorDataOfOneType d, double si, double minT){
        _data = d;
        // this will be used for deleting past, already "used" GPS recordings
        // they are deleted up to _sizeGpsBuffer recordings before the given time
        _sizeGpsBuffer = Math.round(minT/si) * 10;
    }
    
    public double getFirstRecordingTime(){
        int i = 0;
        Calendar d = _data.getDataAtIdx(i).getDateTime();
        while (d == null){
            i++;
            d = _data.getDataAtIdx(i).getDateTime();
        }
        return d.getTimeInMillis() / 1000.0;
    }
    
    public double getLastRecordingTime(){
        int i = _data.length()-1;
        Calendar d = _data.getDataAtIdx(i).getDateTime();
        while (d == null){
            i--;
            d = _data.getDataAtIdx(i).getDateTime();
        }
        return d.getTimeInMillis() / 1000.0;
    }
    
    //public double getDurationatLocOfTimeT(double t){}
    
    public double getNextStartTime(double t, Predicate p){
        // this expects a t of a location that is not the predicate location
        GpsDataPoint g;
        GpsCoordinate c;
        int idx = this.getIdxOfLocAtTime(t);
        
        for (int i = idx; i<_data.length(); i++){
            g = (GpsDataPoint) _data.getDataAtIdx(i);
            c = g.getGpsCoord();
            if (p.test(c)){
                Calendar d = _data.getDataAtIdx(i).getDateTime();
                if (d != null){
                    return d.getTimeInMillis() / 1000.0;
                }
            }
        }
        return 0.0;
    }
    
    public GpsDataPoint getLocationAndClearPreceeding(double tInSecs){
        int i = getIdxOfLocAtTime(tInSecs);
        // this deletes past, already "used" GPS recordings
        // they are deleted up to _sizeGpsBuffer recordings before the given time
        if (i > _sizeGpsBuffer){
            while (i > _sizeGpsBuffer){
                i--;
                _data.deleteItem(0);
            }
        }
        return (GpsDataPoint) _data.getDataAtIdx(i);
    }
    
    public GpsDataPoint getLocation(double tInSecs){
        int i = getIdxOfLocAtTime(tInSecs);
        return (GpsDataPoint) _data.getDataAtIdx(i);
    }
    
    private Integer getIdxOfLocAtTime(double tInSecs){
        double t = _data.getDataAtIdx(_data.length()-1).getDateTime().getTimeInMillis()/1000.0;
        
        if (tInSecs >= t){
            return _data.length()-1;
        }

        for (int i = 0; i<_data.length(); i++){
            t = _data.getDataAtIdx(i).getDateTime().getTimeInMillis()/1000.0;
            if (t > tInSecs){
                return i-1;
            }
        }
        return null;
    }
}
