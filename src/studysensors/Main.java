package studysensors;

import java.io.IOException;
import java.text.ParseException;

import Constants.Constants;
import dao.OutputFileWriter;
import stats.ReportsCollection;
import stats.StatsEngine;
import stats.StatsBuilder;
/**
 *
 * @author Maisha Jauernig
 */
public class Main {

    public static void main(String[] args) throws ParseException, IOException {

        String path = Constants.DIRECTORY_PATH;
        int formatVersion = Constants.DEFAULT_FORMAT_VERSION;
        		
        AnalysisEngineBuilder bld = new AnalysisEngineBuilder(path, formatVersion);
        AnalysisEngine eng = bld.
				registerGpsAnalyses().registerWhileAtAnalyses().build();
        		//registerGpsAnalyses().build();
        
        ReportsCollection allReports = eng.getAllReports();
        
        StatsBuilder sb = new StatsBuilder(path, formatVersion, allReports).enableSensorsStats().enableRulesStats();
        StatsEngine stats = sb.build();
        
        ReportsCollection allStats = stats.getStats();
        
        OutputFileWriter out = new OutputFileWriter(allReports, path, formatVersion);
        out.writeAllDataToFiles();
        
        // printing
        System.out.println(allReports);
        System.out.println(allStats);

    }
}
