package stats;

import orderedcollection.IMJ_OC;

/**
 *
 * @author Maisha Jauernig
 */
public class OneRuleReport extends OneReport {
	public OneRuleReport(IMJ_OC<String> relatedDataNames) {
		super(false, true, relatedDataNames);
	}
}
