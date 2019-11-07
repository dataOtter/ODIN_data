package cron;

public class ConstantsRE {

	public static final int MONDAY = 1;

	public static final int TUESDAY = 2;

	public static final int WEDNESDAY = 3;

	public static final int THURSDAY = 4;

	public static final int FRIDAY = 5;

	public static final int SATURDAY = 6;

	public static final int SUNDAY = 7;
	
	//Min and Max Values
	
	public static final int MINVALUE_MINUTE=0;
	public static final int MAXVALUE_MINUTE=59;
	
	public static final int MINVALUE_HOUR=0;
	public static final int MAXVALUE_HOUR=23;
	
	public static final int MINVALUE_DAYOFMONTH=1;
	public static final int MAXVALUE_DAYOFMONTH=31;
	
	public static final int MINVALUE_MONTH=1;
	public static final int MAXVALUE_MONTH=12;
	
	public static final int MINVALUE_DAYOFWEEK=1;
	public static final int MAXVALUE_DAYOFWEEK=7;

	public static final int AM = 0;
	public static final int PM = 1;
    
	/* ------------------------- RULE TYPES CONSTANTS ------------------- */
	public static final String PERIODICRULE_FE = "periodicRule_FE";
	public static final String ABSOLUTERULE_FE = "absoluteRule_FE";
	public static final String CRONRULE_FE = "cronRule_FE";
	public static final String QUESTIONOBSERVERRULE_FE = "questionObserverRule_FE";
	public static final String UIOBSERVERRULE_FE = "uiObserverRule_FE";
	public static final String ASYNC_ACTIVITY_RECOGNITION_RULE_FE = "asyncActivityRecognitionRule_FE";
	public static final String ASYNCE4ACCELERATIONRULE_FE = "asyncE4AccelerationRule_FE";
	public static final String ONARRIVAL_FE = "onArrival_FE";
	public static final String ONDEPARTURE_FE = "onDeparture_FE";
	public static final String WHILEAT_FE = "whileAt_FE";
	public static final String WHILENOTAT_FE = "whileNotAt_FE";
	public static final String ONARRIVAL_BLUETOOTH_FE = "onArrival_Bluetooth_FE";
	public static final String ONDEPARTURE_BLUETOOTH_FE = "onDeparture_Bluetooth_FE";
	public static final String TIME_SINCE_ENROLLMENT_FE="timeSinceEnrollment_FE";
	public static final String END_OF_STUDY_FE="endOfStudy_FE";
	public static final String END_OF_STUDY_CHAIN_FE="endOfStudyChain_FE";
	public static final String WHILEAT_BLUETOOTH_FE = "whileAt_Bluetooth_FE";
	public static final String WHILENOTAT_BLUETOOTH_FE = "whileNotAt_Bluetooth_FE";
	public static final String ONARRIVAL_BEACON_FE="onArrival_Beacon_FE";
	public static final String ONDEPARTURE_BEACON_FE="onDeparture_Beacon_FE";
	public static final String ONARRIVAL_ACTIVITY_FE="onArrival_Activity_FE";
	public static final String ONDEPARTURE_ACTIVITY_FE="onDeparture_Activity_FE";
	public static final String WHILEAT_ACTIVITY_FE="whileAt_Activity_FE";
	public static final String WHILENOTAT_ACTIVITY_FE="whileNotAt_Activity_FE";
	public static final String WHILEAT_BEACON_FE = "whileAt_Beacon_FE";
	public static final String WHILENOTAT_BEACON_FE = "whileNotAt_Beacon_FE";
	
