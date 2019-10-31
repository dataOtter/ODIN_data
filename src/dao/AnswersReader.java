package dao;

import java.text.ParseException;
import java.util.Scanner;

import Assert.Assertion;
import constants.Constants;
import orderedcollection.*;
import reports.rules.AnswersCollection;

/**
 *
 * @author Maisha Jauernig
 */
public class AnswersReader {
	private final String _path;
	private final int _formatVersion;
	
	/**
	 * Reads all data from the answers.csv file into an AnswersCollection
	 * @param path
	 * @param formatVersion
	 */
	public AnswersReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
	}
	
	/**
	 * @return all data from the answers.csv file as an AnswersCollection
	 * @throws ParseException
	 */
	public AnswersCollection getAllAnswers() throws ParseException {
        Scanner sc = new ScannerHelper(_path, Constants.ANSWERS_CSV, Constants.ANSWERS_NUM_COLS).getScanner();
        AnswersCollection ans = new AnswersCollection();
		
        while ( sc.hasNextLine() ){
        	IMJ_OC<String> line = getCompleteLine(sc);
            OneAnswer oneAns = new OneAnswer(_formatVersion, line);
            ans.addAnswer(oneAns);
        } 
        sc.close();
        
        return ans;
	}
	
	/**
	 * @param sc
	 * @return the complete next line using the given scanner, as a String[] 
	 */
	private IMJ_OC<String> getCompleteLine(Scanner sc){
        int cols = 0;
        IMJ_OC<String> completeLine = new MJ_OC_Factory<String>().create();

        do {
          String oneline = sc.nextLine();
          // if this hits it came from the while loop, and so this subsequent 
          // partial line needs to be attached to the previous partial line
          if (cols > 0) {
            // throw away everything up to and including the first comma
            oneline = oneline.substring(oneline.indexOf(",") + 1);
          }
          completeLine.addAll(getLineOc(oneline));  // merge the two partial lines
          cols = completeLine.size();
        }
        // while the column count does not match (indicating lines incorrectly broken up)
        while (cols < Constants.ANSWERS_NUM_COLS);
        
        Assertion.test(cols == Constants.ANSWERS_NUM_COLS, "Not the right number of columns in this row");
        
        return completeLine;
    }
	
	private IMJ_OC<String> getLineOc(String oneLine) {
		IMJ_OC<String> line = new MJ_OC_Factory<String>().create();
        boolean inQuotes = false;
        int prevCommaIdx = -1;

        for (int i=0; i<oneLine.length(); i++) {
           // if this is the beginning or end of a sub-string that is in quotes
           if (oneLine.charAt(i)=='\"') {  
               inQuotes = ! inQuotes; 
           }
           // if we are not inside a sub-string that is in quotes, add any comma separated elements to the OC
           if ( ! inQuotes) {
              if (oneLine.charAt(i)==',') { 
            	  line.add(oneLine.substring(prevCommaIdx+1, i));
            	  prevCommaIdx = i;
              }
           }
        }
        return line;
    }
}
