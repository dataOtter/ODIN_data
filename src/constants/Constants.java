package constants;

/**
 *
 * @author Maisha Jauernig
 */
public class Constants {
    
    public static final String DIRECTORY_PATH = 
            //"C:\\Users\\Bilal\\Dropbox\\MB_various\\UNL\\Data\\06-04-2019_167";  // format v 1
            "C:\\Users\\Bilal\\Dropbox\\MB_various\\UNL\\Data\\10-29-2019_116";
    		//"C:\\Users\\Bilal\\Dropbox\\MB_various\\UNL\\Data\\02-21-2019_120\\t4.2fv2";
    		//"D:\\Dropbox\\MB_various\\UNL\\Data\\08-27-2019_89";  // format v 2
    		//"D:\\Dropbox\\MB_various\\UNL\\Data\\02-21-2019_120\\t4.2fv2";
    		//"D:\\Dropbox\\MB_various\\UNL\\Data\\10-29-2019_116"; 
    
    // base version is 1
    public static final int DEFAULT_FORMAT_VERSION = 2;
    public static final boolean TESTING_GPS_ONLY = true;

    //*********ANALAYSIS VARIABLES*********
    // for sensor data eval
    public final static double PERCENT_ALLOWED_DEVIATION_FROM_SI = 0.01;
    // for rules eval
    public static final double PERC_ALLOWED_DEV_FROM_GIVEN_TIME_ONTIME = 0.1;
    public static final double PERC_ALLOWED_DEV_FROM_GIVEN_TIME_NOTMISSED = 0.8;
    public static final double PERC_ALLOWED_DEV_FROM_GIVEN_TIME_ONTIME_CRON = 0.02;
    public static final double PERC_ALLOWED_DEV_FROM_GIVEN_TIME_NOTMISSED_CRON = 0.3;
    // when an answer fired too late, after what percentage of the minT 
    // is it considered missed entirely rather than fired late
    public static final double PERCENT_CUTOFF_OF_MINT_FOR_LATE_ANS = 0.8;
    
    //*********SENSOR IDs*********
    public static final int SENSORID_SMS = 1;
    public static final int SENSORID_CLOCK = 2;
	public static final int SENSORID_BT =  3;
	public static final int SENSORID_EMOTIVE = 4;
	public static final int SENSORID_CALLS = 5;
    public static final int SENSORID_EMPATICA = 6;
    public static final int SENSORID_ACTIGRAPH = 7;
    public static final int SENSORID_ACCELEROMETER = 8;
    public static final int SENSORID_MAGNETOMETER =  9;
    public static final int SENSORID_BEACON = 10;
    public static final int SENSORID_LIGHT = 11;
    public static final int SENSORID_GPS = 12;
	public static final int SENSORID_ACTIVITY = 13;
	
	//*********FILTER TYPE NAMES*********
	public static final String FILTERS_LIST = "filtersList";
	public static final String FILTER_PARAM_TO_VAL = "filterParamToValue";
	public static final String FILTER_TYPE = "filterType";
	public static final String FILTER_TIME = "timeFilter";
	public static final String FILTER_TIME_START = "minTime";
	public static final String FILTER_TIME_END = "maxTime";
	public static final String FILTER_LOCATION = "locationFilter";
	public static final String FILTER_LOCATION_DIST = "distance";
	public static final String FILTER_LOCATION_LAT = "latitude";
	public static final String FILTER_LOCATION_LON = "longitude";
	public static final String FILTER_QUESTION = "answerFilter";
	public static final String FILTER_QUESTION_QID = "questionId";
	public static final String FILTER_QUESTION_CHID = "choiceId";

    //*********CSV FILE NAMES & COLUMNS*********
    public static final String SERVICETOHEARTBEAT_CSV = "servicetoheartbeat.csv";
    public static final int SERVICETHB_NUM_COLS = 8;
    public static final int SERVICETHB_COUPONID_IDX = 1;
    public static final int SERVICETHB_SERVICEID_IDX = 2;
    public static final int SERVICETHB_LASTUPLOADTIME_IDX = 7;

    public static final String SERVICES_CSV = "services.csv";
    public static final int SERVICES_NUM_COLS = 2;
    public static final int SERVICES_SERVICEID_IDX = 0;
    public static final int SERVICES_SERVICENAME_IDX = 1;
    
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
    public static final int COUPON_COUPONNUMBER_IDX = 1;
    public static final int COUPON_CONSENTSTATUS_IDX = 5; 
    public static final int COUPON_LASTREGISTRATION_IDX = 12; 
    public static final int COUPON_LASTQUESTIONCALL_IDX = 13; 
    
