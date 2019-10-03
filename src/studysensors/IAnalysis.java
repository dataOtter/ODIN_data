package studysensors;

import stats.OneReport;

/**
 *
 * @author Maisha Jauernig
 */
public interface IAnalysis {
    OneReport getAnalysisReport();
    String getAnalysisType();
}
