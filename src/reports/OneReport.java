package reports;

import constants.ConstTags;
import maps.*;
import orderedcollection.*;

/**
 *
 * @author Maisha Jauernig
 */
public abstract class OneReport {
	private final IMJ_Map<String, Double> _data;
	private final IMJ_Map<String, String> _docs;
	private final boolean _isRuleReport;
	private final boolean _isSensorReport;
	private IMJ_OC<String> _relatedDataNames;
	
	protected OneReport(boolean isSensorReport, boolean isRuleReport, IMJ_OC<String> relatedDataNames) {
		_data = new MJ_Map_Factory<String, Double>().create();
		_docs = new MJ_Map_Factory<String, String>().create();
		_isSensorReport = isSensorReport;
		_isRuleReport = isRuleReport;
		_relatedDataNames = relatedDataNames;
	}
	
	public IMJ_OC<String> getRelatedDataNames() {
		return _relatedDataNames;
	}

	public boolean isRuleReport() {
		return _isRuleReport;
	}
	
	public boolean isSensorReport() {
		return _isSensorReport;
	}
	
	public Double getValue(String tag) {
		if ( ! _data.containsKey(tag)) {
			return null;
		}
		return _data.get(tag);
	}

	public String getDocs(String tag) {
		if ( ! _docs.containsKey(tag)) {
			return null;
		}
		return _docs.get(tag);
	}
	
	public int getCid() {
		return _data.get(ConstTags.REPORTS_COUPONID).intValue();
	}
	
	public void addValue(String tag, Double val) {
		_data.put(tag, val);
		_docs.put(tag, "");
	}
	
	public void addValue(String tag, Double val, String doc) {
		_data.put(tag, val);
		_docs.put(tag, doc);
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i<_data.size(); i++) {
			String tag = _data.getKey(i);
			
			if (! tag.contains("__") || tag.contains(ConstTags.REPORTS_COUPONID)) {
				
				Double val = _data.get(tag);
				String valAsString = "";
				if (val == null) {
					valAsString = "null";
				}
				else if (val < 1.0 && val > 0.0) {
					valAsString = String.format("%.1f", val);
				}
				else {
					valAsString = String.format("%.0f", val);
				}

				s += _docs.get(tag) + " (" + tag + ") : " + valAsString + "\n";
			}
		}
			
		return s;
	}

	public IMJ_OC<String> getAllTags() {
		IMJ_OC<String> tagList = new MJ_OC_Factory<String>().create();
		for (int i = 0; i<_data.size(); i++) {
			String tag = _data.getKey(i);
			tagList.add(tag);
		}
		return tagList;
	}
	
	public IMJ_Map<String, String> getFinalTagToData(String typeAndId) {
		return getTagToVal(typeAndId, _data);
	}
	
	public IMJ_Map<String, String> getFinalTagToDocs(String typeAndId) {
		return getTagToVal(typeAndId, _docs);
	}
	
	private IMJ_Map<String, String> getTagToVal(String typeAndId, IMJ_Map<String, ?> dataMap) {
		IMJ_Map<String, String> tagToVal = new MJ_Map_Factory<String, String>().create();
		// for each tag, value pair in the given data map
		for (int i = 0; i<dataMap.size(); i++) {
			String tag = dataMap.getKey(i);
			// ignore couponid data because it will be added only once, at ReportsCollection level
			if (tag != ConstTags.REPORTS_COUPONID) {
				String finalTag = typeAndId + "_" + tag;
				Object val = dataMap.get(tag);
				String finalVal = "";
				// if the dataMap is the _docs map
				if (val instanceof String) {
					finalVal = (String) val;
				}  
				// if the dataMap is the _data map
				else if (val instanceof Double) {
					finalVal = Double.toString((Double) val);
				}
				tagToVal.put(finalTag, finalVal);
			}
		}
		return tagToVal;
	}
}
