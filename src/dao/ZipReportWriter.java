package dao;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import constants.Constants;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.AnalysisEngine;
import reports.AnalysisEngineBuilder;
import reports.ReportsCollection;

public class ZipReportWriter {
    private final String _path;
    private final int _formatVersion;
    
    public ZipReportWriter(String path, int formatVersion) {
        _path = path;
        _formatVersion = formatVersion;
    }
	
	public void makeFullReportsZipFolder(double startTInSecs, double stopTimeInSecs, double slidingWindowInHrs) 
			throws ParseException, IOException {
        IMJ_OC<String> consentstatuses = new MJ_OC_Factory<String>().create();
        consentstatuses.add(Constants.COUPON_CONSENTSTATUS_CONSENTAGREED);
        
        AnalysisEngineBuilder bld = new AnalysisEngineBuilder(_path, _formatVersion, consentstatuses, 
        		stopTimeInSecs, slidingWindowInHrs);
        AnalysisEngine eng = bld.addSensorJobs().addRuleJobs().buildEngine();
        ReportsCollection allReports = eng.getAllReports();

        PerCidReportWriter writer = new PerCidReportWriter(allReports, _path, _formatVersion);
		int studyId = new StudyReader(_path, _formatVersion).getStudy().getStudyId();
		String folderPath = "study_" + Integer.toString(studyId);
        new File(folderPath).mkdir();
        
        String folderPath2 = folderPath + "/full";
        new File(folderPath2).mkdir();
        writer.writeAllDataToFiles(Constants.HEALTH_REPORT_CSV, folderPath2, studyId);
        
        JupyterReportWriter writer2 = new JupyterReportWriter(allReports, _path, _formatVersion);
        writer2.writeAllDataToFiles(folderPath2 + "/" + Constants.JUPYTER_REPORT_CSV, folderPath, studyId);
        
        loopTime(_path, _formatVersion, consentstatuses, stopTimeInSecs, startTInSecs, slidingWindowInHrs, folderPath, studyId);
    }
	
	public void loopTime(String path, int formatVersion, IMJ_OC<String> consentstatuses,
    		double stopT, double startT, double slidingWindowInHrs, String folderPath, int studyId) throws ParseException, IOException {
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
            
            String dateName = Constants.HEALTH_REPORT_CSV.substring(0, Constants.HEALTH_REPORT_CSV.length() - 4) 
            		+ "_" + startDate + "_to_" + stopDate + ".csv";
            String datePath = folderPath + "/" + stopDate;
    		new File(datePath).mkdir();
    		
            PerCidReportWriter writer = new PerCidReportWriter(allReports, path, formatVersion);
            writer.writeAllDataToFiles(dateName, datePath, studyId);

            String jupyterName = Constants.JUPYTER_REPORT_CSV.substring(0, Constants.JUPYTER_REPORT_CSV.length() - 4) 
            		+ "_" + startDate + "_to_" + stopDate + ".csv";
            JupyterReportWriter writer2 = new JupyterReportWriter(allReports, path, formatVersion);
            writer2.writeAllDataToFiles(datePath + "/" + jupyterName, datePath, studyId);
        }
    }

}
