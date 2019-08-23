package studysensors;

import stats.OneReport;

/**
 *
 * @author Maisha
 */
public interface IAnalysis {
    OneReport getAnalysisReport();
    String getAnalysisType();
}
