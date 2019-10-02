package orderedcollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
    public boolean addAll(Collection<? extends E> c) {
    	int lenBefore = this.size();
    	Iterator<? extends E> iter = c.iterator();
    	iter.forEachRemaining(e -> this.add(e));
    	if (lenBefore < this.size()) {
    		return true;
    	}
    	return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
    	int lenBefore = this.size();
    	
    	Iterator<? extends E> iter = c.iterator();
    	while (iter.hasNext()) {
    		this.add(index, iter.next());
    		index++;
    	}
    	
    	if (lenBefore < this.size()) {
    		return true;
    	}
    	return false;
	}
    
    @Override
    public boolean contains(Object value) {
    	Iterator<E> iter = this.iterator();
    	
    	while (iter.hasNext()) {
    		if (iter.next().equals(value)) {
                return true;
            }
    	}
        return false;
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
    public int indexOf(Object o) {
    	for (int i = 0; i<this.size(); i++) {
    		if (this.get(i).equals(o)) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    @Override
    public boolean isEmpty(){
        return size() == 0;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public Iterator<E> iterator(){
        return (Iterator<E>) new MJ_OC_Iterator<E>(this);
    }

    @Override
    public int lastIndexOf(Object o) {
    	int idx = -1;
    	for (int i = 0; i<this.size(); i++) {
    		if (this.get(i).equals(o)) {
    			idx = i;
    		}
    	}
    	return idx;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public ListIterator<E> listIterator() {
        return (ListIterator<E>) new MJ_OC_ListIterator<E>(this);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public ListIterator<E> listIterator(int index) {
        return (ListIterator<E>) new MJ_OC_ListIterator<E>(index, this);
    }

    @Override
    public boolean remove(Object o) {
    	Iterator<E> iter = this.iterator();
    	while (iter.hasNext()) {
    		if (iter.next().equals(o)) {
    			iter.remove();
    			return true;
    		}
    	}
    	return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
    	int lenBefore = this.size();
    	
    	Iterator<?> iter = c.iterator();
    	iter.forEachRemaining(e -> this.remove(e));
    	
    	if (lenBefore > this.size()) {
    		return true;
    	}
    	return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
    	int lenBefore = this.size();
    	IMJ_OC<E> toRemove = new MJ_OC_Factory<E>().create();
    	Iterator<E> iter = this.iterator();
    	while (iter.hasNext()) {
    		E element = iter.next();
    		if (! c.contains(element)) {
    			toRemove.add(element);
    		}
    	}
    	
    	iter = toRemove.iterator();
    	iter.forEachRemaining(e -> this.remove(e));
    	if (lenBefore > this.size()) {
    		return true;
    	}
    	return false;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
    	List<E> toReturn = new LinkedList<E>();
    	if (fromIndex != toIndex-1) {
    		for (int i = fromIndex; i<toIndex; i++) {
        		toReturn.add(this.get(i));
        	}
    	}
    	return toReturn;
    }

    @Override
    public Object[] toArray() {
    	@SuppressWarnings("unchecked")
		E[] arr = (E[]) new Object[this.size()];
    	
    	Iterator<E> iter = this.iterator();
    	for (int i = 0; i<this.size(); i++) {
    		arr[i] = (E) iter.next();
    	}
    	return arr;
    }
}
