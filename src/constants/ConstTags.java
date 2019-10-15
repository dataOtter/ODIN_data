/**
 * 
 */
package constants;

import maps.*;
import reports.OneReport;

/**
 * @author Maisha Jauernig
 *
 */
public class ConstTags {
	
	public static final String REPORT_TYPE_WHILEAT_RULE_ANALYSIS = "whileAtRule";
	public static final String REPORT_TYPE_ALL_RULES_ANALYSIS = "allRules";
	public static final String REPORT_TYPE_ALL_SENSORS_ANALYSIS = "allSensors";
	public static final IMJ_Map<Integer, String> SENSORID_TO_TYPE = getSidToTypeMap();
	
	private static IMJ_Map<Integer, String> getSidToTypeMap() {
		IMJ_Map<Integer, String> sidToTypeMap = new MJ_Map_Factory<Integer, String>().create();
		sidToTypeMap.put(12, "gpsSensor");
		sidToTypeMap.put(3, "bluetoothSensor");
		sidToTypeMap.put(10, "beaconSensor");
		sidToTypeMap.put(13, "activitySensor");
		return sidToTypeMap;
	}

	// underscore before tag means it is not averageable data - not relevant for full-text descriptions
	public static final String REPORTS_PERC_ALLW_DEV_FRM_RULE_FIRE_T = "_percAllowedDevFromRuleFireTime";
	public static final String REPORTS_P_A_D_F_R_F_T_TEXT = "Allowed time deviation from minimum time between fires";
	public static final String REPORTS_PERC_CUTOFF_MINT_LATE_ANS = "_percTimeAllowedToBeLate";
	public static final String REPORTS_P_C_M_L_A_TEXT = "Percent of minimum time between rule fires cutoff for being late allowance";
	public static final String REPORTS_PERC_ALLW_DEV_FRM_SI = "_percAllowedDevFromSI";
	public static final String REPORTS_P_A_D_F_SI_TEXT = "Maximum percent deviation from requested sensor interval";
	
	public static final String REPORTS_RULEID = "_ruleId";
	public static final String REPORTS_COUPONID = "_couponId";
	public static final String REPORTS_SENSORID = "_sensorId";
	public static final String REPORTS_STUDYID = "_studyId";
	
	public static final String REPORTS_SENSOR_INTERVAL = "_sensorInterval";
	public static final String REPORTS_S_I_TEXT = "Researcher requested sensor time interval";
	public static final String REPORTS_GOOD_FIRE_PERCENT = "goodFirePercentOfIdeal";  // averageable
	public static final String REPORTS_G_F_P_TEXT = "Good rule fire percentage out of ideal world rule fires";
	public static final String REPORTS_GOOD_FIRE_PERCENT_OF_TOTAL = "goodFirePercentOfTotal";  // averageable
	public static final String REPORTS_G_F_P_O_T_TEXT = "Good rule fire percentage out of all rule fires";
	public static final String REPORTS_AVERAGE_RULES = "avgRulesAsPercentGoodFires";  // values close to 100 are good  // averageable
	public static final String REPORTS_AVERAGE_ONE_SENSOR = "avgOneSensorAsPercOfSi";  // values close to 100 are good  // averageable
	public static final String REPORTS_A_O_S_TEXT = "Average percent deviation of actual time between GPS recordings from sensor interval";
	public static final String REPORTS_AVERAGE_ONE_SENSOR_IN_SECS = "avgOneSensorInSecs";  // averageable
	public static final String REPORTS_A_O_S_I_S_TEXT = "Average time in seconds between actual GPS recordings";
	public static final String REPORTS_AVERAGE_ALL_SENSORS = "avgAllSensorAsPercOfSi";  // averageable
	// e.g. avg is 88% with std dev 3 percent points
	public static final String REPORTS_STANDARD_DEVS_RULES = "stdevRulesAsDevFromGoodFiresPerc";  // averageable
	// as true percent of SI, e.g. avg is 102% of SI with stdev of 50% of SI, 
	// so if e.g. SI is 300 secs, avg is 306 secs and stdev is 150 secs
	public static final String REPORTS_STANDARD_DEV_ONE_SENSOR = "stdevOneSensorAsPercentOfSi";  // averageable
	public static final String REPORTS_S_D_O_S_TEXT = "Standard deviation (in percent of SI) between actual GPS recordings";
	public static final String REPORTS_STANDARD_DEV_ALL_SENSORS = "stdevAllSensorsAsPercentOfSi";  // averageable
	
	//public static final String REPORTS_ALL_CIDS_DATA = "allCidsData";
	public static final String REPORTS_STANDARD_DEV_ALL_CIDS_SENSORS = "stdevAllSensorsAllCids";  // averageable
	public static final String REPORTS_STANDARD_DEV_ALL_CIDS_RULES = "stdevAllRulesAllCids";  // averageable
	public static final String REPORTS_AVERAGE_ALL_CIDS_SENSORS = "avgAllSensorsAllCids";  // averageable
	public static final String REPORTS_AVERAGE_ALL_CIDS_RULES = "avgAllRulesAllCids";  // averageable
	
