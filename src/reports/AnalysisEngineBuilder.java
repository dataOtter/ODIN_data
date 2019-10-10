package reports;

import java.text.ParseException;

import constants.Constants;
import dao.AnswersReader;
import dao.CouponReader;
import dao.GpsSensorReader;
import dao.RulesReader;
import dao.SensorTblNamesReader;
import dao.SensorsReader;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.rules.AnswersCollection;
import reports.rules.RulesCollection;
import reports.rules.whileAt.AnalysisWhileAt;
import reports.sensors.gps.AnalysisGps;
import sensors.StudySensorsCollection;
import sensors.gps.GpsDataCollection;

/**
 *
 * @author Maisha Jauernig
 */
public class AnalysisEngineBuilder {
    private final String _path;
    private final int _formatVersion;
    private final IMJ_OC<Integer> _cids;
    private final IMJ_OC<Integer> _rids;
    private final IMJ_OC<Integer> _sids;
    private final RulesCollection _rules;
    private final AnswersCollection _answers;
    private GpsDataCollection _gpsSensorData;
    private final StudySensorsCollection _studySensors;
    private IMJ_OC<IJob> _jobs;
    
    public interface IJob {
    	void doWork(AnalysisEngine e);
    }
    
    public AnalysisEngineBuilder(String path, int formatVersion) throws ParseException {
        _path = path;
        _formatVersion = formatVersion;
        for (String tblName: new SensorTblNamesReader(_path, _formatVersion).getSensorTblNames()) {
        	if (tblName.equals("sensor_GPS")) {
                _gpsSensorData = new GpsSensorReader(_path, _formatVersion).getAllGpsSensorData(tblName);
        	}
        }
        _studySensors = new SensorsReader(_path, _formatVersion).getStudySensorsCollection();
        _cids = new CouponReader(_path, _formatVersion).getActiveCouponIds();
        
        // _answers will contain all answers, regardless of cid and rid
        _answers = new AnswersReader(_path, _formatVersion).getAllAnswers();
        _rules = new RulesReader(_path, _formatVersion).getAllRules();
        _rids = _rules.getRulesCollectionByType(Constants.RULE_WHILEAT_NOTAT).getAllRids();
         //= new RuleIdReader(_path, _formatVersion).getWhileAtRuleIds();
        
        _sids = _studySensors.getSensorIds();
        
        _jobs = new MJ_OC_Factory<IJob>().create();
    }
    
    public AnalysisEngineBuilder registerGpsAnalyses() {
    	IJob j = new IJob() {
	    	IAnalysis an;
	    	
    		public void doWork(AnalysisEngine e) {
    			for (int cid: _cids) {
    				for (int sensorId: _sids) {
    					if (sensorId == Constants.SENSORID_GPS) {
    						double sensorInterval = _studySensors.getSensorInterval(sensorId);
    	    	            an = new AnalysisGps(cid, sensorId, _gpsSensorData, sensorInterval);
    					}
    					else {
    						an = null;
    					}
        	            e.register(an);
    				}
    	        }
    		}
    	};
        _jobs.add(j);
        
        return this;
    }
    
    public AnalysisEngineBuilder registerWhileAtAnalyses() {
    	IJob j = new IJob() {
    		public void doWork(AnalysisEngine e) {
    			for (int cid: _cids) {
    	            for (int rid: _rids) {
    	            	// currently _rids is already filtered for while at; 
    	            	// eventually change this and check which rid it is to make the correct analysis 
    	            	double gpsSensorInterval = _studySensors.getSensorInterval(Constants.SENSORID_GPS);
    	                // _answers contains all answers, regardless of cid and rid
    	                IAnalysis an = new AnalysisWhileAt(_answers, _rules, _gpsSensorData, 
    	                		gpsSensorInterval, cid, rid);
    	                e.register(an);
    	            }
    	        }
    		}
    	};
        _jobs.add(j);

        return this;
    }
    
    public AnalysisEngine build() {
    	AnalysisEngine eng = new AnalysisEngine();
    	for (IJob j: _jobs) {
    		j.doWork(eng);
    	}
    	return eng;
    }
}
