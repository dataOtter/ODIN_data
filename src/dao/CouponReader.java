package dao;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Scanner;

import constants.Constants;
import maps.*;

/**
 * Reads all data from the coupon.csv file
 * @author Maisha Jauernig
 */
public class CouponReader {
	private final String _path;
	private final int _formatVersion;
	
	/**
	 * @param path
	 * @param formatVersion
	 */
	public CouponReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
	}
	
	/**
	 * @return CouponCollection of coupon IDs to coupon numbers
	 * @throws ParseException 
	 */
	public CouponCollection getAllCoupons() throws ParseException {
		IMJ_Map<Integer, IMJ_Map<String, Calendar>> uploadtimes = new ServieToHeartbeatReader(_path, _formatVersion).getUploadTimes();

		int numCols;
		if (_formatVersion == 2) {
			numCols = Constants.COUPON_NUM_COLS_V2;
		}
		else {
			numCols = Constants.COUPON_NUM_COLS;
		}
        Scanner sc = new ScannerHelper(_path, Constants.COUPON_CSV, numCols).getScanner();
        
        CouponCollection coll = new CouponCollection();
        
        while (sc.hasNextLine()){
        	String line = sc.nextLine();
        	String[] lineArr = line.split(",");
            String consentStatus = lineArr[Constants.COUPON_CONSENTSTATUS_IDX];
            if (Constants.COUPON_CONSENTSTATUS_CONSENTAGREED.equals(consentStatus)){
            	coll.add(new OneCoupon(line, uploadtimes));
            }
        }
        sc.close();
        
        return coll;
	}
	
	/**
	 * @return CouponCollection of coupon IDs to coupon numbers that belong to participants whose consentstatus is "agreed"
	 * @throws ParseException 
	 */
	public CouponCollection getActiveCoupons() throws ParseException {
        return getAllCoupons().getActiveCoupons();
	}
	
	/**
	 * @return IMJ_Map<Integer, String> of coupon IDs to coupon numbers that belong to participants whose consentstatus is "agreed"
	 */
	public IMJ_Map<Integer, String> getActiveCouponIdsToNumbers() {
		int numCols;
		if (_formatVersion == 2) {
			numCols = Constants.COUPON_NUM_COLS_V2;
		}
		else {
			numCols = Constants.COUPON_NUM_COLS;
		}
        Scanner sc = new ScannerHelper(_path, Constants.COUPON_CSV, numCols).getScanner();
        
        IMJ_Map<Integer, String> cidToNo = new MJ_Map_Factory<Integer, String>().create();
        
        while (sc.hasNextLine()){
        	String[] line = sc.nextLine().split(",");
            int cid = Integer.parseInt(line[Constants.COUPON_COUPONID_IDX]);
            String cno = line[Constants.COUPON_COUPONNUMBER_IDX];
            String consentStatus = line[Constants.COUPON_CONSENTSTATUS_IDX];
            
            if (Constants.COUPON_CONSENTSTATUS_CONSENTAGREED.equals(consentStatus)){
            	cidToNo.put(cid, cno);
            }
        }
        sc.close();
        return cidToNo;
	}
}
