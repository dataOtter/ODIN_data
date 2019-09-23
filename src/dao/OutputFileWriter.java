package dao;

import java.io.FileWriter;
import java.io.IOException;

import maps.*;
import orderedcollection.*;
import stats.ReportsCollection;
import studysensors.Constants;

public class OutputFileWriter {
	private final ReportsCollection _repsCollection;
	private IMJ_OC<String> _allFinalTags;
	private IMJ_OC<String> _tagsWritten;
    private final String _path;
    private final int _formatVersion;
	
	public OutputFileWriter(ReportsCollection reps, String path, int formatVersion) {
        _path = path;
        _formatVersion = formatVersion;
		_repsCollection = reps;
		_allFinalTags = new MJ_OC_Factory<String>().create();
		_tagsWritten = new MJ_OC_Factory<String>().create();
		_allFinalTags.add(Constants.REPORTS_COUPONID);
		_allFinalTags.add(Constants.REPORTS_STUDYID);
	}
	
	public void writeAllDataToFiles() throws IOException {
		Study s = new StudyReader(_path, _formatVersion).getStudy();
		int studyId = s.getStudyId();
		
		// must call docs first in order to make list of _allFinalTags
		FileWriter wrDocs = new FileWriter("health_codebook.csv");
		String colsToWrite = "column_index,variable_tag,variable_description\n";
		try { 
			wrDocs.write(colsToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeDocsToFile(wrDocs, studyId);

		FileWriter wrData = new FileWriter("health_report.csv");
		//write to the file the index numbers of _allUniqueTags as columns
		colsToWrite = "";
		for (int i = 0; i<_allFinalTags.size(); i++) {
			colsToWrite += i + ",";
		}
		colsToWrite = colsToWrite.substring(0, colsToWrite.length()-1) + "\n";
		try { 
			wrData.write(colsToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeDataToFile(wrData, studyId);
	}
	
	private void writeDocsToFile(FileWriter wr, int studyId) {
		// must call docs first in order to make list of _allFinalTags
		
		// get unique tags and descriptions for the given studyid 
		IMJ_Map<String, String> tagToDesc = _repsCollection.getTagToDescAndBuildFinalTags(studyId, _allFinalTags);
		
		// add the coupon id and study id tag
		for (int i = 0; i<2; i++) {
			tagToDesc.put(_allFinalTags.get(i), "");
		}
		// for each tag, desc pair
		for (int i = 0; i<tagToDesc.size(); i++) {
			String tag = tagToDesc.getKey(i);
			// only write each unique tag and description to the docs file once 
			// (if there will be multiple studies in one file)
			if ( ! _tagsWritten.contains(tag) ) {
				// get the column index, tag, and description to write to the file
				String toWrite = Integer.toString(_allFinalTags.indexOf(tag)) + "," + tag + "," 
				+ tagToDesc.get(tag).replace(",", " ") + "\n";  // replacing commmas as this is not a json
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
	
	private void writeDataToFile(FileWriter wr, int studyId) {
		// make map from: tag in correct order, to: empty string data placeholder
		IMJ_Map<String, String> tagOrderedToData = new MJ_Map_Factory<String, String>().create();
		for (String tag: _allFinalTags) {
			tagOrderedToData.put(tag, "NA");
		}
		
		// get unique tags and data for each cid and the given studyid 
		IMJ_Map<Integer, IMJ_Map<String, String>> cidToTagToData = 
				_repsCollection.getCidToTagToData(studyId, _allFinalTags);
		// for each coupon's tagToData map
		for (int i = 0; i<cidToTagToData.size(); i++) {
			int cid = cidToTagToData.getKey(i);
			addToTagOrderedToData(tagOrderedToData, cid, studyId, cidToTagToData.get(cid));

			// write the data row for the cid to the file
			String toWrite = "";
			for (int j = 0; i<tagOrderedToData.size(); j++) {
				String tag = tagOrderedToData.getKey(j);
				if (tag == null) {
					System.out.println(tag + " is null");
				}
				toWrite += tagOrderedToData.get(tag) + ",";
				System.out.println(toWrite);
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
	
	private void addToTagOrderedToData(IMJ_Map<String, String> tagOrderedToData, int cid,
			int studyId, IMJ_Map<String, String> tagToData) {
		// get the cid and study ID as the first two entries for the file output
		tagOrderedToData.replace(Constants.REPORTS_COUPONID, Integer.toString(cid));
		tagOrderedToData.replace(Constants.REPORTS_STUDYID, Integer.toString(studyId));
		// for each data column for this coupon
		for (int j = 0; j<tagToData.size(); j++) {
			String tag = tagToData.getKey(j);
			String data = tagToData.get(tag);
			// add the data to the file output
			tagOrderedToData.replace(tag, data);
		}
	}
}
