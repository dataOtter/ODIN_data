package reports;

import maps.IMJ_Map;
import maps.MJ_Map_Factory;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import Assert.Assertion;
import constants.ConstTags;

/**
 *
 * @author Maisha Jauernig
 */
public class ReportsCollection {
    // String = GPSSensor, WhileAtRule, Stats
	private final IMJ_Map<String, IMJ_OC<OneReport>> _typeToReports;
    
	public ReportsCollection() {
		_typeToReports = new MJ_Map_Factory<String, IMJ_OC<OneReport>>().create();
	}
	
	public IMJ_OC<OneReport> getAnalysesByType(String type) {
		if ( ! _typeToReports.containsKey(type)) {
			IMJ_OC<OneReport> empty = new MJ_OC_Factory<OneReport>().create();
			_typeToReports.put(type,  empty);
			return empty;
		}
		return _typeToReports.get(type);
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
		for (int i = 0; i<_typeToReports.size(); i++) {
			String type = _typeToReports.getKey(i);
			IMJ_OC<OneReport> list = _typeToReports.get(type);
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
		for (int i = 0; i<_typeToReports.size(); i++) {
			String type = _typeToReports.getKey(i);
			IMJ_OC<OneReport> list = _typeToReports.get(type);
			for (OneReport rep: list) {
				if (rep.isRuleReport() && rep.getCid()==cid) {
					sub.addAnalysisByType(type, rep);
				}
			}
		}
		return sub;
	}

	public IMJ_OC<String> getAllUniqueTags() {
		IMJ_OC<String> tagList = new MJ_OC_Factory<String>().create();
		
		for (int i = 0; i<_typeToReports.size(); i++) {
			IMJ_OC<OneReport> list = _typeToReports.get( _typeToReports.getKey(i) );
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
		
		for (int i = 0; i<_typeToReports.size(); i++) {
			String type = _typeToReports.getKey(i);
			IMJ_OC<OneReport> list = _typeToReports.get(type);
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
		for (int i = 0; i<_typeToReports.size(); i++) {
			String type = _typeToReports.getKey(i);
			s += type + ":\n\n";
			IMJ_OC<OneReport> list = _typeToReports.get(type);
			for (OneReport rep: list) {
				s += rep.toString();
				s += "\n";
			}
		}
		return s;
	}
	
	public IMJ_Map<String, IMJ_OC<String>> getTagToDescAndBuildFinalTags(int studyId, IMJ_OC<String> allFinalTags) {
		IMJ_OC<String> onlyAddTheseTags = null;
		return (IMJ_Map<String, IMJ_OC<String>>) getMapOfValues(studyId, false, allFinalTags, onlyAddTheseTags); 
	}
	
	public IMJ_Map<String, IMJ_OC<String>> getTagToDescAndBuildFinalTags(int studyId, IMJ_OC<String> allFinalTags, 
			IMJ_OC<String> onlyAddTheseTags) {
		// overloaded
		return (IMJ_Map<String, IMJ_OC<String>>) getMapOfValues(studyId, false, allFinalTags, onlyAddTheseTags); 
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
			IMJ_OC<String> allFinalTags) {
		IMJ_OC<String> onlyAddTheseTags = null;
		return (IMJ_Map<Integer, IMJ_Map<String, String>>) getMapOfValues(studyId, true, allFinalTags, onlyAddTheseTags); 
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
	
	private IMJ_Map<?, ?> getMapOfValues(int studyId, boolean isDataNotDesc, IMJ_OC<String> allFinalTags, 
			IMJ_OC<String> onlyAddTheseTags) {
		IMJ_Map<?, ?> mapOfValues = null;
		if (isDataNotDesc) {
			mapOfValues = new MJ_Map_Factory<Integer, IMJ_Map<String, String>>().create(); 
		}
		else {
			mapOfValues = new MJ_Map_Factory<String, String>().create(); 
		}
		
		// for each list of report types
		for (int i = 0; i<_typeToReports.size(); i++) {
			String type = _typeToReports.getKey(i);
			IMJ_OC<OneReport> reps = _typeToReports.get(type);  // reports of one type
			// for each report
			for (OneReport rep: reps) {
				if (isDataNotDesc) {
					addToCidToTagToData(rep, type, studyId, 
							(IMJ_Map<Integer, IMJ_Map<String, String>>) mapOfValues);
				}
				else {
					addToTagToDescAndToFinalTags(rep, type, studyId, 
							(IMJ_Map<String, IMJ_OC<String>>) mapOfValues, allFinalTags, onlyAddTheseTags);
					IMJ_OC<String> relatedDataNames = rep.getRelatedDataNames();
				}
			} 
		} 
		return mapOfValues;
	}
	
	private void addToTagToDescAndToFinalTags(OneReport rep, String type, int studyId, 
			IMJ_Map<String, IMJ_OC<String>> tagToDesc, IMJ_OC<String> allFinalTags, IMJ_OC<String> onlyAddTheseTags) {
		// get the sensor or rule id and type, to prepend to column names
		String typeAndId = ConstTags.getTypeAndId(type, rep);
		// get the vals to add to tagToDesc to write to the file
		IMJ_Map<String, String> oneRepTagToDesc = rep.getFinalTagToDocs(typeAndId);
		String relatedDataNames = rep.getRelatedDataNames().toString();
		
		// add new unique vals to tagToDesc
		for (int k = 0; k<oneRepTagToDesc.size(); k++) {
			String finalTag = oneRepTagToDesc.getKey(k);
			String descr = oneRepTagToDesc.get(finalTag);
			IMJ_OC<String> descrAndRelData = new MJ_OC_Factory<String>().create();
			descrAndRelData.add(descr);
			descrAndRelData.add(relatedDataNames);
			
			if (onlyAddTheseTags == null) {
				// update list of unique tags across all reports
				if ( ! allFinalTags.contains(finalTag) ) {
					allFinalTags.add(finalTag);
				}
			}
			else {
				for (int i = 0; i < onlyAddTheseTags.size(); i++) {
					String repTag = onlyAddTheseTags.get(i);
					if (finalTag.contains(repTag)) {
						// update list of unique tags across all reports
						if ( ! allFinalTags.contains(finalTag) ) {
							allFinalTags.add(finalTag);
						}
						break;
					}
				}
			}
						
			if ( ! tagToDesc.containsKey(finalTag)) {
				tagToDesc.put(finalTag, descrAndRelData);
			}
			else {
				String existingDesc = tagToDesc.get(finalTag).get(0);
				Assertion.test(existingDesc.equals(descr), "Conflicting descriptions of tag " + finalTag);
			}
		}
	}
	
	private void addToCidToTagToData(OneReport rep, String type, int studyId,
			IMJ_Map<Integer, IMJ_Map<String, String>> cidToTagToData) {
		int cid = rep.getCid();
		// get the sensor or rule id and type, to prepend to column names
		String typeAndId = ConstTags.getTypeAndId(type, rep);
		// get the vals to add to the final cidToTagToData to write to the file
		IMJ_Map<String, String> tagToData = rep.getFinalTagToData(typeAndId);
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
			tagToData.put(ConstTags.REPORTS_STUDYID, Integer.toString(studyId));
			cidToTagToData.put(cid, tagToData);
		}
	}
}
