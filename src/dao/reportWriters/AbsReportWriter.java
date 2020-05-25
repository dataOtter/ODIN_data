package dao.reportWriters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import constants.ConstTags;
import constants.Constants;
import dao.StudyReader;
import maps.IMJ_Map;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.AnalysisEngine;
import reports.AnalysisEngineBuilder;
import reports.ReportsCollection;

public abstract class AbsReportWriter {
	protected final ReportsCollection _repsCollection;
	protected IMJ_OC<String> _tagsWritten;
	protected IMJ_OC<String> _allFinalTags;
	protected final String _path;
	protected final int _formatVersion;
	protected IMJ_OC<String> _onlyAddTheseTags;
	
	public AbsReportWriter(ReportsCollection reps, String path, int formatVersion, IMJ_OC<String> onlyAddTheseTags) {
        _path = path;
        _formatVersion = formatVersion;
		_repsCollection = reps;
		_tagsWritten = new MJ_OC_Factory<String>().create();
		_allFinalTags = new MJ_OC_Factory<String>().create();
		_allFinalTags.add(ConstTags.REPORTS_COUPONNAME);
		_onlyAddTheseTags = onlyAddTheseTags;
	}
	
	protected abstract void writeDataToFile(int studyId, String reportName, String colsToWrite, String folderName) throws IOException;
	
	public void writeTimeWindowReportsToFiles(IMJ_OC<String> consentstatuses, double stopT, double startT, 
			double slidingWindowInHrs, boolean isJupyterReport, boolean isFullReport, boolean isZipReport,
			String folderPath, String reportName) throws ParseException, IOException {
		
		int studyId = new StudyReader(_path, _formatVersion).getStudy().getStudyId();
		
		if (isZipReport) {
			AnalysisEngineBuilder bld = new AnalysisEngineBuilder(_path, _formatVersion, consentstatuses, 
	        		stopT, -1, startT);
	        AnalysisEngine eng = bld.addSensorJobs().addRuleJobs().buildEngine();
	        ReportsCollection allReports = eng.getAllReports();

			new File(folderPath).mkdir();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM", Locale.ENGLISH);
            String startDate = sdf.format(startT * 1000);
            String stopDate = sdf.format(stopT * 1000);
	        String folderPath2 = folderPath + "/" + startDate + "_to_" + stopDate; 
			new File(folderPath2).mkdir();
	        
	        new PerCidReportWriter(allReports, _path, _formatVersion).writeAllDataToFiles(reportName, folderPath2, studyId, true);
	        
	        new JupyterReportWriter(allReports, _path, _formatVersion)
	        .writeAllDataToFiles(folderPath2 + "/" + Constants.JUPYTER_REPORT_CSV, folderPath, studyId, true);
		}
		
		else {
			boolean writeDocs = true;
			if (isJupyterReport) {
				folderPath += "\\tex_study_" + Integer.toString(studyId);
		        new File(folderPath).mkdir();
		        writeDocs = false;
		        
		        AnalysisEngineBuilder bld = new AnalysisEngineBuilder(_path, _formatVersion, consentstatuses, 
		        		stopT, -1, startT);
		        ReportsCollection allReports = bld.addSensorJobs().addRuleJobs().buildEngine().getAllReports();
		        new JupyterReportWriter(allReports, _path, _formatVersion)
		        .writeAllDataToFiles(folderPath + "/" + Constants.JUPYTER_REPORT_CSV, folderPath, studyId, writeDocs);
			}
			else if (isFullReport) {
				// this makes a report for the entire time-frame, not any sliding window
				AnalysisEngineBuilder bld = new AnalysisEngineBuilder(_path, _formatVersion, consentstatuses, 
		        		stopT, -1, startT);
		        ReportsCollection allReports = bld.addSensorJobs().addRuleJobs().buildEngine().getAllReports();
		        new FullReportWriter(allReports, _path, _formatVersion).
				writeAllDataToFiles(reportName, folderPath, studyId, writeDocs);
			}
		}
		
		loopTimeForRep(_path, _formatVersion, consentstatuses, stopT, startT, slidingWindowInHrs, folderPath, studyId, 
				isJupyterReport, isFullReport, isZipReport);
		
		if (isZipReport) {
			pack(folderPath, folderPath + ".zip");
			/*File f = new File(folderPath);
			String[]entries = f.list();
			for(String s: entries){
			    File currentFile = new File(f.getPath(),s);
			    currentFile.delete();
			}
			f.delete();*/
		}
	}

