package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

import orderedcollection.*;
import studysensors.Constants;
import studysensors.rules.RulesCollection;

public class RulesReader {
	private final String _path;
	private final int _formatVersion;
	private RulesCollection _rules;
	
	public RulesReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
		_rules = null;
	}
	
	public RulesCollection getAllRules() throws ParseException, FileNotFoundException {
		if (_rules == null) {
			IMJ_OC<OneRule> rules = new MJ_OC_Factory<OneRule>().create();
			
			File f = new File(_path + "\\" + Constants.RULES_CSV);
	        try (Scanner sc = new Scanner(f)) {
	            sc.nextLine();
	            while (sc.hasNextLine()){
	                String row = sc.nextLine();
	                OneRule r = new OneRule(_formatVersion, row);
	                rules.append(r);
	            }
	        }
	        _rules = new RulesCollection(rules);
		}
        return _rules;
	}
}
