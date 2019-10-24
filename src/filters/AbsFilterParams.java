/**
 * 
 */
package filters;

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
	
	public abstract boolean testInput(IMJ_OC<AbsFilterInput> inputs);

	/**
	 * @return the _type
	 */
	public String getType() {
		return _type;
	}
}