	protected void writeAllDataToFiles(String reportName, String folderPath, int studyId, boolean writeDocs) throws IOException {
		// must call docs first in order to make list of _allFinalTags
		String colsToWrite = Constants.HEALTH_CODEBOOK_COLUMN_LABELS + "\n";
		
		if(writeDocs) {
			FileWriter wrDocs;
			try {
				wrDocs = new FileWriter(folderPath + "/" + Constants.REPORT_CODEBOOK_CSV);
				wrDocs.write(colsToWrite);
				writeDocsToFile(wrDocs, studyId, _onlyAddTheseTags);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		makeFinalTagsAndGetTagToDescr(studyId, _onlyAddTheseTags);
		
		colsToWrite = _allFinalTags.toString();
		colsToWrite = colsToWrite + "\n";

		writeDataToFile(studyId, reportName, colsToWrite, folderPath);
	}
	
	protected void addToTagOrderedToData(IMJ_Map<String, String> tagOrderedToData, int cid, int studyId, 
			IMJ_Map<String, String> tagToData) {
		// get the cid and study ID as the first two entries for the file output
		tagOrderedToData.replace(ConstTags.REPORTS_COUPONID, Integer.toString(cid));
		tagOrderedToData.replace(ConstTags.REPORTS_STUDYID, Integer.toString(studyId));
		// for each data column for this coupon
		for (int j = 0; j<tagToData.size(); j++) {
			String tag = tagToData.getKey(j);
			String data = tagToData.get(tag);
			
			for (int i = 0; i < tagOrderedToData.size(); i++) {
				String repTag = tagOrderedToData.getKey(i);
				if (tag.contains(repTag)) {
					// add the data to the file output
					tagOrderedToData.replace(repTag, data);
					break;
				}
			}
		}
	}
	
	private void pack(String sourceDirPath, String zipFilePath) {
		try {
			Path p = Files.createFile(Paths.get(zipFilePath));
			ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p));
	        Path pp = Paths.get(sourceDirPath);
	        Files.walk(pp)
	          .filter(path -> !Files.isDirectory(path))
	          .forEach(path -> {
	              ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
	              try {
	                  zs.putNextEntry(zipEntry);
	                  Files.copy(path, zs);
	                  zs.closeEntry();
	            } catch (IOException e) {
	                System.err.println(e);
	            }
	          });
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private IMJ_Map<String, IMJ_OC<String>> makeFinalTagsAndGetTagToDescr(int studyId, IMJ_OC<String> onlyAddTheseTags) {
		// get unique tags and descriptions for the given studyid 
		IMJ_Map<String, IMJ_OC<String>> tagToDesc = 
				_repsCollection.getTagToDescAndBuildFinalTags(studyId, _allFinalTags, onlyAddTheseTags);
		return tagToDesc;
	}
	
	private void writeDocsToFile(FileWriter wr, int studyId, IMJ_OC<String> onlyAddTheseTags) {
		// must call docs first in order to make list of _allFinalTags
		
		// get unique tags and descriptions for the given studyid 
		IMJ_Map<String, IMJ_OC<String>> tagToDesc = 
				makeFinalTagsAndGetTagToDescr(studyId, onlyAddTheseTags);
		
		// add the coupon id and study id tag
		for (int i = 0; i<2; i++) {
			tagToDesc.put(_allFinalTags.get(i), null);
		}
		// for each tag, desc pair
		for (int i = 0; i<tagToDesc.size(); i++) {
			String tag = tagToDesc.getKey(i);
			
			// only write each unique tag to the docs file once 
			// (if there will be multiple studies in one file)
			if ( ! _tagsWritten.contains(tag) ) {
				
				// get the column index, tag, description, and relate data names to write to the file
				IMJ_OC<String> descrAndRelDataNames = tagToDesc.get(tag);
				String toWrite = Integer.toString(_allFinalTags.indexOf(tag)) + "," + tag;
				
				if (descrAndRelDataNames != null) {
					String descr = descrAndRelDataNames.get(0).replace(",", " "); // replacing commmas so as not to confuse the csv
					String relDataNames = descrAndRelDataNames.get(1).replace(",", ";");
					toWrite += "," + descr + "," + relDataNames;  
				}
				toWrite += "\n";
				
				try {
					wr.write(toWrite);
					_tagsWritten.add(tag);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loopTimeForRep(String path, int formatVersion, IMJ_OC<String> consentstatuses,
    		double stopT, double startT, double slidingWindowInHrs, String folderPath, int studyId, 
    		boolean isJupyterReport, boolean isFullReport, boolean isZipReport) 
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
        			//t + (newWindow * 60 * 60), newWindow, startT);
        			t + (newWindow * 60 * 60), newWindow, t);
            AnalysisEngine eng = bld.addRuleJobs().addSensorJobs().buildEngine();
            ReportsCollection allReports = eng.getAllReports();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date =  new Date( (long) t * 1000);
            String startDate = sdf.format(date);
            date =  new Date( (long) (t + newWindow*60*60) * 1000);
            String stopDate = sdf.format(date);
            
            if (isJupyterReport) {
            	writeReport(new JupyterReportWriter(allReports, path, formatVersion), folderPath + "\\", 
            			Constants.JUPYTER_REPORT_CSV, startDate, stopDate, studyId, folderPath, false);
            }
            else if (isFullReport) {
            	writeReport(new FullReportWriter(allReports, path, formatVersion), "", 
            			Constants.HEALTH_REPORT_CSV, startDate, stopDate, studyId, folderPath, true);
            }
            else if (isZipReport) {
            	String datePath = folderPath + "\\" + stopDate;
        		new File(datePath).mkdir();
        		
            	writeReport(new PerCidReportWriter(allReports, path, formatVersion), "", 
            			Constants.HEALTH_REPORT_CSV, startDate, stopDate, studyId, datePath, true);
        		
            	writeReport(new JupyterReportWriter(allReports, path, formatVersion), datePath + "/", 
            			Constants.JUPYTER_REPORT_CSV, startDate, stopDate, studyId, folderPath, true);
            }
        }
    }
	
	private void writeReport(AbsReportWriter writer, String prepend, String name, String startDate, String stopDate, 
			int studyId, String folderPath, boolean writeDocs) throws IOException {
		String reportName = prepend + name.substring(0, name.length()
    			- 4) + "_" + startDate + "_to_" + stopDate + ".csv";
    	writer.writeAllDataToFiles(reportName, folderPath, studyId, writeDocs);
	}
}
