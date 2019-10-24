/**
 * 
 */
package filters;

import constants.Constants;

/**
 * @author Maisha Jauernig
 *
 */
public class TimeFilterInput extends AbsFilterInput {
	
	public TimeFilterInput(double timeNow) {
		super(Constants.FILTER_TIME, timeNow);
	}
}
