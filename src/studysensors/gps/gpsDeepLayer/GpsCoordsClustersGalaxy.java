package studysensors.gps.gpsDeepLayer;

import Assert.Assertion;
import maps.*;
import orderedcollection.*;

/**
 *
 * @author Maisha
 */
public class GpsCoordsClustersGalaxy {
    IMJ_OC<GpsCoordsCluster> _clusters;
    
    public GpsCoordsClustersGalaxy(){
        _clusters = new MJ_OC_Factory<GpsCoordsCluster>().create();
    }
    
    public GpsCoordsClustersGalaxy getNextGalaxy(){
        //loop through _clusters and get all cluster centers -- make map of center to cluster
        IMJ_Map<GpsCoordinate, GpsCoordsCluster> centerToCluster = getCenterToClusterMap();
        Assertion.test(centerToCluster.size() > 1, "Only one cluster left, cannot get next galaxy");
        
        //find the two closest center coordinates
        GpsCoordinate[] closestCenterCoords = getClosestClusterCenters(centerToCluster);
        
        //make a new cluster by merging those two clusters
        GpsCoordsCluster cluster = mergeClusters(centerToCluster.get(closestCenterCoords[0]), 
                centerToCluster.get(closestCenterCoords[1]));
        
        //update map by removing those 2 and adding the new cluster
        centerToCluster.remove(closestCenterCoords[0]);
        centerToCluster.remove(closestCenterCoords[1]);
        centerToCluster.put(new GpsCoordinate(0, 0), cluster);
        
        //put all clusters from the updated map into a new galaxy
        GpsCoordsClustersGalaxy g = makeGalaxyFromMapOfCentersToClusters(centerToCluster);
        return g;
    }
    
    public void makeFirstGalaxy(OneCouponsGpsData d){
        GpsCoordsCluster cluster;
        GpsCoordinate coord;
        IGpsCoordsClusterObserver obs;
        //loop through d and make a cluster for each GpsDataPoint
        for (int i = 0; i<d.length(); i++){
            coord = d.getDataAtIdx(i).getGpsCoord();
            cluster = new GpsCoordsCluster(coord);
            obs = new GpsCoordsClusterCenterCalc();
            cluster.registerObserver(obs);
            //put it in _clusters using addCluster
            _clusters.add(cluster);
        }
    }
    
    public int length(){
        return _clusters.size();
    }
    
    public GpsCoordsCluster getClusterAtIdx(int i){
        return _clusters.get(i);
    }
    
    private void addCluster(GpsCoordsCluster c){
        _clusters.add(c);
    }
    
    private double getCoordsDistance(GpsCoordinate a, GpsCoordinate b){
        final int R = 6371; // Radius of the earth
        double lat1 = a.getLat();
        double lon1 = a.getLon();
        double lat2 = b.getLat();
        double lon2 = b.getLon();
        
        // Using Haversine method as base to get distance, ignoring altitude
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double x = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double y = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
        
        double distance = R * y * 1000; // convert to meters
        
        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
    
    private GpsCoordsClustersGalaxy makeGalaxyFromMapOfCentersToClusters(IMJ_Map<GpsCoordinate, GpsCoordsCluster> m){
        GpsCoordsClustersGalaxy g = new GpsCoordsClustersGalaxy();
        //put all clusters from the map into a new galaxy using addCluster
        for (int i = 0; i<m.size(); i++){
        	GpsCoordinate coord = m.getKey(i);
        	GpsCoordsCluster cluster = m.get(coord);
            g.addCluster(cluster);
        }
        return g;
    }
    
    private GpsCoordinate[] getClosestClusterCenters(IMJ_Map<GpsCoordinate, GpsCoordsCluster> m){
        GpsCoordinate[] closestCoords = new GpsCoordinate[2];
        double tempClosest = 999999999.9;
        double distance;
        //find the two closest centers
        for (int i = 0; i<_clusters.size()-1; i++){
        	GpsCoordinate coord = m.getKey(i);
        	GpsCoordinate coord2 = m.getKey(i+1);
            distance = getCoordsDistance(coord2, coord);
            if (distance < tempClosest){
                tempClosest = distance;
                closestCoords[0] = coord;
                closestCoords[1] = coord2;
            }
        }
        return closestCoords;
    }
    
    private IMJ_Map<GpsCoordinate, GpsCoordsCluster> getCenterToClusterMap(){
        //loop through _clusters and get all cluster centers
        IMJ_Map<GpsCoordinate, GpsCoordsCluster> centerToCluster = 
                new MJ_Map_Factory<GpsCoordinate, GpsCoordsCluster>().create();
        for (GpsCoordsCluster cluster: _clusters) {
            IGpsCoordsClusterObserver o = cluster.getSpecifiedObserver(GpsCoordsClusterCenterCalc.class);
            GpsCoordsClusterCenterCalc cal = (GpsCoordsClusterCenterCalc) o;
            GpsCoordinate coord = cal.getCenter();
            centerToCluster.put(coord, cluster);
        }
        return centerToCluster;
    }
    
    private GpsCoordsCluster mergeClusters(GpsCoordsCluster c1, GpsCoordsCluster c2){
        GpsCoordsCluster c = new GpsCoordsCluster(c1.getCoordAtIdx(0));
        IGpsCoordsClusterObserver obs = new GpsCoordsClusterCenterCalc();
        c.registerObserver(obs);
        c.addCoordToCluster(c2.getCoordAtIdx(0));
        
        int len1 = c1.length();
        int len2 = c2.length();
        
        for (int i = 1; i<Math.min(len1, len2); i++){
            c.addCoordToCluster(c1.getCoordAtIdx(i));
            c.addCoordToCluster(c2.getCoordAtIdx(i));
        }
        
        if (len1 < len2){
            for (int i = len1; i<len2; i++){
                c.addCoordToCluster(c2.getCoordAtIdx(i));
            }
        }
        else if (len2 < len1){
            for (int i = len2; i<len1; i++){
                c.addCoordToCluster(c1.getCoordAtIdx(i));
            }
        }
        return c;
    }
}