	public static final String REPORTS_RULE_MIN_T = "_ruleMinTBetweenFires";
	public static final String REPORTS_R_M_T_TEXT = "Rule required minimum time between fires";
	public static final String REPORTS_GOOD_RULE_FIRES = "_numGoodRuleFires";
	public static final String REPORTS_G_R_F_TEXT = "Number of good rule fires";
	public static final String REPORTS_IDEAL_RULE_FIRES = "_numIdealRuleFires";
	public static final String REPORTS_I_R_F_TEXT = "Number of ideal world rule fires";
	public static final String REPORTS_TOTAL_RULE_FIRES = "_numTotalRuleFires";
	public static final String REPORTS_T_R_F_TEXT = "Number of total rule fires";
	public static final String REPORTS_LATE_RULE_FIRES = "numLateRuleFires";  // averageable
	public static final String REPORTS_L_R_F_TEXT = "Number of late rule fires (late by at most given % of minimum time between fires)";
	public static final String REPORTS_EARLY_RULE_FIRES = "numEarlyRuleFires";  // averageable
	public static final String REPORTS_E_R_F_TEXT = "Number of early rule fires";
	public static final String REPORTS_MISSED_RULE_FIRES = "numMissedRuleFires";  // averageable
	public static final String REPORTS_M_R_F_TEXT = "Number of missed rule fires (later than given % of minimum time between fires)";
	public static final String REPORTS_OTHER_RULE_FIRES = "numNotMissedLateEarlyGoodRuleFires";  // averageable
	public static final String REPORTS_O_R_F_TEXT = "Number of rule fires that were not early, late, missed, or on time";
	public static String REPORTS_LATEORMISSED_ANS(int i) {return "_lateOrMissedAns#" + i;}
	public static String REPORTS_LOM_A_TEXT(int i) {return "Late or missed answer occurence " + i + ", time in milliseconds";}
	public static String REPORTS_EARLY_ANS(int i) {return "_earlyAns#" + i;}
	public static String REPORTS_E_A_TEXT(int i) {return "Early answer occurence " + i + ", time in milliseconds";}

	public static final String REPORTS_TOTAL_SENSOR_RECS = "_numTotalSensorRecordings";
	public static final String REPORTS_T_S_R_TEXT = "Total GPS recordings";
	public static final String REPORTS_SENSOR_RECS_WITHIN_DEV = "_numSensorRecsWithinAllowedDevOFSi";
	public static final String REPORTS_S_R_W_D_TEXT = "Number of GPS recordings within given allowed deviation of sensor interval";
	public static final String REPORTS_PERC_SENSOR_RECS_WITHIN_DEV = "perSensorRecsWithinAllowedDevOFSi";  // averageable
	public static final String REPORTS_S_R_P_W_D_TEXT = "Percent of GPS recordings within given allowed deviation of sensor interval";
	public static final String REPORTS_MINT_BTW_SENSOR_RECS = "_minTimeBetweenSensorRecordings";
	public static final String REPORTS_MIN_B_S_R_TEXT = "Minimum/smallest time in seconds between actual GPS recordings";
	public static final String REPORTS_MINT_BTW_SENSOR_RECS_AS_PERC = "minTimeBetweenSensorRecsAsPercOfSi";  // averageable
	public static final String REPORTS_MIN_B_S_R_A_P_TEXT = "Percent deviation of smallest actual time between GPS recordings from sensor interval";
	public static final String REPORTS_MAXT_BTW_SENSOR_RECS = "_maxTimeBetweenSensorRecordings";
	public static final String REPORTS_MAX_B_S_R_TEXT = "Maximum/largest time in seconds between actual GPS recordings";
	public static final String REPORTS_MAXT_BTW_SENSOR_RECS_AS_PERC = "maxTimeBetweenSensorRecsAsPercOfSi";  // averageable
	public static final String REPORTS_MAX_B_S_R_A_P_TEXT = "Percent deviation of largest actual time between GPS recordings from sensor interval";
	
	public static final String REPORTS_REL_DATA_GPS = "GPS_sensor";
	public static final String REPORTS_REL_DATA_ANSWERS = "answers";
	
	public static String getTypeAndId(String type, OneReport rep) {
		String typeAndId = "";
		if (rep.isSensorReport()) {
			typeAndId = type + "_id_" + rep.getValue(REPORTS_SENSORID).intValue();
		}
		else if (rep.isRuleReport()) {
			typeAndId = type + "_id_" + rep.getValue(REPORTS_RULEID).intValue();
		}
		return typeAndId;
	}
}
