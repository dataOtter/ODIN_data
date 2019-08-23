package stats;

import maps.IMJ_Map;
import maps.MJ_Map_Factory;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;

public class ReportsCollection {
    // String = GPSSensor, WhileAtRule, Stats
	private final IMJ_Map<String, IMJ_OC<OneReport>> _allReports;
    
	public ReportsCollection() {
		_allReports = new MJ_Map_Factory<String, IMJ_OC<OneReport>>().create();
	}
	
	public IMJ_OC<OneReport> getAnalysesByType(String type) {
		if ( ! _allReports.contains(type)) {
			IMJ_OC<OneReport> empty = new MJ_OC_Factory<OneReport>().create();
			_allReports.add(type,  empty);
			return empty;
		}
		return _allReports.getValueOfKey(type);
	}
	
	public void addAnalysisByType(String type, OneReport rep) {
		IMJ_OC<OneReport> list = getAnalysesByType(type);
		list.append(rep);
	}
	
	public OneReport getAnalysisByTypeAndCid(String type, int cid) {
		IMJ_OC<OneReport> reports = getAnalysesByType(type);
		OneReport rep;
		
		for (int i = 0; i<reports.length(); i++) {
			rep = reports.getItem(i);
			if (rep.getCid() == cid) {
				return rep;
			}
		}
		return null;
	}

	public ReportsCollection extractAllSensorReportsForOneCid(int cid) {
		ReportsCollection sub = new ReportsCollection();
		for (int i = 0; i<_allReports.length(); i++) {
			String type = _allReports.getKeyAtIdx(i);
			IMJ_OC<OneReport> list = _allReports.getValueOfKey(type);
			for (int j=0; j<list.length(); j++) {
				OneReport rep = list.getItem(j);
				if (rep.isSensorReport() && rep.getCid()==cid) {
					sub.addAnalysisByType(type, rep);
				}
			}
		}
		return sub;
	}
	
	public ReportsCollection extractAllRuleReportsForOneCid(int cid) {
		ReportsCollection sub = new ReportsCollection();
		for (int i = 0; i<_allReports.length(); i++) {
			String type = _allReports.getKeyAtIdx(i);
			IMJ_OC<OneReport> list = _allReports.getValueOfKey(type);
			for (int j=0; j<list.length(); j++) {
				OneReport rep = list.getItem(j);
				if (rep.isRuleReport() && rep.getCid()==cid) {
					sub.addAnalysisByType(type, rep);
				}
			}
		}
		return sub;
	}

	public IMJ_OC<String> getAllTags() {
		IMJ_OC<String> tag_list = new MJ_OC_Factory<String>().create();
		
		for (int i = 0; i<_allReports.length(); i++) {
			String type = _allReports.getKeyAtIdx(i);
			IMJ_OC<OneReport> list = _allReports.getValueOfKey(type);
			for (int j=0; j<list.length(); j++) {
				OneReport rep = list.getItem(j);
				IMJ_OC<String> rep_tags = rep.getAllTags();
				for (int k=0; k<rep_tags.length(); k++) {
					String tag = rep_tags.getItem(k);
					if ( ! tag_list.contains(tag)) {
						tag_list.append(tag);
					}
				}
			}
		}
		return tag_list;
	}
	
	public IMJ_OC<Double> getAllValuesForTag(String tag) {
		IMJ_OC<Double> val_list = new MJ_OC_Factory<Double>().create();
		
		for (int i = 0; i<_allReports.length(); i++) {
			String type = _allReports.getKeyAtIdx(i);
			IMJ_OC<OneReport> list = _allReports.getValueOfKey(type);
			for (int j=0; j<list.length(); j++) {
				OneReport rep = list.getItem(j);
				Double val = rep.getValue(tag);
				if (val != null) {
					val_list.append(val);
				}
			}
		}
		return val_list;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i<_allReports.length(); i++) {
			String type = _allReports.getKeyAtIdx(i);
			s += type + ":\n\n";
			IMJ_OC<OneReport> list = _allReports.getValueOfKey(type);
			for (int j=0; j<list.length(); j++) {
				OneReport rep = list.getItem(j);
				s += rep.toString();
				s += "\n";
			}
		}
		return s;
	}
}
