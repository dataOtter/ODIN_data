package orderedcollection;

import Assert.Assertion;
import java.util.ListIterator;

/**
 *
 * @author Maisha Jauernig
 * @param <E>
 */
public class MJ_OC_ListIterator<E> implements ListIterator<Object> {
    private IMJ_OC<E> _oc;
    private int _idxOfNext;
    
    MJ_OC_ListIterator(IMJ_OC<E> oc){
        _oc = oc;
        _idxOfNext = 0;
    }    
    
    MJ_OC_ListIterator(int idx, IMJ_OC<E> oc){
        _oc = oc;
        _idxOfNext = idx;
    }

	@Override
	public void add(Object arg0) {
		// some assertion that arg0 is of type E
		_oc.add(_idxOfNext, (E) arg0);
	}
    
    @Override
    public boolean hasNext() {
        return _oc.get(_idxOfNext) != null;
    }

	@Override
	public boolean hasPrevious() {
        return _oc.get(_idxOfNext - 1) != null;
	}

    @Override
    public Object next() {
        Assertion.test(hasNext(), "No next element in this collection");
        Object o = _oc.get(_idxOfNext);
        _idxOfNext++;
        return o;
    }

	@Override
	public int nextIndex() {
		return _idxOfNext;
	}

	@Override
	public Object previous() {
        Assertion.test(hasPrevious(), "No previous element in this collection");
        _idxOfNext--;
        return _oc.get(_idxOfNext);
	}

	@Override
	public int previousIndex() {
		return _idxOfNext - 1;
	}

    @Override
    public void remove() {
    	Assertion.test(false, "This method is not implemented yet");
    }

	@Override
	public void set(Object arg0) {
    	Assertion.test(false, "This method is not implemented yet");
		
	}
}
