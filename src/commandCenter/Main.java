package commandCenter;

import java.io.IOException;
import java.text.ParseException;

import constants.Constants;
import dao.OutputFileWriter;
import reports.AnalysisEngine;
import reports.AnalysisEngineBuilder;
import reports.ReportsCollection;
import reports.stats.StatsBuilder;
import reports.stats.StatsEngine;

/**
 *
 * @author Maisha Jauernig
 */
public class Main {

    public static void main(String[] args) throws ParseException, IOException {
    	
        String path = Constants.DIRECTORY_PATH;
        int formatVersion = Constants.DEFAULT_FORMAT_VERSION;
        		
        AnalysisEngineBuilder bld = new AnalysisEngineBuilder(path, formatVersion);
        AnalysisEngine eng = bld.addSensorJobs().addRuleJobs().buildEngine();
        
        ReportsCollection allReports = eng.getAllReports();
        
        StatsBuilder sb = new StatsBuilder(path, formatVersion, allReports).enableSensorsStats().enableRulesStats();
        StatsEngine stats = sb.build();
        
        ReportsCollection allStats = stats.getStats();
        
        OutputFileWriter out = new OutputFileWriter(allReports, path, formatVersion);
        out.writeAllDataToFiles();
        
        // printing
        System.out.println(allReports);
        //System.out.println(allStats);

    }
}
