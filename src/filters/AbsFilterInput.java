/**
 * 
 */
package filters;

/**
 * @author Maisha Jauernig
 *
 */
public class AbsFilterInput {
	private String _type;
	private final double _timeNowSecs;
	
	public AbsFilterInput(String type, double time) {
		_type = type;
		_timeNowSecs = time;
	}
	
	public String getType() {
		return _type;
	}

	/**
	 * @return the _timeNowSecs
	 */
	public double getTimeNowSecs() {
		return _timeNowSecs;
	}
}
