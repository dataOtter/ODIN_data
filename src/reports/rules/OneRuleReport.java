package reports.rules;

import orderedcollection.IMJ_OC;
import reports.OneReport;

/**
 *
 * @author Maisha Jauernig
 */
public class OneRuleReport extends OneReport {
	public OneRuleReport(IMJ_OC<String> relatedDataNames) {
		super(false, true, relatedDataNames);
	}
}
