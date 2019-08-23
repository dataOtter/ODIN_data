package stats;

import java.io.FileNotFoundException;

import dao.CouponIdReader;
import orderedcollection.IMJ_OC;
import studysensors.Constants;

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
	
	public ReportsCollection getStats() throws FileNotFoundException {

        ReportsCollection stats = new ReportsCollection();
        IMJ_OC<Integer> cid_list = new CouponIdReader(_path, _formatVersion).getActiveCouponIds();
        
        for (int i=0; i<cid_list.length(); i++) {
        	int cid = cid_list.getItem(i);
        	
        	if (_doRules) {
            	ReportsCollection justRules = _allReports.extractAllRuleReportsForOneCid(cid);
            	OneStatsReport rep1 = new StatsOfReportsCollection(justRules).getValues();
            	stats.addAnalysisByType(Constants.REPORT_TYPE_ALL_RULES_ANALYSIS, rep1);
        	}
        	
        	if (_doSensors) {
            	ReportsCollection justSensors = _allReports.extractAllSensorReportsForOneCid(cid);
            	OneStatsReport rep2 = new StatsOfReportsCollection(justSensors).getValues();
            	stats.addAnalysisByType(Constants.REPORT_TYPE_ALL_SENSORS_ANALYSIS, rep2);
        	}
        }
        return stats;
	}
}
