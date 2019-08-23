/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studysensors;

import java.io.FileNotFoundException;
import java.text.ParseException;

import dao.*;
import orderedcollection.*;
import studysensors.gps.*;
import studysensors.rules.*;
import studysensors.sensors.*;
import studysensors.whileAt.AnalysisWhileAt;

/**
 *
 * @author Maisha Jauernig
 */
public class AnalysisEngineBuilder {
	private final int gpsSensorId = 12;
    private final String _path;
    private final int _formatVersion;
    private final IMJ_OC<Integer> _cids;
    private final IMJ_OC<Integer> _rids;
    private final RulesCollection _rules;
    private final AnswersCollection _answers;
    private final GpsDataCollection _gpsSensorData;
    private final StudySensorsCollection _studySensors;
    
    public interface IJob {
    	void doWork(AnalysisEngine e);
    }
 
    IMJ_OC<IJob> _jobs;
    
    public AnalysisEngineBuilder(String path, int formatVersion) throws FileNotFoundException, ParseException {
        _path = path;
        _formatVersion = formatVersion;
        String gpsSensorTblName = new SensorTblNamesReader(_path, _formatVersion).getSensorTblNames().getItem(0);
        _gpsSensorData = new GpsSensorReader(_path, _formatVersion).getAllGpsSensorData(gpsSensorTblName);
        _studySensors = new SensorsReader(_path, _formatVersion).getStudySensorsCollection();
        _cids = new CouponIdReader(_path, _formatVersion).getActiveCouponIds();
        
        // _answers will contain all answers, regardless of cid and rid
        _answers = new AnswersReader(_path, _formatVersion).getAllAnswers();
        _rules = new RulesReader(_path, _formatVersion).getAllRules();
        _rids = _rules.getRulesCollectionByType(Constants.RULE_WHILEAT_NAME).getAllRids();
         //= new RuleIdReader(_path, _formatVersion).getWhileAtRuleIds();
        
        _jobs = new MJ_OC_Factory<IJob>().create();
    }
    
    public AnalysisEngineBuilder registerGpsAnalyses() {
    	IJob j = new IJob() {
    		public void doWork(AnalysisEngine e) {
    			for (int i = 0; i<_cids.length(); i++){
    				double sensorInterval = _studySensors.getSensorInterval(gpsSensorId);
    	            IAnalysis an = new AnalysisGps(_cids.getItem(i), gpsSensorId, _gpsSensorData, sensorInterval);
    	            e.register(an);
    	        }
    		}
    	};
        _jobs.append(j);
        
        return this;
    }
    
    public AnalysisEngineBuilder registerWhileAtAnalyses() {
    	IJob j = new IJob() {
    		public void doWork(AnalysisEngine e) {
    	        for (int i = 0; i<_cids.length(); i++){
    	            int cid = _cids.getItem(i);
    	            for (int j = 0; j<_rids.length(); j++){

    	                // _answers contains all answers, regardless of cid and rid
    	                IAnalysis an = new AnalysisWhileAt(_answers, _rules, _gpsSensorData, 
    	                		_studySensors, cid, _rids.getItem(j));
    	                e.register(an);
    	            }
    	        }
    		}
    	};
        _jobs.append(j);

        return this;
    }
    
    public AnalysisEngine build() {
    	AnalysisEngine eng = new AnalysisEngine();
    	for (int i = 0; i< _jobs.length(); i++) {
    		IJob j = _jobs.getItem(i);
    		j.doWork(eng);
    	}
    	return eng;
    }
}
