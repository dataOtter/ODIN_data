package dao;

import java.io.FileWriter;
import java.io.IOException;

import maps.*;
import orderedcollection.*;
import stats.ReportsCollection;
import studysensors.Constants;

public class OutputFileWriter {
	private final ReportsCollection _repsCollection;
	private IMJ_OC<String> _allUniqueTags;
	private IMJ_OC<String> _tagsWritten;
    private final String _path;
    private final int _formatVersion;
	
	public OutputFileWriter(ReportsCollection reps, String path, int formatVersion) {
        _path = path;
        _formatVersion = formatVersion;
		_repsCollection = reps;
		_allUniqueTags = new MJ_OC_Factory<String>().create();
		_tagsWritten = new MJ_OC_Factory<String>().create();
		_allUniqueTags.add(Constants.REPORTS_COUPONID);
		_allUniqueTags.add(Constants.REPORTS_STUDYID);
	}
	
	public void writeAllDataToFiles() throws IOException {
		Study s = new StudyReader(_path, _formatVersion).getStudy();
		int studyId = s.getStudyId();
		FileWriter wrDocs = new FileWriter("health_codebook.csv");
		writeDocsToFile(wrDocs, studyId);

		FileWriter wrData = new FileWriter("health_report.csv");
		writeDataToFile(wrData, studyId);
	}
	
	private void writeDocsToFile(FileWriter wr, int studyId) {
		
		
		the column numbers in the docs file are all -1
		
		/*// this should happen above for file writer, just once 
		String toWrite = "column_index,variable_tag,variable_description";*/
		
		// get unique tags and descriptions for the given studyid 
		IMJ_Map<String, String> tagToDesc = _repsCollection.getTagToDesc(studyId, _allUniqueTags);
		// for each tag, desc pair
		for (int i = 0; i<tagToDesc.size(); i++) {
			String tag = tagToDesc.getKey(i);
			// only write each unique tag and description to the docs file once 
			// (if there will be multiple studies in one file)
			if ( ! _tagsWritten.contains(tag) ) {
				// get the column index, tag, and description to write to the file
				String toWrite = Integer.toString(_allUniqueTags.indexOf(tag)) + "," + tag + "," 
				+ tagToDesc.get(tag) + "\n";
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
		/*// this should happen above for file writer, just once 
		write to the file passed in the index numbers of _allUniqueTags as columns */
		
		// make map from: tag in correct order, to: empty string data placeholder
		IMJ_Map<String, String> tagOrderedToData = new MJ_Map_Factory<String, String>().create();
		for (String tag: _allUniqueTags) {
			tagOrderedToData.put(tag, "");
		}
		
		// get unique tags and data for each cid and the given studyid 
		IMJ_Map<Integer, IMJ_Map<String, String>> cidToTagToData = 
				_repsCollection.getCidToTagToData(studyId, _allUniqueTags);
		// for each coupon's tagToData map
		for (int i = 0; i<cidToTagToData.size(); i++) {
			int cid = cidToTagToData.getKey(i);
			addToTagOrderedToData(tagOrderedToData, cid, studyId, cidToTagToData.get(cid));

			// write the data row for the cid to the file
			String toWrite = "";
			for (int j = 0; i<tagOrderedToData.size(); j++) {
				toWrite += tagOrderedToData.get(tagOrderedToData.getKey(j)) + ",";
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
