package studysensors;

import java.io.FileNotFoundException;
import java.text.ParseException;

import stats.ReportsCollection;
import stats.StatsEngine;
import stats.StatsBuilder;
/**
 *
 * @author Maisha Jauernig
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, ParseException {

        String path = Constants.DIRECTORY_PATH;
        int formatVersion = Constants.DEFAULT_FORMAT_VERSION;
        		
        AnalysisEngine eng = new AnalysisEngineBuilder(path, formatVersion).
				registerGpsAnalyses().registerWhileAtAnalyses().build();
        
        ReportsCollection allReports = eng.getAllReports();
        
        StatsBuilder sb = new StatsBuilder(path, formatVersion, allReports).enableSensorsStats().enableRulesStats();
        StatsEngine stats = sb.build();
        
        ReportsCollection allStats = stats.getStats();
        
        // printing
        System.out.println(allReports);
        System.out.println(allStats);

    }
}
