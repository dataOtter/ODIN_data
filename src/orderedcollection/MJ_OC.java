package orderedcollection;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Maisha Jauernig
 * @param <E>
 */
public abstract class MJ_OC<E> implements IMJ_OC<E>{
	
	public MJ_OC(){
    	super();
    }
	
    @Override
    public boolean containsAll(Collection<?> c){
        // should assert that the elements in c are the same as in this
        Iterator<?> iter = c.iterator();
        while (iter.hasNext()){
            if ( ! contains(iter.next()) ){
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isEmpty(){
        return size() == 0;
    }
    
    @Override
    public Iterator<E> iterator(){
        return (Iterator<E>) new MJ_OC_Iterator<E>(this);
    }
}
