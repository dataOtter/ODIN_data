package dao;

import java.io.File;
import java.util.Scanner;

import Assert.Assertion;

/**
 * Makes a Scanner object for the given input data
 * @author Maisha Jauernig
 */
public class ScannerHelper {
	private final String _path;
	private Scanner _sc;
	
	/**
	 * @param path - path without file name, as a String
	 * @param fileName - name of the file, as a String
	 * @param numCols - number of columns contained in the file, as an int
	 */
	public ScannerHelper(String path, String fileName, int numCols) {
		_path = path;
		File f = new File(_path + "\\" + fileName);
        _sc = null;
        try{
            _sc = new Scanner(f);
        }
        catch(Exception e){
            Assertion.test(false, "Scanner failed to find file");
        }
        Assertion.test(_sc.nextLine().split(",").length == numCols, 
                "No file or file does not have the correct number of columns");
	}
	
	/**
	 * @return a Scanner object
	 */
	public Scanner getScanner() {
		return _sc;
	}
}
