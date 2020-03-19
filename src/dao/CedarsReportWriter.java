/**
 * 
 */
package dao;

import java.io.FileWriter;
import java.io.IOException;

import constants.ConstTags;
import constants.Constants;
import maps.IMJ_Map;
import maps.MJ_Map_Factory;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.ReportsCollection;

/**
 * @author Maisha Jauernig
 *
 */
public class CedarsReportWriter {
	private final ReportsCollection _repsCollection;
	//private IMJ_OC<String> _tagsWritten;
	private IMJ_OC<String> _allFinalTags;
    private final String _path;
    private final int _formatVersion;
	
	public CedarsReportWriter(ReportsCollection reps, String path, int formatVersion) {
        _path = path;
        _formatVersion = formatVersion;
		_repsCollection = reps;
		//_tagsWritten = new MJ_OC_Factory<String>().create();
		_allFinalTags = new MJ_OC_Factory<String>().create();
		_allFinalTags.add(ConstTags.REPORTS_COUPONID);
		//_allFinalTags.add(ConstTags.REPORTS_STUDYID);
	}
	
	public void writeAllDataToFiles(String reportName) throws IOException {
		Study s = new StudyReader(_path, _formatVersion).getStudy();
		int studyId = s.getStudyId();

		// must call docs first in order to make list of _allFinalTags
		/*FileWriter wrDocs = new FileWriter(Constants.CEDARS_CODEBOOK_CSV);
		String colsToWrite = Constants.HEALTH_CODEBOOK_COLUMN_LABELS + "\n";
		try { 
			wrDocs.write(colsToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeDocsToFile(wrDocs, studyId);*/
		makeFinalTagsAndGetTagToDescr(studyId);
		
		FileWriter wrData = new FileWriter(reportName);
		String colsToWrite = _allFinalTags.toString();
		colsToWrite = colsToWrite + "\n";
		
		try { 
			wrData.write(colsToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeDataToFile(wrData, studyId);
	}
	
	private void writeDataToFile(FileWriter wr, int studyId) {
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
	
	private IMJ_Map<String, IMJ_OC<String>> makeFinalTagsAndGetTagToDescr(int studyId) {
		IMJ_OC<String> onlyAddTheseTags = new MJ_OC_Factory<String>().create();
		onlyAddTheseTags.add(ConstTags.REPORTS_RULEID); 
		onlyAddTheseTags.add("today"); 
		onlyAddTheseTags.add("answered_all_time"); 
		onlyAddTheseTags.add(ConstTags.REPORTS_TOTAL_RULE_FIRES); 
		onlyAddTheseTags.add(ConstTags.REPORTS_IDEAL_RULE_FIRES); 
		//onlyAddTheseTags.add(ConstTags.REPORTS_SKIPPED_RULE_FIRES); 
		//onlyAddTheseTags.add(ConstTags.REPORTS_EXPIRED_RULE_FIRES); 
		//onlyAddTheseTags.add(ConstTags.REPORTS_POWEREDOFF_RULE_FIRES); 
		onlyAddTheseTags.add(ConstTags.REPORTS_RESPONDED_RULE_FIRES); 
		// get unique tags and descriptions for the given studyid 
		IMJ_Map<String, IMJ_OC<String>> tagToDesc = 
				_repsCollection.getTagToDescAndBuildFinalTags(studyId, _allFinalTags, onlyAddTheseTags);
		return tagToDesc;
	}
	
	/*private void writeDocsToFile(FileWriter wr, int studyId) {
		// must call docs first in order to make list of _allFinalTags
		
		// get unique tags and descriptions for the given studyid 
		IMJ_Map<String, IMJ_OC<String>> tagToDesc = 
				makeFinalTagsAndGetTagToDescr(studyId);
		
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
	}*/
	
	private void addToTagOrderedToData(IMJ_Map<String, String> tagOrderedToData, int cid,
			int studyId, IMJ_Map<String, String> tagToData) {
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
