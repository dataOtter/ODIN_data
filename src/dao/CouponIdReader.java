package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import orderedcollection.IMJ_OC;
import orderedcollection.MJ_OC_Factory;
import studysensors.Constants;

public class CouponIdReader {
	private final String _path;
	private final int _formatVersion;
	
	public CouponIdReader(String path, int formatVersion) {
		_path = path;
		_formatVersion = formatVersion;
	}
	
	public IMJ_OC<Integer> getActiveCouponIds() throws FileNotFoundException{
        File f = new File(_path + "\\" + Constants.COUPON_CSV);
        Scanner sc = new Scanner(f);
        String[] line;
        int cid;
        String consentStatus;
        IMJ_OC<Integer> cids = new MJ_OC_Factory<Integer>().create();
        
        sc.nextLine();
        while (sc.hasNextLine()){
            line = sc.nextLine().split(",");
            cid = Integer.parseInt(line[Constants.COUPON_COUPONID_IDX]);
            consentStatus = line[Constants.COUPON_CONSENTSTATUS_IDX];
            
            if ("consentAgreed".equals(consentStatus)){
                cids.append(cid);
            }
        }
        sc.close();
        return cids;
    }

}
