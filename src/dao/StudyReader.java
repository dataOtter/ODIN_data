package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

import Assert.Assertion;
import studysensors.Constants;

public class StudyReader {
    private final String _path;
    private final int _formatVersion;
    
    public StudyReader(String path, int formatVersion) throws FileNotFoundException, ParseException {
        _path = path;
        _formatVersion = formatVersion;
    }
    
    public Study getStudy(){
        File f = new File(_path + "\\" + Constants.STUDY_CSV);
        Scanner sc = null;
        try{
            sc = new Scanner(f);
        }
        catch(Exception e){
            Assertion.test(false, "Scanner failed to find file");
        }
        Assertion.test(sc.nextLine().split(",").length == Constants.STUDY_NUM_COLS, 
                "No file or file does not have the correct number of columns");
        String line = sc.nextLine();
        
        return new Study(line.split(","));
    }
}
