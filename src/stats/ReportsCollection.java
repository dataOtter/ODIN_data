package stats;

import java.io.FileWriter;
import java.io.IOException;

import maps.IMJ_Map;
import maps.MJ_Map_Factory;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import studysensors.Constants;
import Assert.Assertion;

public class ReportsCollection {
    // String = GPSSensor, WhileAtRule, Stats
	private final IMJ_Map<String, IMJ_OC<OneReport>> _allReports;
    
	public ReportsCollection() {
		_allReports = new MJ_Map_Factory<String, IMJ_OC<OneReport>>().create();
	}
	
	public IMJ_OC<OneReport> getAnalysesByType(String type) {
		if ( ! _allReports.containsKey(type)) {
			IMJ_OC<OneReport> empty = new MJ_OC_Factory<OneReport>().create();
			_allReports.put(type,  empty);
			return empty;
		}
		return _allReports.get(type);
	}
	
	public void addAnalysisByType(String type, OneReport rep) {
		IMJ_OC<OneReport> list = getAnalysesByType(type);
		list.add(rep);
	}
	
	public OneReport getAnalysisByTypeAndCid(String type, int cid) {
		IMJ_OC<OneReport> reports = getAnalysesByType(type);
		for (OneReport rep: reports) {
			if (rep.getCid() == cid) {
				return rep;
			}
		}
		return null;
	}

	public ReportsCollection extractAllSensorReportsForOneCid(int cid) {
		ReportsCollection sub = new ReportsCollection();
		for (int i = 0; i<_allReports.size(); i++) {
			String type = _allReports.getKey(i);
			IMJ_OC<OneReport> list = _allReports.get(type);
			for (OneReport rep: list) {
				if (rep.isSensorReport() && rep.getCid()==cid) {
					sub.addAnalysisByType(type, rep);
				}
			}
		}
		return sub;
	}
	
	public ReportsCollection extractAllRuleReportsForOneCid(int cid) {
		ReportsCollection sub = new ReportsCollection();
		for (int i = 0; i<_allReports.size(); i++) {
			String type = _allReports.getKey(i);
			IMJ_OC<OneReport> list = _allReports.get(type);
			for (OneReport rep: list) {
				if (rep.isRuleReport() && rep.getCid()==cid) {
					sub.addAnalysisByType(type, rep);
				}
			}
		}
		return sub;
	}

	public IMJ_OC<String> getAllTags() {
		IMJ_OC<String> tagList = new MJ_OC_Factory<String>().create();
		
		for (int i = 0; i<_allReports.size(); i++) {
			IMJ_OC<OneReport> list = _allReports.get( _allReports.getKey(i) );
			for (OneReport rep: list) {
				IMJ_OC<String> repTags = rep.getAllTags();
				for (String tag: repTags) {
					if ( ! tagList.contains(tag)) {
						tagList.add(tag);
					}
				}
			}
		}
		return tagList;
	}
	
