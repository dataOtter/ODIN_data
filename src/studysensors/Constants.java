package studysensors;

/**
 *
 * @author Maisha
 */
public class Constants {
    
    public static final String DIRECTORY_PATH = 
            //"C:\\Users\\Bilal\\Dropbox\\MB_various\\UNL\\Data\\06-04-2019_167";
            "C:\\Users\\Bilal\\Dropbox\\MB_various\\UNL\\Data\\08-27-2019_89";
    		//"D:\\Dropbox\\MB_various\\UNL\\Data\\02-21-2019_120";
    
    // base version is 1
    public static final int DEFAULT_FORMAT_VERSION = 2;
    public static final boolean TESTING_GPS_ONLY = true;

    //*********ANALAYSIS VARIABLES*********
    // for GPS data eval
    public final static double PERCENT_ALLOWED_DEVIATION_FROM_SI = 0.01;
    public static final String PERC_ALLOWED_DEV_FROM_SI = "percAllowedDevFromSI";
    
    // for while at rules eval
    public static final int GPS_SENSOR_ID = 12;
    public static final double PERCENT_ALLOWED_DEVIATION_FROM_REQ_RULE_FIRE_TIME = 0.1;
    public static final String PERC_ALLOWED_DEV_FROM_RULE_FIRE_TIME = "percAllowedDevFromRuleFireTime";
    // when an answer fired too late, after what percentage of the minT 
    // is it considered missed entirely rather than fired late
    public static final double PERCENT_CUTOFF_OF_MINT_FOR_LATE_ANS = 0.8;
    public static final String PERC_CUTOFF_MINT_LATE_ANS = "percTimeAllowedToBeLate";
    
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
    public static final int COUPON_NUM_COLS = 21;
    public static final int COUPON_NUM_COLS_V2 = 23;
    public static final int COUPON_COUPONID_IDX = 0;
    public static final int COUPON_CONSENTSTATUS_IDX = 5; 
    
    public static final String RULES_CSV = "rules.csv";
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
    public static final int STUDYTOSENSOR_GPS_DISTANCE_IDX = 3;
    
    //**********REPORT TAGS**********
    public static final String REPORT_TYPE_GPS_SENSOR_ANALYSIS = "gpsSensor";
    public static final String REPORT_TYPE_WHILEAT_RULE_ANALYSIS = "whileAtRule";
    public static final String REPORT_TYPE_ALL_RULES_ANALYSIS = "allRules";
    public static final String REPORT_TYPE_ALL_SENSORS_ANALYSIS = "allSensors";
    
    public static final String REPORTS_RULEID = "ruleId";
    public static final String REPORTS_COUPONID = "couponId";
    public static final String REPORTS_SENSORID = "sensorId";
    public static final String REPORTS_STUDYID = "studyId";
    
    public static final String REPORTS_SENSOR_INTERVAL = "sensorInterval";
    public static final String REPORTS_GOOD_FIRE_PERCENT = "goodFirePercentOfIdeal";
    public static final String REPORTS_GOOD_FIRE_PERCENT_OF_TOTAL = "goodFirePercentOfTotal";
    public static final String REPORTS_AVERAGE_RULES = "avgRulesAsPercentGoodFires";  // values close to 100 are good
    public static final String REPORTS_AVERAGE_ONE_SENSOR = "avgOneSensorAsPercOfSi";  // values close to 100 are good
    public static final String REPORTS_AVERAGE_ONE_SENSOR_IN_SECS = "avgOneSensorInSecs";
    public static final String REPORTS_AVERAGE_ALL_SENSORS = "avgAllSensorAsPercOfSi";
    // e.g. avg is 88% with std dev 3 percent points
    public static final String REPORTS_STANDARD_DEVS_RULES = "stdevRulesAsDevFromGoodFiresPerc";  
    // as true percent of SI, e.g. avg is 102% of SI with stdev of 50% of SI, 
    // so if e.g. SI is 300 secs, avg is 306 secs and stdev is 150 secs
    public static final String REPORTS_STANDARD_DEV_ONE_SENSOR = "stdevOneSensorAsPercentOfSi";  
    public static final String REPORTS_STANDARD_DEV_ALL_SENSORS = "stdevAllSensorsAsPercentOfSi";
    
    public static final String REPORTS_ALL_CIDS_DATA = "allCidsData";
    public static final String REPORTS_STANDARD_DEV_ALL_CIDS_SENSORS = "stdevAllSensorsAllCids";
    public static final String REPORTS_STANDARD_DEV_ALL_CIDS_RULES = "stdevAllRulesAllCids";
    public static final String REPORTS_AVERAGE_ALL_CIDS_SENSORS = "avgAllSensorsAllCids";
    public static final String REPORTS_AVERAGE_ALL_CIDS_RULES = "avgAllRulesAllCids";
    
    public static final String REPORTS_RULE_MIN_T = "ruleMinTBetweenFires";
    public static final String REPORTS_GOOD_RULE_FIRES = "numGoodRuleFires";
    public static final String REPORTS_IDEAL_RULE_FIRES = "numIdealRuleFires";
    public static final String REPORTS_TOTAL_RULE_FIRES = "numTotalRuleFires";
    public static final String REPORTS_LATE_RULE_FIRES = "numLateRuleFires";
    public static final String REPORTS_EARLY_RULE_FIRES = "numEarlyRuleFires";
    public static final String REPORTS_MISSED_RULE_FIRES = "numMissedRuleFires";
    public static final String REPORTS_OTHER_RULE_FIRES = "numNotMissedLateEarlyGoodRuleFires";
    public static final String REPORTS_LATEORMISSED_ANS = "lateOrMissedAns";
    public static final String REPORTS_EARLY_ANS = "earlyAns";
    
    public static final String REPORTS_TOTAL_SENSOR_RECS = "numTotalSensorRecordings";
    public static final String REPORTS_SENSOR_RECS_WITHIN_DEV = "numSensorRecsWithinAllowedDevOFSi";
    public static final String REPORTS_SENSOR_RECS_PERC_WITHIN_DEV = "numSensorRecsWithinAllowedDevOFSiPerc";
    public static final String REPORTS_MINT_BTW_SENSOR_RECS = "minTimeBetweenSensorRecordings";
    public static final String REPORTS_MINT_BTW_SENSOR_RECS_AS_PERC = "minTimeBetweenSensorRecsAsPercOfSi";
    public static final String REPORTS_MAXT_BTW_SENSOR_RECS = "maxTimeBetweenSensorRecordings";
    public static final String REPORTS_MAXT_BTW_SENSOR_RECS_AS_PERC = "maxTimeBetweenSensorRecsAsPercOfSi";
}
