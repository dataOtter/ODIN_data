package dao;

import java.io.IOException;

public class ZipReportWriter extends AbsReportWriter{
    
    public ZipReportWriter(String path, int formatVersion) {
    	super(null, path, formatVersion, null);
    }
	
	@Override
	protected void writeDataToFile(int studyId, String reportName, String colsToWrite, String folderName)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