	public IMJ_OC<Double> getAllValuesForTag(String tag) {
		IMJ_OC<Double> valList = new MJ_OC_Factory<Double>().create();
		
		for (int i = 0; i<_allReports.size(); i++) {
			String type = _allReports.getKey(i);
			IMJ_OC<OneReport> list = _allReports.get(type);
			for (OneReport rep: list) {
				Double val = rep.getValue(tag);
				if (val != null) {
					valList.add(val);
				}
			}
		}
		return valList;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i<_allReports.size(); i++) {
			String type = _allReports.getKey(i);
			s += type + ":\n\n";
			IMJ_OC<OneReport> list = _allReports.get(type);
			for (OneReport rep: list) {
				s += rep.toString();
				s += "\n";
			}
		}
		return s;
	}
	
	public IMJ_Map<String, String> getTagToDesc(int studyId, IMJ_OC<String> allUniqueTags) {
		return (IMJ_Map<String, String>) getMapOfValues(studyId, false, allUniqueTags); 
	}
		/*IMJ_Map<String, String> tagToDesc = new MJ_Map_Factory<String, String>().create(); 
		// for each list of report types
		for (int i = 0; i<_allReports.size(); i++) {
			String type = _allReports.getKey(i);
			IMJ_OC<OneReport> reps = _allReports.get(type);  // reports of one type
			// for each report
			for (OneReport rep: reps) {
				addToTagToDesc(rep, type, studyId, tagToDesc);
			}
		}
		return tagToDesc;
	}*/
	
	public IMJ_Map<Integer, IMJ_Map<String, String>> getCidToTagToData(int studyId, 
			IMJ_OC<String> allUniqueTags) {
		return (IMJ_Map<Integer, IMJ_Map<String, String>>) getMapOfValues(studyId, true, allUniqueTags); 
	}
		/*// for each list of report types
		for (int i = 0; i<_allReports.size(); i++) {
			String type = _allReports.getKey(i);
			IMJ_OC<OneReport> reps = _allReports.get(type);  // reports of one type
			// for each report
			for (OneReport rep: reps) {
				addToCidToTagToData(rep, type, studyId, cidToTagToData);
			} 
		} 
		return cidToTagToData;
	}*/
	
	private IMJ_Map<?, ?> getMapOfValues(int studyId, boolean isDataNotDesc, IMJ_OC<String> allUniqueTags) {
		IMJ_Map<?, ?> mapOfValues = null;
		if (isDataNotDesc) {
			mapOfValues = new MJ_Map_Factory<Integer, IMJ_Map<String, String>>().create(); 
		}
		else {
			mapOfValues = new MJ_Map_Factory<String, String>().create(); 
		}
		
		// for each list of report types
		for (int i = 0; i<_allReports.size(); i++) {
			String type = _allReports.getKey(i);
			IMJ_OC<OneReport> reps = _allReports.get(type);  // reports of one type
			// for each report
			for (OneReport rep: reps) {
				if (isDataNotDesc) {
					addToCidToTagToData(rep, type, studyId, 
							(IMJ_Map<Integer, IMJ_Map<String, String>>) mapOfValues, allUniqueTags);
				}
				else {
					addToTagToDesc(rep, type, studyId, 
							(IMJ_Map<String, String>) mapOfValues, allUniqueTags);
				}
			} 
		} 
		return mapOfValues;
	}
	
	private void addToTagToDesc(OneReport rep, String type, int studyId, 
			IMJ_Map<String, String> tagToDesc, IMJ_OC<String> allUniqueTags) {
		// get the sensor or rule id and type, to prepend to column names
		String typeAndId = getTypeAndId(type, rep);
		// get the vals to add to tagToDesc to write to the file
		IMJ_Map<String, String> toWrite = rep.getTagToDocsToWrite(typeAndId, allUniqueTags);
		// add new unique vals to tagToDesc
		for (int k = 0; k<toWrite.size(); k++) {
			String tag = toWrite.getKey(k);
			String descr = toWrite.get(tag);
			if ( ! tagToDesc.containsKey(tag)) {
				tagToDesc.put(tag, descr);
			}
			else {
				String existingDesc = tagToDesc.get(tag);
				Assertion.test(existingDesc == descr, "Conflicting descriptions of tag " + tag);
			}
		}
	}
	
	private void addToCidToTagToData(OneReport rep, String type, int studyId,
			IMJ_Map<Integer, IMJ_Map<String, String>> cidToTagToData, IMJ_OC<String> allUniqueTags) {
		int cid = rep.getCid();
		// get the sensor or rule id and type, to prepend to column names
		String typeAndId = getTypeAndId(type, rep);
		// get the vals to add to the final cidToTagToData to write to the file
		IMJ_Map<String, String> tagToData = rep.getTagToDataToWrite(typeAndId, allUniqueTags);
		// if the final cidToTagToData to write to the file already has an entry for this cid 
		// (and later study id), add the new columns to that entry
		if (cidToTagToData.containsKey(cid)) {
			IMJ_Map<String, String> colToData = cidToTagToData.get(cid);
			for (int k = 0; k<tagToData.size(); k++) {
				String col = tagToData.getKey(k);
				colToData.put(col, tagToData.get(col));
			}
		}
		// otherwise, make an entry for that cid and add the data
		else {
			tagToData.put(Constants.REPORTS_STUDYID, Integer.toString(studyId));
			cidToTagToData.put(cid, tagToData);
		}
	}
	
	private String getTypeAndId(String type, OneReport rep) {
		String typeAndId = "";
		if (rep.isSensorReport()) {
			typeAndId = type + "_id" + rep.getValue(Constants.REPORTS_SENSORID);
		}
		else if (rep.isRuleReport()) {
			typeAndId = type + "_id" + rep.getValue(Constants.REPORTS_RULEID);
		}
		return typeAndId;
	}
}
