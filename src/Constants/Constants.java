package Constants;

/**
 *
 * @author Maisha Jauernig
 */
public class Constants {
    
    public static final String DIRECTORY_PATH = 
            "C:\\Users\\Bilal\\Desktop\\eclipseProjects\\Data\\06-04-2019_167";
            //"C:\\Users\\Bilal\\Desktop\\eclipseProjects\\Data\\08-27-2019_89";  // format v 2
    		//"C:\\Users\\Maisha\\Dropbox\\MB_various\\UNL\\Data\\08-27-2019_89";  // format v 2
    		//"D:\\Dropbox\\MB_various\\UNL\\Data\\02-21-2019_120";
    
    // base version is 1
    public static final int DEFAULT_FORMAT_VERSION = 1;
    public static final boolean TESTING_GPS_ONLY = true;

    //*********ANALAYSIS VARIABLES*********
    // for GPS data eval
    public final static double PERCENT_ALLOWED_DEVIATION_FROM_SI = 0.01;
    // for while at rules eval
    public static final int GPS_SENSOR_ID = 12;
    public static final double PERCENT_ALLOWED_DEVIATION_FROM_REQ_RULE_FIRE_TIME = 0.1;
    // when an answer fired too late, after what percentage of the minT 
    // is it considered missed entirely rather than fired late
    public static final double PERCENT_CUTOFF_OF_MINT_FOR_LATE_ANS = 0.8;
    
    //*********CSV FILE NAMES & COLUMNS*********
    public static final String STUDY_CSV = "study.csv";
    public static final int STUDY_NUM_COLS = 15;
    public static final int STUDY_NUM_COLS_V2 = 18;
    public static final int STUDY_STUDYID_IDX = 0;
    
    public static final String SENSORTYPES_CSV = "sensortypes.csv";
    public static final int SENSORTYPES_NUM_COLS = 4;
    public static final int SENSORTYPES_SENSORID_IDX = 0;
    public static final int SENSORTYPES_SENSORNAME_IDX = 1;
    public static final int SENSORTYPES_DESCRIPTION_IDX = 2;
    public static final int SENSORTYPES_SCHEMA_IDX = 3;
    
    public static final String COUPON_CSV = "coupon.csv";
    public static final String COUPON_CONSENTSTATUS_CONSENTAGREED = "consentAgreed"; 
    public static final int COUPON_NUM_COLS = 21;
    public static final int COUPON_NUM_COLS_V2 = 23;
    public static final int COUPON_COUPONID_IDX = 0;
    public static final int COUPON_CONSENTSTATUS_IDX = 5; 
    
    public static final String RULES_CSV = "rules.csv";
    public static final String RULES_RULEPARAMTOVALUE = "ruleParamToValue";
    public static final String RULES_DISTANCE = "distance";
    public static final String RULES_LATITUDE = "latitude";
    public static final String RULES_LONGITUDE = "longitude";
    public static final String RULES_MINTLASTFIRE = "mintimesincelastfire";
    public static final int RULES_NUM_COLS = 13;
    public static final int RULES_NUM_COLS_V2 = 14;
    public static final int RULES_RULEID_IDX = 0;
    public static final int RULES_QUESTIONID_IDX = 1;
    public static final int RULES_RULETYPE_IDX = 2;
    public static final int RULES_RULETEXT_IDX = 3;  // this is broken/weird with commas
    public static final String RULE_WHILEAT_NAME = "whileAt";
    
    public static final String ANSWERS_CSV = "answers.csv";
    public static final int ANSWERS_NUM_COLS = 10;
    public static final int ANSWERS_COUPONID_IDX = 0;
    public static final int ANSWERS_RULEID_IDX = 1;
    public static final int ANSWERS_QUESTIONID_IDX = 2;
    public static final int ANSWERS_CHOICEID_IDX = 3;
    public static final int ANSWERS_ANSWERTEXT_IDX = 4;
    public static final int ANSWERS_RULEFIREDTIME_IDX = 7;

    public static final int SENSOR_NUM_COLS = 5;
    public static final int SENSOR_COUPONID_IDX = 0;
    public static final int SENSOR_SENSORID_IDX = 1;
    public static final int SENSOR_GPS_LAT_IDX = 2;
    public static final int SENSOR_GPS_LON_IDX = 3;
    public static final int SENSOR_GPS_TIME_IDX = 4;
    
    public static final String STUDYTOSENSOR_CSV = "studytosensor.csv";
    public static final int STUDYTOSENSOR_NUM_COLS = 4;
    public static final int STUDYTOSENSOR_STUDYID_IDX = 0;
    public static final int STUDYTOSENSOR_SENSORID_IDX = 1;
    public static final int STUDYTOSENSOR_GPS_TIME_IDX = 2;
    public static final String STUDYTOSENSOR_GPS_TIME_LABEL = "time";
    public static final int STUDYTOSENSOR_GPS_DISTANCE_IDX = 3;
    public static final String STUDYTOSENSOR_GPS_DISTANCE_LABEL = "distance";
    public static final String STUDYTOSENSOR_BEACON_INTERVAL_LABEL = "interval";
    public static final String STUDYTOSENSOR_BT_INTERVAL_LABEL = "interval";
    
    public static final String HEALTH_CODEBOOK_CSV = "health_codebook.csv";
    public static final String HEALTH_CODEBOOK_COLUMN_LABELS = "health_report_column_label,variable_tag,variable_description,variable_related_data";
    public static final String HEALTH_REPORT_CSV = "health_report.csv";
    public static final String HEALTH_REPORT_NO_VALUE = "NA";
}
