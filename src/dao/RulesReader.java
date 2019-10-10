package dao;

import java.util.Scanner;

import constants.Constants;
import dao.rules.OneRule;
import orderedcollection.*;
import reports.rules.RulesCollection;

/**
 *
 * @author Maisha Jauernig
 */
public class RulesReader {
	private final String _path;
	private final int _formatVersion;
	private RulesCollection _rules;
	
	public RulesReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
		_rules = null;
	}
	
	public RulesCollection getAllRules() {
		if (_rules == null) {
	    	int numCols;
	    	if (_formatVersion == 2) {
	    		numCols = Constants.RULES_NUM_COLS_V2;
	    	}
	    	else {
	    		numCols = Constants.RULES_NUM_COLS;
	    	}
			Scanner sc = new ScannerHelper(_path, Constants.RULES_CSV, numCols).getScanner();

			IMJ_OC<OneRule> rules = new MJ_OC_Factory<OneRule>().create();
			
            while (sc.hasNextLine()){
                String row = sc.nextLine();
                OneRule r = new OneRule(_formatVersion, row);
                rules.add(r);
            }
            sc.close();
	        _rules = new RulesCollection(rules);
		}
        return _rules;
	}
}
