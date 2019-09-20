package dao;

import java.io.File;
import java.util.Scanner;

import Assert.Assertion;

public class ScannerHelper {
	private final String _path;
	private Scanner _sc;
	
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
	
	public Scanner getScanner() {
		return _sc;
	}
}
