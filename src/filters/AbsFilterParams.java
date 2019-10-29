/**
 * 
 */
package filters;

import java.util.Arrays;

import constants.Constants;
import orderedcollection.IMJ_OC;

/**
 * @author Maisha Jauernig
 *
 */
public abstract class AbsFilterParams {
	private final String _type;
	
	public AbsFilterParams(String type) {
		_type = type;
	}
	
	public static AbsFilterParams parseFromString(String ruleRow) {
		ruleRow = ruleRow.replace("\\", "").replace("\"", "");
		String filtersList = ruleRow.substring(ruleRow.indexOf('['), ruleRow.indexOf(']')+1);
		String[] filtersArr = filtersList.split(Constants.FILTER_PARAM_TO_VAL);
		filtersArr = Arrays.copyOfRange(filtersArr, 1, filtersArr.length);
		
		AbsFilterParams filterParam = null;
		
		for (String s: filtersArr) {
			String[] vals = s.split(Constants.FILTER_TYPE);
			
			String params = vals[0];
			params = params.substring(params.indexOf('{')+1, params.indexOf('}'));
			
			String filterType = vals[1];
			filterType = filterType.substring(filterType.indexOf(':')+1, filterType.indexOf('}'));
			
			if (filterType.equals(Constants.FILTER_TIME)) {
				filterParam =  new TimeFilterParams(params);
			}
			else if (filterType.equals(Constants.FILTER_LOCATION)) {
				filterParam =  new LocFilterParams(params);
			}
			else if (filterType.equals(Constants.FILTER_QUESTION)) {
				filterParam =  new QuestionFilterParams(params);
			}
		}
		return filterParam;
	}
	
	public abstract boolean testInput(IMJ_OC<AbsFilterInput> inputs);

	/**
	 * @return the _type
	 */
	public String getType() {
		return _type;
	}
}
