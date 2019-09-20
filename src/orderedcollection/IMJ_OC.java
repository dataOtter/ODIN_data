package orderedcollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Maisha Jauernig
 * @param <E>
 */
public interface IMJ_OC <E> extends Iterable <E> { // extends List<E> {
    
	boolean add(E x);
	void add(int i, E x);
	void clear();
	E get(int i);
	boolean contains(Object value);
	E remove(int index);
	E set(int index, E newValue);
	int size();
	Object[] toArray();
	<T> T[] toArray(T[] a);
	boolean remove(Object o);
	boolean addAll(Collection<? extends E> c);
	boolean removeAll(Collection<?> c);
	boolean retainAll(Collection<?> c);
	boolean addAll(int index, Collection<? extends E> c);
	int indexOf(Object o);
	int lastIndexOf(Object o);
	ListIterator<E> listIterator();
	ListIterator<E> listIterator(int index);
	List<E> subList(int fromIndex, int toIndex);
	
	boolean containsAll(Collection<?> c);
	boolean isEmpty();
	Iterator<E> iterator();
	
    /**
     * @return Returns a deep copy of this collection.
     */
    IMJ_OC <E> getDeepCopy();
    
    /**
     * Prepends the specified element to the beginning of this collection.
     * @param e
     */
    void prepend(E e);
    
    /**
     * Pretty prints all elements in this collection.
     */
    void printAll();
}
