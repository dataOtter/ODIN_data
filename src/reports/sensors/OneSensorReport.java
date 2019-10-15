package reports.sensors;

import orderedcollection.IMJ_OC;
import reports.OneReport;

/**
 *
 * @author Maisha Jauernig
 */
public class OneSensorReport extends OneReport {
	public OneSensorReport(IMJ_OC<String> relatedDataNames) {
		super(true, false, relatedDataNames);
	}
}
