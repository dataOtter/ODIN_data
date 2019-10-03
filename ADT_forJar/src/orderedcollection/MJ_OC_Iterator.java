package orderedcollection;

import Assert.Assertion;
import java.util.Iterator;

/**
 *
 * @author Maisha Jauernig
 * @param <E>
 */
public class MJ_OC_Iterator<E> implements Iterator<Object> {
    private IMJ_OC<E> _oc;
    private int _idxOfCurrent;
    
    MJ_OC_Iterator(IMJ_OC<E> oc){
        _oc = oc;
        _idxOfCurrent = -1;
    }
    
    @Override
    public boolean hasNext() {
    	E ret = _oc.get(_idxOfCurrent + 1);
        return ret != null;
    }

    @Override
    public Object next() {
        Assertion.test(hasNext(), "No more elements in this collection");
        _idxOfCurrent++;
        return _oc.get(_idxOfCurrent);
    }

    @Override
    public void remove() {
        Assertion.test(_idxOfCurrent >= 0, "Remove attempted but no current was set in collection");
        _oc.remove(_idxOfCurrent);
    }
}
