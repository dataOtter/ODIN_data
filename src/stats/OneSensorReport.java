package stats;

import orderedcollection.IMJ_OC;

public class OneSensorReport extends OneReport {
	public OneSensorReport(IMJ_OC<String> relatedDataNames) {
		super(true, false, relatedDataNames);
	}
}
