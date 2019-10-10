package sensors;

import constants.Constants;

/**
 *
 * @author Maisha Jauernig
 */
public class SensorType {
    private Integer _id;
    private String _name;
    private String _description;
    private String _schema;
    private String _tblName;
    
    public SensorType(String[] line){
    	_id = Integer.parseInt(line[Constants.SENSORTYPES_SENSORID_IDX]);
    	_name = line[Constants.SENSORTYPES_SENSORNAME_IDX];
    	_schema = line[Constants.SENSORTYPES_SCHEMA_IDX];
    	_description = line[Constants.SENSORTYPES_DESCRIPTION_IDX];
    	
    	String[] a = _schema.split(" ");  // split the SQL on spaces
        String s = a[2];  // to get the part that contains the table name (without "create table ")
        a = s.split("\\(");  // split that part on open parenthesis
        _tblName = a[0];  // to get the table name 
    }
    
    public Integer getSensorId(){
        return _id;
    }
    
    public String getSensorName(){
        return _name;
    }
    
    public String getSensorTblName(){
        return _tblName;
    }
    
    public String getSchema(){
        return _schema;
    }
    
    public String getDescr(){
        return _description;
    }
}
