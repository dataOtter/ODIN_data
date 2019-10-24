/**
 * 
 */
package filters;

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
}
