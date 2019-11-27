package reports;

import java.text.ParseException;

import constants.Constants;
import dao.*;
import orderedcollection.*;
import reports.rules.AnswersCollection;
import reports.rules.RulesCollection;
import reports.rules.Cron.AnalysisCron;
import reports.rules.OnArrival.AnalysisOnArrival;
import reports.rules.OnDepBt.AnalysisOnDepBt;
import reports.rules.whileAt.AnalysisWhileAt;
import reports.sensors.AnalysisSensor;
import sensors.StudySensorsCollection;
import sensors.data.SensorDataCollection;

/**
 *
 * @author Maisha Jauernig
 */
public class AnalysisEngineBuilder {
    private final String _path;
    private final int _formatVersion;
    private final CouponCollection _coupons;
    private final IMJ_OC<Integer> _cids;
    private final IMJ_OC<Integer> _rids;
    private final IMJ_OC<Integer> _sids;
    private final RulesCollection _rules;
    private final AnswersCollection _answers;
    private SensorDataCollection _sensorData;
    private final StudySensorsCollection _studySensors;
    private IMJ_OC<IJob> _jobs;
    
    public AnalysisEngineBuilder(String path, int formatVersion) throws ParseException {
        _path = path;
        _formatVersion = formatVersion;
        _sensorData = new SensorDataCollection();
        IMJ_OC<String> sensorTblNames = new SensorTblNamesReader(_path, _formatVersion).getSensorTblNames();
        for (String tblName: sensorTblNames) {
        	new SensorsReader(_path, _formatVersion).addAllSensorDataToDataColl(tblName, _sensorData);
        }
        _studySensors = new SensorsReader(_path, _formatVersion).getStudySensorsCollection();
        _coupons = new CouponReader(_path, _formatVersion).getActiveCoupons();
        _cids = _coupons.getAllCids();
        
        // _answers will contain all answers, regardless of cid and rid
        _answers = new AnswersReader(_path, _formatVersion).getAllAnswers();
        _rules = new RulesReader(_path, _formatVersion).getAllRules();
        _rids = _rules.getAllRids();
        
        _sids = _studySensors.getSensorIds();
        
        //get filters here
        //_filters = null;
        
        _jobs = new MJ_OC_Factory<IJob>().create();
    }
    
    public AnalysisEngineBuilder addSensorJobs() {
    	IJob j = new IJob() {
	    	IAnalysis an;
	    	
    		public void registerAnalysesToEngine(AnalysisEngine e) {
    			for (int cid: _cids) {
    				for (int sensorId: _sids) {
						double sensorInterval = _studySensors.getSensorInterval(sensorId);
	    	            an = new AnalysisSensor(cid, sensorId, _sensorData, sensorInterval);
        	            e.register(an);
    				}
    	        }
    		}
    	};
        _jobs.add(j);
        
        return this;
    }
    
    public AnalysisEngineBuilder addRuleJobs() {
    	IJob j = new IJob() {
    		public void registerAnalysesToEngine(AnalysisEngine e) {
    			for (int cid: _cids) {
    	            for (int rid: _rids) {
    	            	double gpsSensorInterval = _studySensors.getSensorInterval(Constants.SENSORID_GPS);
    	            	String type = _rules.getRuleById(rid).getRuleType();
    	            	IAnalysis an;
    	            	if (type.contains(Constants.RULE_WHILEAT_NOTAT)) {
	            			an = new AnalysisWhileAt(_answers, _rules, _sensorData, gpsSensorInterval, cid, rid, _coupons);
	            			e.register(an);
    	            	}
    	            	else if (type.contains(Constants.RULE_ONARRIVAL) && ! type.contains(Constants.RULE_BLUETOOTH)) {
	            			an = new AnalysisOnArrival(_answers, _rules, _sensorData, gpsSensorInterval, cid, rid, _coupons);
	            			e.register(an);
    	            	}
    	            	//else if (type.contains(Constants.RULE_CRON)) {
	            			//an = new AnalysisCron(_answers, _rules, _sensorData, gpsSensorInterval, cid, rid, _coupons);
	            			//e.register(an);
    	            	//}
    	            	//else if (type.contains(Constants.RULE_ONDEPARTURE) && type.contains(Constants.RULE_BLUETOOTH)) {
	            			//an = new AnalysisOnDepBt(_answers, _rules, _sensorData, gpsSensorInterval, cid, rid, _coupons);
	            			//e.register(an);
    	            	//}
    	            }
    	        }
    		}
    	};
        _jobs.add(j);

        return this;
    }
    
    public AnalysisEngine buildEngine() {
    	AnalysisEngine eng = new AnalysisEngine();
    	for (IJob j: _jobs) {
    		j.registerAnalysesToEngine(eng);
    	}
    	return eng;
    }
    
    private interface IJob {
    	void registerAnalysesToEngine(AnalysisEngine e);
    }
}
