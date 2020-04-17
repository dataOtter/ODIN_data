/**
 * 
 */
package dao;

import java.io.File;
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
import orderedcollection.MJ_OC_Factory;
import reports.AnalysisEngine;
import reports.AnalysisEngineBuilder;
import reports.ReportsCollection;

/**
 * @author Maisha Jauernig
 *
 */
public class JupyterReportWriter extends AbsReportWriter {
	
	public JupyterReportWriter(ReportsCollection reps, String path, int formatVersion) {
		super(reps, path, formatVersion, null);
		
		_onlyAddTheseTags = new MJ_OC_Factory<String>().create();
		_onlyAddTheseTags.add(ConstTags.REPORTS_RULEID); 
		_onlyAddTheseTags.add(ConstTags.REPORTS_TOTAL_RULE_FIRES); 
		_onlyAddTheseTags.add(ConstTags.REPORTS_IDEAL_RULE_FIRES); 
		//_onlyAddTheseTags.add(ConstTags.REPORTS_SKIPPED_RULE_FIRES); 
		//_onlyAddTheseTags.add(ConstTags.REPORTS_EXPIRED_RULE_FIRES); 
		//_onlyAddTheseTags.add(ConstTags.REPORTS_POWEREDOFF_RULE_FIRES); 
		_onlyAddTheseTags.add(ConstTags.REPORTS_RESPONDED_RULE_FIRES); 
	}
	
	public void writeTexJupyterFiles(IMJ_OC<String> consentstatuses, double stopT, double startT, double slidingWindowInHrs) 
			throws IOException, ParseException {
		Study s = new StudyReader(_path, _formatVersion).getStudy();
		int studyId = s.getStudyId();
		writeAllDataToFiles(Constants.JUPYTER_REPORT_CSV, "", studyId);
        String folderPath3 = "tex_study_" + Integer.toString(studyId);
        new File(folderPath3).mkdir();
        loopTimeForJupyterRep(_path, _formatVersion, consentstatuses, stopT, startT, slidingWindowInHrs, folderPath3, studyId);
	}
	
	private void loopTimeForJupyterRep(String path, int formatVersion, IMJ_OC<String> consentstatuses,
    		double stopT, double startT, double slidingWindowInHrs, String folderPath, int studyId) 
    				throws ParseException, IOException {
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
            AnalysisEngine eng = bld.addRuleJobs().buildEngine();
            
            ReportsCollection allReports = eng.getAllReports();
            
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            Date date =  new Date( (long) t * 1000);
            String startDate = sdf.format(date);
            date =  new Date( (long) (t + newWindow*60*60) * 1000);
            String stopDate = sdf.format(date);
            
            JupyterReportWriter writer = new JupyterReportWriter(allReports, path, formatVersion);
            String name = Constants.JUPYTER_REPORT_CSV.substring(0, Constants.JUPYTER_REPORT_CSV.length() - 4) 
            		+ "_" + startDate + "_to_" + stopDate + ".csv";
            writer.writeAllDataToFiles(folderPath + "/" + name, folderPath, studyId);
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
