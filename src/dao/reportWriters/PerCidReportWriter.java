/**
 * 
 */
package dao.reportWriters;

import java.io.File;
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
public class PerCidReportWriter extends AbsReportWriter {
	
	protected PerCidReportWriter(ReportsCollection reps, String path, int formatVersion) {
		super(reps, path, formatVersion, null);
		_allFinalTags.add(ConstTags.REPORTS_COUPONID);
		_allFinalTags.add(ConstTags.REPORTS_STUDYID);
	}
	
	protected void writeDataToFile(int studyId, String reportName, String colsToWrite, String folderName) {
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
			String cName = tempTagOrdToData.get(ConstTags.REPORTS_COUPONNAME);

			// write the data row for the cid to the file
			String toWrite = "";
			for (int j = 0; j<tempTagOrdToData.size(); j++) {
				String tag = tempTagOrdToData.getKey(j);
				toWrite += tempTagOrdToData.get(tag) + ",";
			}

			String tempPath = folderName + "/" + cName;
			String pathAndName = tempPath + "/" + cName + "_" + reportName;
			try {
				new File(tempPath).mkdir();
				
				FileWriter wr = new FileWriter(pathAndName);
				wr.write(colsToWrite);
				wr.write(toWrite.substring(0, toWrite.length()-1) + "\n");
				wr.close();
				
				/*File tmpDir = new File(tempPath + "/" + Constants.CEDARS_CODEBOOK_CSV);
				if (! tmpDir.exists()) {
					Path source = Paths.get(folderName + "/" + Constants.CEDARS_CODEBOOK_CSV);
					Path newdir = Paths.get(tempPath);
					Files.copy(source, newdir.resolve(source.getFileName()));
				}*/
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
