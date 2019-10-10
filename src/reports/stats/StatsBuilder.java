package reports.stats;

import reports.ReportsCollection;

/**
 *
 * @author Maisha Jauernig
 */
public class StatsBuilder {
    private final String _path;
    private final int _formatVersion;
	private final ReportsCollection _allReports;
	private boolean _doRules;
	private boolean _doSensors;


	public StatsBuilder(String path, int formatVersion, ReportsCollection allReports) {
        _path = path;
        _formatVersion = formatVersion;
        _allReports = allReports;
        _doRules = false;
        _doSensors = false;
	}
	
	public StatsBuilder enableRulesStats() {
		_doRules = true;
		return this;
	}

	public StatsBuilder enableSensorsStats() {
		_doSensors = true;
		return this;
	}
	
	public StatsEngine build() {
		return new StatsEngine(_path, _formatVersion, _allReports, _doRules, _doSensors);
	}
}
