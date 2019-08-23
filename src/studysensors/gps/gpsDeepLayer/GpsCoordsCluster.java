package studysensors.gps.gpsDeepLayer;

import orderedcollection.*;

/**
 *
 * @author Maisha
 */
public class GpsCoordsCluster {
    private IMJ_OC<GpsCoordinate> _coords;
    private IMJ_OC<IGpsCoordsClusterObserver> _observers = new MJ_OC_Factory<IGpsCoordsClusterObserver>().create();
    
    public GpsCoordsCluster(GpsCoordinate c){
        _coords = new MJ_OC_Factory<GpsCoordinate>().create();
        _coords.append(c);
    }
    
    public void addCoordToCluster(GpsCoordinate c){
        _coords.append(c);
        for (int i = 0; i<_observers.length(); i++){
            _observers.getItem(i).notifyCoordAdded(c);
        }
    }
    
    //void removeOutliers(){}
    
    public int length(){
        return _coords.length();
    }
    
    public GpsCoordinate getCoordAtIdx(int i){
        return _coords.getItem(i);
    }
    
    public IGpsCoordsClusterObserver getSpecifiedObserver(Class<?> cl){
        IGpsCoordsClusterObserver o;
        for (int i = 0; i<_observers.length(); i++){
            o = _observers.getItem(i);
            if (cl.isAssignableFrom(o.getClass())){
                return o;
            }
        }
        return null;
    }
    
    public void registerObserver(IGpsCoordsClusterObserver o){
        _observers.append(o);
    }
    
    /*GpsCoordinate getCenter2(){
        //get avg of lats and avg of lons to find center of this cluster
        double lats = 0;
        double lons = 0;
        GpsCoordinate c;
        int len = _coords.length();
        for (int i = 0; i<len; i++){
            c = _coords.getItem(i);
            lats += c.getLat();
            lons += c.getLon();
        }
        lats /= len;
        lats = Math.round(lats * 10000000d) / 10000000d;  // this is to get 7 decimal places
        lons /= len;
        lons = Math.round(lons * 10000000d) / 10000000d;  // this is to get 7 decimal places
        c = GpsCoordinate.getCoordFromLatLon(lats, lons);
        return c;
    }*/
}
