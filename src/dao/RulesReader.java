package dao;

import java.util.Scanner;

import constants.Constants;
import orderedcollection.*;
import reports.rules.OneRule;
import reports.rules.RulesCollection;

/**
 * Reads all data from the rules.csv file into a RulesCollection
 * @author Maisha Jauernig
 */
public class RulesReader {
	private final String _path;
	private final int _formatVersion;
	private RulesCollection _rules;
	
	/**
	 * @param path
	 * @param formatVersion
	 */
	public RulesReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
		_rules = null;
	}
	
	/**
	 * @return all data from the rules.csv file as a RulesCollection
	 */
	public RulesCollection getAllRules() {
		if (_rules == null) {
	    	int numCols;
	    	switch (_formatVersion) {
	    	case 0:
	    		numCols = Constants.RULES_NUM_COLS_V0;
	    		break;
	    	case 1:
	    		numCols = Constants.RULES_NUM_COLS_V1;
	    		break;
	    	case 2:
	    		numCols = Constants.RULES_NUM_COLS_V2;
	    	case 3:
	    		numCols = Constants.RULES_NUM_COLS_V2;
	    		break;
    		default:
				numCols = Constants.RULES_NUM_COLS_V0;
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
