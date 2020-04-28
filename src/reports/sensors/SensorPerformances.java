package reports.sensors;

import constants.Constants;
import sensors.data.SensorDataOfOneType;

public class SensorPerformances {
	private final double _idealRecs;
	private final double _recs;
	private double _avg;
	private double _min;
	private double _max;
	private double _recsInTInt;	
	private final double _per = Constants.PERCENT_ALLOWED_DEVIATION_FROM_SI;

	protected SensorPerformances(SensorDataOfOneType data, double si, double windowInSecs) {
		setValues(data, si);
		_idealRecs = windowInSecs / si;
		_recs = (double) data.size();
	}
	
	protected Double getAvg() {
		return _avg;
	}
	
	protected Double getMin() {
		return _min;
	}
	
	protected Double getMax() {
		return _max;
	}
	
	// aka good recs
	protected Double getRecsInTInt() {
		return _recsInTInt;
	}
	
	protected Double getIdealRecs() {
		return _idealRecs;
	}
	
	protected Double getNumRecs() {
		return _recs;
	}

	private void setValues(SensorDataOfOneType data, double sensorInterval) {
        if (data.isEmpty()) {
        	return;
        }
        
        long t1 = data.get(0).getDateTime().getTimeInMillis();
        long t2;
        int numDataPoints = data.size();
        
        // avg
        int numTimeIntervals = numDataPoints - 1;
        long sum = 0;
        
        // max, min
        long tempDiff;
        long maxDiff = 0;
        long minDiff = 999999999;
        
        // _recsInTInt
        long upperBound = Math.round((sensorInterval + (_per * sensorInterval)) * 1000);
        long lowerBound = Math.round((sensorInterval - (_per * sensorInterval)) * 1000);
        double recsInTIntCount = 1;
        
        
        for (int i = 1; i<numDataPoints; i++){
            t2 = data.get(i).getDateTime().getTimeInMillis();
            // avg
            sum += t2-t1;
            
            // max, min, _recsInTInt
            tempDiff = t2-t1;
            // max
            if (tempDiff > maxDiff){
                maxDiff = tempDiff;
            }
            // min
            if (tempDiff < minDiff){
                minDiff = tempDiff;
            }
            // _recsInTInt
            if (tempDiff >= lowerBound && tempDiff <= upperBound){
            	recsInTIntCount += 1;
            }
            
            t1 = t2;
        }
        
        // avg
        long avg = sum / numTimeIntervals;
        double avgInSecs = avg / 1000.0;
        _avg = (double) Math.round(avgInSecs);
        
        // max
        double answerMax = maxDiff / 1000.0;
        _max = (double) Math.round(answerMax);
		
		// min
        double answerMin = minDiff / 1000.0;
        _min = (double) Math.round(answerMin);
        
        // _recsInTInt
        _recsInTInt = recsInTIntCount;
	}

}
