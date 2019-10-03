package stats;

import orderedcollection.IMJ_OC;

/**
 *
 * @author Maisha Jauernig
 */
public class OneSensorReport extends OneReport {
	public OneSensorReport(IMJ_OC<String> relatedDataNames) {
		super(true, false, relatedDataNames);
	}
}
