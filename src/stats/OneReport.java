package stats;

import maps.*;
import orderedcollection.*;
import studysensors.Constants;

public abstract class OneReport {
	private final IMJ_Map<String, Double> _data;
	private final IMJ_Map<String, String> _docs;
	private final boolean _isRuleReport;
	private final boolean _isSensorReport;
	
	public boolean isRuleReport() {
		return _isRuleReport;
	}
	
	public boolean isSensorReport() {
		return _isSensorReport;
	}
	
	protected OneReport(boolean isSensorReport, boolean isRuleReport) {
		_data = new MJ_Map_Factory<String, Double>().create();
		_docs = new MJ_Map_Factory<String, String>().create();
		_isSensorReport = isSensorReport;
		_isRuleReport = isRuleReport;
	}
	
	public Double getValue(String tag) {
		if ( ! _data.contains(tag)) {
			return null;
		}
		return _data.getValueOfKey(tag);
	}

	public String getDocs(String tag) {
		if ( ! _docs.contains(tag)) {
			return null;
		}
		return _docs.getValueOfKey(tag);
	}
	
	public int getCid() {
		return _data.getValueOfKey(Constants.REPORTS_COUPONID).intValue();
	}
	
	public void addValue(String tag, Double val) {
		_data.add(tag, val);
		_docs.add(tag, "");
	}
	
	public void addValue(String tag, Double val, String doc) {
		_data.add(tag, val);
		_docs.add(tag, doc);
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i<_data.length(); i++) {
			String tag = _data.getKeyAtIdx(i);
			
			double val = _data.getValueOfKey(tag);
			String valAsString = "";
			if (val < 1.0 && val > 0.0) {
				valAsString = String.format("%.1f", val);
			}
			else {
				valAsString = String.format("%.0f", val);
			}

			s += _docs.getValueOfKey(tag) + " (" + tag + ") : " + valAsString + "\n";
		}
		return s;
	}

	public IMJ_OC<String> getAllTags() {
		IMJ_OC<String> tag_list = new MJ_OC_Factory<String>().create();
		for (int i = 0; i<_data.length(); i++) {
			String tag = _data.getKeyAtIdx(i);
			tag_list.append(tag);
		}
		return tag_list;
	}
}
