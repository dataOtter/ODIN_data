package reports;

import java.text.ParseException;

import constants.Constants;
import dao.SensorReader;
import dao.AnswersReader;
import dao.CouponReader;
import dao.RulesReader;
import dao.SensorTblNamesReader;
import dao.SensorsReader;
import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import reports.rules.AnswersCollection;
import reports.rules.RulesCollection;
import reports.rules.whileAt.AnalysisWhileAt;
import reports.sensors.AnalysisSensor;
import sensors.StudySensorsCollection;
import sensors.data.DataCollection;

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
    private DataCollection _sensorData;
    private final StudySensorsCollection _studySensors;
    private IMJ_OC<IJob> _jobs;
    
    public interface IJob {
    	void doWork(AnalysisEngine e);
    }
    
    public AnalysisEngineBuilder(String path, int formatVersion) throws ParseException {
        _path = path;
        _formatVersion = formatVersion;
        IMJ_OC<String> sensorTblNames = new SensorTblNamesReader(_path, _formatVersion).getSensorTblNames();
        for (String tblName: sensorTblNames) {
        	_sensorData = new SensorReader(_path, _formatVersion).getAllSensorData(tblName);
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
    
    public AnalysisEngineBuilder registerSensorAnalyses() {
    	IJob j = new IJob() {
	    	IAnalysis an;
	    	
    		public void doWork(AnalysisEngine e) {
    			for (int cid: _cids) {
    				for (int sensorId: _sids) {
    					//if (sensorId == Constants.SENSORID_GPS) {
    						double sensorInterval = _studySensors.getSensorInterval(sensorId);
    	    	            an = new AnalysisSensor(cid, sensorId, _sensorData, sensorInterval);
    					//}
    					//else {an = null;}
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
    	                IAnalysis an = new AnalysisWhileAt(_answers, _rules, _sensorData, 
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