    public static final String RULES_CSV = "rules.csv";
    public static final int RULES_NUM_COLS_V0 = 11;
    public static final int RULES_NUM_COLS_V1 = 13;
    public static final int RULES_NUM_COLS_V2 = 14;
    public static final int RULES_RULEID_IDX = 0;
    public static final int RULES_QUESTIONID_IDX = 1;
    public static final int RULES_RULETYPE_IDX = 2;
    public static final int RULES_RULETEXT_IDX = 3;  // this is broken/weird with commas
    public static final String RULE_BLUETOOTH = "Bluetooth";
    public static final String RULE_PARAM_TO_VAL = "ruleParamToValue";
    public static final String RULE_WHILEAT_NOTAT = "whileAt";
    public static final String RULE_ONARRIVAL = "onArrival";
    public static final String RULE_CRON = "cron";
    public static final String RULE_ONDEPARTURE = "onDeparture";
    public static final String RULE_ONBUTTON = "uiObserverRule";
    public static final String RULE_FOLLOWUP = "questionObserverRule";
    // rule parameter labels 
    // while at/while not at rule parameters: whileAt
    // on arrival/on departure rule parameters: onArrDep
    // on button press rule parameters: onButton
    // follow up rule parameters: followUp
    public static final String RULES_DISTANCE = "distance";  // whileAt, onArrDep
    public static final String RULES_LATITUDE = "latitude";  // whileAt, onArrDep
    public static final String RULES_LONGITUDE = "longitude";  // whileAt, onArrDep
    public static final String RULES_MINTLASTFIRE = "mintimesincelastfire";  // whileAt
    public static final String RULES_BUTTONID = "buttonId";  // onButton
    public static final String RULES_BUTTONLABEL = "buttonLabel";  // onButton
    public static final String RULES_QUESTIONID = "observableQuestionId";  // followUp
    public static final String RULES_CHOICESLIST = "choicesListForObservableQuestion";  // followUp
    public static final String RULES_CRONMANUAL = "cronRuleString_manual";  // cron
    public static final String RULES_CRON = "cronRuleString";  // cron
    public static final String RULES_COUNT = "count";  // onDepBt
    public static final String RULES_DELAY = "delay";  // onDepBt
    public static final String RULES_PROXIMITY = "closeness";  // onDepBt
    public static final String RULES_PROXIMITY_CLOSE = "closeto";  // onDepBt
    public static final String RULES_PROXIMITY_NEARBY = "nearby";  // onDepBt
    
    public static final String ANSWERS_CSV = "answers.csv";
    public static final int ANSWERS_NUM_COLS = 10;
    public static final int ANSWERS_COUPONID_IDX = 0;
    public static final int ANSWERS_RULEID_IDX = 1;
    public static final int ANSWERS_QUESTIONID_IDX = 2;
    public static final int ANSWERS_CHOICEID_IDX = 3;
    public static final int ANSWERS_ANSWERTEXT_IDX = 4;
    public static final int ANSWERS_RULEFIREDTIME_IDX = 7;
    public static final int ANSWERS_NOTIFICATIONRECEIVEDTIME_IDX = 8;

    public static final int SENSOR_NUM_COLS = 5;
    public static final int SENSOR_COUPONID_IDX = 0;
    public static final int SENSOR_SENSORID_IDX = 1;

    public static final String SENSOR_GPS_FIRST_DATA_NAME = "latitude";  // v2
    public static final int SENSOR_GPS_LAT_IDX = 2;
    public static final int SENSOR_GPS_LON_IDX = 3;
    public static final int SENSOR_GPS_TIME_IDX = 4;

    public static final String SENSOR_BT_FIRST_DATA_NAME = "deviceName";  // v2
    public static final int SENSOR_BT_FIRST_DEVNAME_IDX = 2;
    public static final int SENSOR_BT_FIRST_RAW_IDX = 3;
    public static final int SENSOR_BT_FIRST_SMOOTHED_IDX = 4;
    public static final int SENSOR_BT_TIME_IDX = 5;

    public static final String SENSOR_BEACON_FIRST_DATA_NAME = "";  // v2
    public static final int SENSOR_BEACON_TIME_IDX = 3;

    public static final String SENSOR_ACTIVITY_FIRST_DATA_NAME = "Unknown";  // v2
    public static final int SENSOR_ACTIVITY_UNKNOWN_IDX = 2;
    public static final int SENSOR_ACTIVITY_TILTING_IDX = 3;
    public static final int SENSOR_ACTIVITY_VEHICLE_IDX = 4;
    public static final int SENSOR_ACTIVITY_RUNNING_IDX = 5;
    public static final int SENSOR_ACTIVITY_BICYCLE_IDX = 6;
    public static final int SENSOR_ACTIVITY_FOOT_IDX = 7;
    public static final int SENSOR_ACTIVITY_WALKING_IDX = 8;
    public static final int SENSOR_ACTIVITY_STILL_IDX = 9;
    public static final int SENSOR_ACTIVITY_TIME_IDX = 10;
    public static final String[] SENSOR_ACTIVITY_NAMES = {"unknown", "tilting", "inVehicle", "running", "onBicycle", "onFoot", "walking", "still"};
    public static final int[] SENSOR_ACTIVITY_IDICES = {2,3,4,5,6,7,8,9};
    
    public static final String STUDYTOSENSOR_CSV = "studytosensor.csv";
    public static final int STUDYTOSENSOR_NUM_COLS = 4;
    public static final int STUDYTOSENSOR_STUDYID_IDX = 0;
    public static final int STUDYTOSENSOR_SENSORID_IDX = 1;
    public static final int STUDYTOSENSOR_GPS_TIME_IDX = 2;
    public static final int STUDYTOSENSOR_GPS_DISTANCE_IDX = 3;
    // sensor parameter labels
    // GPS, Beacon, BT
    public static final String STUDYTOSENSOR_DISTANCE = "distance";  // gps
    public static final String STUDYTOSENSOR_INTERVAL = "interval";  // bt, beacon
    public static final String STUDYTOSENSOR_TIME = "time";  // gps
    public static final String STUDYTOSENSOR_MAJOR = "major";  // beacon
    public static final String STUDYTOSENSOR_MINOR = "minor";  // beacon
    public static final String STUDYTOSENSOR_PASSWORD = "password";  // beacon
    public static final String STUDYTOSENSOR_STANDARD = "standard";  // beacon
    public static final String STUDYTOSENSOR_UUID = "uuid";  // beacon
    
    public static final String HEALTH_CODEBOOK_CSV = "health_codebook.csv";
    public static final String HEALTH_CODEBOOK_COLUMN_LABELS = "health_report_column_label,variable_tag,variable_description,variable_related_data";
    public static final String HEALTH_REPORT_CSV = "health_report.csv";
    public static final String HEALTH_REPORT_NO_VALUE = "NA";
}
