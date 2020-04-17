/**
 * 
 */
package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import constants.ConstTags;
import constants.Constants;
import maps.IMJ_Map;
import maps.MJ_Map_Factory;
import orderedcollection.IMJ_OC;
import reports.AnalysisEngine;
import reports.AnalysisEngineBuilder;
import reports.ReportsCollection;

/**
 * @author Maisha Jauernig
 *
 */
public class FullReportWriter extends AbsReportWriter {
	
	public FullReportWriter(ReportsCollection reps, String path, int formatVersion) {
		super(reps, path, formatVersion, null);
		_allFinalTags.add(ConstTags.REPORTS_COUPONID);
		_allFinalTags.add(ConstTags.REPORTS_STUDYID);
	}
	
	public void writeOutFullReports(IMJ_OC<String> consentstatuses, double stopT, double startT, double slidingWindowInHrs) throws ParseException, IOException {
		int studyId = new StudyReader(_path, _formatVersion).getStudy().getStudyId();
		String folderPath = "";
		writeAllDataToFiles(Constants.HEALTH_REPORT_CSV, folderPath, studyId);
        loopTimeForFullRep(_path, _formatVersion, consentstatuses, stopT, startT, slidingWindowInHrs, studyId, folderPath);
	}
	
	private void loopTimeForFullRep(String path, int formatVersion, IMJ_OC<String> consentstatuses,
    		double stopT, double startT, double slidingWindowInHrs, int studyId, String folderPath) throws ParseException, IOException {
    	double newWindow = 0;
    	double checkT = startT;
    	double stopIncreaseingWindowT = startT + (slidingWindowInHrs * 60 * 60);
    	
        for (double t = startT; t <= (stopT - (slidingWindowInHrs * 60 * 60)); 
        		t = (checkT < stopIncreaseingWindowT) ? startT : t + (24 * 60 * 60)) {
        	if (checkT < stopIncreaseingWindowT) {
        		newWindow += 24;
        		checkT += (24 * 60 * 60);
        	}
        	AnalysisEngineBuilder bld = new AnalysisEngineBuilder(path, formatVersion, consentstatuses, 
        			t + (newWindow * 60 * 60), newWindow);
            AnalysisEngine eng = bld.addSensorJobs().addRuleJobs().buildEngine();
            ReportsCollection allReports = eng.getAllReports();
            
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            Date date =  new Date( (long) t * 1000);
            String startDate = sdf.format(date);
            date =  new Date( (long) (t + newWindow*60*60) * 1000);
            String stopDate = sdf.format(date);
            
            FullReportWriter writer = new FullReportWriter(allReports, path, formatVersion);
            String name = Constants.HEALTH_REPORT_CSV.substring(0, Constants.HEALTH_REPORT_CSV.length() - 4) 
            		+ "_" + startDate + "_to_" + stopDate + ".csv";
            writer.writeAllDataToFiles(name, folderPath, studyId);
        }
    }

	protected void writeDataToFile(int studyId, String reportName, String colsToWrite, String folderName) throws IOException {
		FileWriter wr = new FileWriter(reportName);
		try { 
			wr.write(colsToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		IMJ_Map<String, String> tagOrderedToData = new MJ_Map_Factory<String, String>().create();
		for (String tag: _allFinalTags) {
			tagOrderedToData.put(tag, Constants.HEALTH_REPORT_NO_VALUE);
		}
		
		// get unique tags and data for each cid and the given studyid 
		IMJ_Map<Integer, IMJ_Map<String, String>> cidToTagToData = 
				_repsCollection.getCidToTagToData(studyId, _allFinalTags);
		
		// for each coupon's tagToData map
		for (int i = 0; i<cidToTagToData.size(); i++) {
			int cid = cidToTagToData.getKey(i);
			IMJ_Map<String, String> tempTagOrdToData = tagOrderedToData.getDeepCopy();
			addToTagOrderedToData(tempTagOrdToData, cid, studyId, cidToTagToData.get(cid));

			// write the data row for the cid to the file
			String toWrite = "";
			for (int j = 0; j<tempTagOrdToData.size(); j++) {
				String tag = tempTagOrdToData.getKey(j);
				toWrite += tempTagOrdToData.get(tag) + ",";
			}
			try {
				wr.write(toWrite.substring(0, toWrite.length()-1) + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
