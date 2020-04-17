package dao;

import java.io.FileWriter;
import java.io.IOException;

import constants.ConstTags;
import constants.Constants;
import maps.IMJ_Map;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
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
	
	public void writeAllDataToFiles(String reportName, String folderPath, int studyId) throws IOException {
		// must call docs first in order to make list of _allFinalTags
		String colsToWrite = Constants.HEALTH_CODEBOOK_COLUMN_LABELS + "\n";
		FileWriter wrDocs;
		try {
			wrDocs = new FileWriter(folderPath + "/" + Constants.REPORT_CODEBOOK_CSV);
			wrDocs.write(colsToWrite);
			writeDocsToFile(wrDocs, studyId, _onlyAddTheseTags);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		makeFinalTagsAndGetTagToDescr(studyId, _onlyAddTheseTags);
		
		colsToWrite = _allFinalTags.toString();
		colsToWrite = colsToWrite + "\n";

		writeDataToFile(studyId, reportName, colsToWrite, folderPath);
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
}
