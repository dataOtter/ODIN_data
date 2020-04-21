/**
 * 
 */
package dao;

import java.io.FileWriter;
import java.io.IOException;

import constants.ConstTags;
import constants.Constants;
import maps.*;
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
	
	@Override
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
