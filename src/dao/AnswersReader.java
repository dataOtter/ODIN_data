package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

import Assert.Assertion;
import orderedcollection.*;
import studysensors.Constants;
import studysensors.rules.AnswersCollection;

public class AnswersReader {
	private final String _path;
	private final int _formatVersion;
	
	public AnswersReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
	}
	
	public AnswersCollection getAllAnswers() throws FileNotFoundException, ParseException {
		IMJ_OC<OneAnswer> answers = new MJ_OC_Factory<OneAnswer>().create();
        
        String[] line;
        File f = new File(_path + "\\" + Constants.ANSWERS_CSV);
        Scanner sc = new Scanner(f);
        sc.nextLine();
        
        while ( sc.hasNextLine() ){
            line = getCompleteLineAsArray(sc);
            OneAnswer oneAns = new OneAnswer(_formatVersion, line);
            answers.append(oneAns);
        } 
        sc.close();
        
        return new AnswersCollection(answers);
	}
	
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
