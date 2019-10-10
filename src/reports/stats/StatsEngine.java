package reports.stats;

import constants.ConstTags;
import dao.CouponReader;
import orderedcollection.IMJ_OC;
import reports.ReportsCollection;

/**
 * @author Maisha Jauernig
 *
 */
public class StatsEngine {

    private final String _path;
    private final int _formatVersion;
	private final ReportsCollection _allReports;
	private final boolean _doRules;
	private final boolean _doSensors;
	
	StatsEngine(String path, int formatVersion, ReportsCollection allReports, boolean doRules, boolean doSensors) {
        _path = path;
        _formatVersion = formatVersion;
        _allReports = allReports;
        _doRules = doRules;
        _doSensors = doSensors;
	}
	
	public ReportsCollection getStats() {

        ReportsCollection stats = new ReportsCollection();
        IMJ_OC<Integer> cidList = new CouponReader(_path, _formatVersion).getActiveCouponIds();
        
        for (int cid: cidList) {
        	
        	if (_doRules) {
            	ReportsCollection justRules = _allReports.extractAllRuleReportsForOneCid(cid);
            	OneStatsReport rep1 = new StatsOfReportsCollection(justRules).getValues();
            	stats.addAnalysisByType(ConstTags.REPORT_TYPE_ALL_RULES_ANALYSIS, rep1);
        	}
        	
        	if (_doSensors) {
            	ReportsCollection justSensors = _allReports.extractAllSensorReportsForOneCid(cid);
            	OneStatsReport rep2 = new StatsOfReportsCollection(justSensors).getValues();
            	stats.addAnalysisByType(ConstTags.REPORT_TYPE_ALL_SENSORS_ANALYSIS, rep2);
        	}
        }
        return stats;
	}
}
