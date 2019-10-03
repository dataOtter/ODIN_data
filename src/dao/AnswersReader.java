package dao;

import java.text.ParseException;
import java.util.Scanner;

import Assert.Assertion;
import Constants.Constants;
import orderedcollection.*;
import studysensors.rules.AnswersCollection;

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
        
		IMJ_OC<OneAnswer> answers = new MJ_OC_Factory<OneAnswer>().create();
		
        while ( sc.hasNextLine() ){
        	String[] line = getCompleteLineAsArray(sc);
            OneAnswer oneAns = new OneAnswer(_formatVersion, line);
            answers.add(oneAns);
        } 
        sc.close();
        
        return new AnswersCollection(answers);
	}
	
	/**
	 * @param sc
	 * @return the complete next line using the given scanner, as a String[] 
	 */
	private String[] getCompleteLineAsArray(Scanner sc){
        int cols = 0;
        String completeLine = "";

        do {
          String oneline = sc.nextLine();
          // if this hits it came from the while loop, and so this subsequent 
          // partial line needs to be attached to the previous partial line
          if (cols > 0) {
            // throw away everything upto and including the first comma
            oneline = oneline.substring(oneline.indexOf(",") + 1);
          }
          completeLine += oneline;  // merge the two partial lines
          cols += getNumCols(oneline);
        }
        // while the column count does not match (indicating lines incorrectly broken up)
        while (cols < Constants.ANSWERS_NUM_COLS);
        
        Assertion.test(cols == Constants.ANSWERS_NUM_COLS, 
                "Not the right number of columns in this row");
        
        return completeLine.split(",");
    }
	
	private int getNumCols(String oneLine) {
        boolean inQuotes = false;
        int commas = 0;

        for (int i=0; i<oneLine.length(); i++) {
           // if this is the beginning or end of a sub-string that is in quotes
           if (oneLine.charAt(i)=='\"') {  
               inQuotes = true; 
           }
           // if we are not inside a sub-string that is in quotes, count any commas
           if ( ! inQuotes) {
              if (oneLine.charAt(i)==',') { 
                  commas++; 
              }
           }
        }
        // if the last character in this string was not a closing quote, comma count is correct
        if ( ! inQuotes) {
            return commas;
        }
        // if the last character in this string was a closing quote, add a comma count 
        // because lines end with a comma in CSVs (num commas = num columns)
        else {
            return commas+1;
        }
    }
}
