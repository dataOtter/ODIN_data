package sensors.params;

/**
 *
 * @author Maisha Jauernig
 */
public class JsonSensorparams {
    private String time;  // these must not have underscore as they must exactly reflect the naming in the csv
    private String distance;
    
    public Double getTimeInterval(){
        return Double.parseDouble(time);
    }
    
    public Double getDistance(){
        return Double.parseDouble(distance);
    }
}