	public static final String PERIODICRULE_BE = "periodicRule_BE";
	public static final String ABSOLUTERULE_BE = "absoluteRule_BE";
	public static final String CRONRULE_BE = "cronRule_BE";
/*	public static final String PERIODICLOCATIONBASEDRULE_BE = "periodicLocationBasedRule_BE";	
	public static final String CRONLOCATIONBASEDRULE_BE = "cronLocationBasedRule_BE";
	public static final String NEGATIONLOCATIONBASEDRULE_BE = "cronNegationLocationBasedRule_BE";*/
	public static final String PHONEWAKEUPCRONRULE = "phoneWakeUpCronRule";
	public static final String QUESTIONOBSERVERRULE_BE = "questionObserverRule_BE";
	public static final String PERIODIC_ACTIVITY_RECOGNITION_RULE_BE = "periodicActivityRecognitionRule_BE";
	public static final String ONARRIVAL_BE = "onArrival_BE";
	public static final String ONDEPARTURE_BE = "onDeparture_BE";
	public static final String WHILEAT_BE = "whileAt_BE";
	public static final String WHILENOTAT_BE = "whileNotAt_BE";
	public static final String ONARRIVAL_BLUETOOTH_BE = "onArrival_Bluetooth_BE";
	public static final String ONDEPARTURE_BLUETOOTH_BE = "onDeparture_Bluetooth_BE";
	public static final String WHILEAT_BLUETOOTH_BE = "whileAt_Bluetooth_BE";
	public static final String WHILENOTAT_BLUETOOTH_BE = "whileNotAt_Bluetooth_BE";
	public static final String ONARRIVAL_BEACON_BE = "onArrival_Beacon_BE";
	public static final String ONDEPARTURE_BEACON_BE = "onDeparture_Beacon_BE";
	public static final String ONARRIVAL_ACTIVITY_BE="onArrival_Activity_BE";
	public static final String ONDEPARTURE_ACTIVITY_BE="onDeparture_Activity_BE";
	public static final String WHILEAT_ACTIVITY_BE="whileAt_Activity_BE";
	public static final String WHILENOTAT_ACTIVITY_BE="whileNotAt_Activity_BE";
	public static final String WHILEAT_BEACON_BE = "whileAt_Beacon_BE";
	public static final String WHILENOTAT_BEACON_BE = "whileNotAt_Beacon_BE";
	public static final String TIME_SINCE_ENROLLMENT_BE="timeSinceEnrollment_BE";
    public static final int CRON_MAX_YEARS_TO_CHECK = 2;

	
	/* ------------------------- FILTER TYPES CONSTANTS ------------------- */
	
	public static final String timeFilter = "timeFilter";
	public static final String locationFilter="locationFilter";
	public static final String answerFilter="answerFilter";

	public static final double TESTER_TIMER_MINUTES = 300;
	
	public static final double TEST_DURATION_MINUTES =   300;
	
	public static final double MAX_DURATION_MINUTES = TEST_DURATION_MINUTES/3.0;
	
	public static final double SCHEDULAR_SPEED = (TEST_DURATION_MINUTES/TESTER_TIMER_MINUTES);
	
	public static final int  NUM_OF_PERIODIC_RULES = 10;	
	
	public static final int MAXVARIATIONINMILLIS = 5000 ;
	
	public static final String RULE_FIRED_LOCAL = "LOCAL";
	
	public static final String RULE_FIRED_REMOTE = "REMOTE";
	
	public static final String RULES_LABEL = "name";
	
	public static final int MAX_ERROR_PERCENTAGE = 50;
	
	public static final int LOWER_BOUNDARY_MINTRUEPERIOD = 0;
	public static final int LOWER_BOUNDARY_MINFALSEPERIOD=0;
	public static final int LOWER_BOUNDARY_MINTIMESINCELASTFIREDTIME=0;
	
	public static final String QUESTION_OBSERVER_RULE_ANY_CHOICE = "*";

	public static final int FIRST_SENSOR_ROW_SUBTRACT_MINUTES = 15;
	
	/* ------------------------ VALIDATION HINTS ---------------------------- */
	public static final String VALIDATION_HINT_STRING = "String";
	public static final String VALIDATION_HINT_INT = "int";
	public static final String VALIDATION_HINT_DOUBLE = "double";
	public static final String VALIDATION_HINT_LIST_QUESTIONS = "list_questions";
	public static final String VALIDATION_HINT_LIST_CHOICES = "list_choices";
	public static final String VALIDATION_HINT_MAP = "map";
	public static final String VALIDATION_HINT_RADIO = "radio";

}