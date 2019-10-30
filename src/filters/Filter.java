/**
 * 
 */
package filters;

import java.util.Arrays;

import constants.Constants;
import orderedcollection.*;

/**
 * @author Maisha Jauernig
 *
 */
public class Filter {
	private AbsFilterParams _params;

	public Filter(AbsFilterParams params) {
		_params = params;
	}

	public boolean checkFilterCondition(IMJ_OC<AbsFilterInput> inputs) {
		return _params.testInput(inputs);
	}
	
	public static IMJ_OC<Filter> parseFromString(String ruleRow) {
		ruleRow = ruleRow.replaceAll("\\\\", "").replaceAll("\"", "");
		if ( ! ruleRow.contains(Constants.FILTERS_LIST)) {
			return null;
		}
		ruleRow = ruleRow.substring(ruleRow.indexOf(Constants.FILTERS_LIST));
		ruleRow = ruleRow.substring(ruleRow.indexOf(':')+1);
		if (ruleRow.charAt(0) != '[') {
			return null;
		}
		
		String filtersList = ruleRow.substring(ruleRow.indexOf('[')+1, ruleRow.indexOf(']'));
		String[] filtersArr = filtersList.split(Constants.FILTER_PARAM_TO_VAL);
		filtersArr = Arrays.copyOfRange(filtersArr, 1, filtersArr.length);
		
		IMJ_OC<Filter> filters = new MJ_OC_Factory<Filter>().create();
		Filter f = null;
		
		for (String s: filtersArr) {
			String[] vals = s.split(Constants.FILTER_TYPE);
			String params = vals[0];
			params = params.substring(params.indexOf('{')+1, params.indexOf('}'));
			String filterType = vals[1];
			filterType = filterType.substring(filterType.indexOf(':')+1, filterType.indexOf('}'));
			
			if (filterType.equals(Constants.FILTER_TIME)) {
				f = new Filter(new TimeFilterParams(params));
			}
			else if (filterType.equals(Constants.FILTER_LOCATION)) {
				f = new Filter(new LocFilterParams(params));
			}
			else if (filterType.equals(Constants.FILTER_QUESTION)) {
				f = new Filter(new QuestionFilterParams(params));
			}
			
			filters.add(f);
		}
		return filters;
	}
}
