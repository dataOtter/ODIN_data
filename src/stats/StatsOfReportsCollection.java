package stats;

import orderedcollection.IMJ_OC;

/**
 *
 * @author Maisha Jauernig
 */
public class StatsOfReportsCollection {

	private ReportsCollection _reps;
	
	public StatsOfReportsCollection(ReportsCollection reps) {
		_reps = reps;
	}
	
	public OneStatsReport getValues() {
		OneStatsReport answer = new OneStatsReport();
		IMJ_OC<String> tagsList = _reps.getAllUniqueTags();
		for (String tag: tagsList) {
			IMJ_OC<Double> val_list = _reps.getAllValuesForTag(tag);
			
			double ave = getAvg(val_list);
			String ave_tag = "Average_" + tag;
			answer.addValue(ave_tag, ave);
			
			double stdev = getStdev(val_list, ave);
			String stdev_tag = "Stdev_" + tag;
			answer.addValue(stdev_tag, stdev);
			
			double max = getMax(val_list);
			String max_tag = "Max_" + tag;
			answer.addValue(max_tag, max);
			
			double min = getMin(val_list);
			String min_tag = "Min_" + tag;
			answer.addValue(min_tag, min);
		}
		
		return answer;
	}
	
	private Double getAvg(IMJ_OC<Double> values){
		double count = values.size();
		double sum = 0;
		for (int j = 0; j<count; j++){
			sum += values.get(j);
		}
		return sum/count;
	}

	private Double getStdev(IMJ_OC<Double> values, double avg){
		double count = values.size();
		double sumSqDiff = 0;
		for (int j = 0; j<count; j++){
			double per = values.get(j);
			sumSqDiff += (per - avg) * (per - avg);
		}
		return Math.sqrt(sumSqDiff/count);
	}
	
	private Double getMax(IMJ_OC<Double> values){
		double max = values.get(0);
		for (double temp: values) {
			if (temp > max) {
				max = temp;
			}
		}
		return max;
	}
	
	private Double getMin(IMJ_OC<Double> values){
		double min = values.get(0);
		for (double temp: values) {
			if (temp < min) {
				min = temp;
			}
		}
		return min;
	}
}
