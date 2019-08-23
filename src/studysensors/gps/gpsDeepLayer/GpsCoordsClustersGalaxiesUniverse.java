package studysensors.gps.gpsDeepLayer;

import orderedcollection.*;

/**
 *
 * @author Maisha
 */
public class GpsCoordsClustersGalaxiesUniverse {
    IMJ_OC<GpsCoordsClustersGalaxy> _galaxies;
    
    public GpsCoordsClustersGalaxiesUniverse(OneCouponsGpsData d){
        //make first galaxy with d.length() clusters and add to _galaxies
        _galaxies = new MJ_OC_Factory<GpsCoordsClustersGalaxy>().create();
        GpsCoordsClustersGalaxy g = new GpsCoordsClustersGalaxy();
        g.makeFirstGalaxy(d);
        _galaxies.append(g);
        
        //while the galaxy with just one clusters is not made yet
        while (g.length() > 1){
            //make a galaxy at each level with d.length()-i clusters
            g = g.getNextGalaxy();
            //add each galaxy to _galaxies
            _galaxies.append(g);
        }
    }
    
    public GpsCoordsClustersGalaxy getGalaxyWithNClusters(int n){
        return _galaxies.getItem(_galaxies.length() - n);
    }
}
