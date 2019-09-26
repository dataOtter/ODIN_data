package orderedcollection;

import Assert.Assertion;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Maisha Jauernig
 * @param <E>
 */
public class MJ_OC_Array<E> extends MJ_OC<E> {
	
    private E[] _storage;
    private int _storageSize;
    private int _slotsUsed;
    private final int _memoryStepSize;
    private final int _initialLen;
    
    public MJ_OC_Array(int len){
        _initialLen = len;
        _storage = (E[]) new Object[_initialLen];
        _storageSize = _initialLen;
        _memoryStepSize = _initialLen;
        _slotsUsed = 0;
    }
    
    @Override
    public boolean add(E e){
        if (_slotsUsed == _storageSize){
            makeArrayLonger();
        }
        _storage[_slotsUsed] = e;
        _slotsUsed += 1;
        return true;  // eventually have something that makes sure it was appended
    }
    
    @Override
    public void add(int index, E value){
        Assertion.test(index > _slotsUsed, "Index out of range");
        
        if (index == _slotsUsed){
            MJ_OC_Array.this.add(value);
        }
        else if (index == 0){
            prepend(value);
        }
        else{
            _slotsUsed += 1;
            if (_slotsUsed == _storageSize){
                makeArrayLonger();
            }
            E[] tempStorage = (E[]) new Object[_storageSize];
            
            for (int i = 0; i<index; i++){
                tempStorage[i] = _storage[i];
            }
            
            tempStorage[index] = value;
            for (int i = index+1; i<_storageSize; i++){
                tempStorage[i] = _storage[i-1];
            }
            _storage = tempStorage;
        }
    }
    
    @Override
    public void clear(){
        _storage = (E[]) new Object[_initialLen];
    }
    
    @Override
    public boolean contains(Object value){
        for (int i = 0; i<_slotsUsed; i++){
            if (_storage[i] == value){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public E get(int index){
        //Assertion.test(index < length(), "Index out of range");
        if (size() <= index){
            return null;
        }
        return _storage[index];
    }
    
    @Override
    public IMJ_OC <E> getDeepCopy(){
        IMJ_OC <E> stor = new MJ_OC_Array<E>(_memoryStepSize);
        for (int i = 0; i<_storageSize; i++){
            stor.add(_storage[i]);
        }
        return stor;
    }
    
    @Override
    public void prepend(E e){
        if (_slotsUsed == _storageSize){
            makeArrayLonger();
        }
        for (int i = _slotsUsed; i>0; i--){
            _storage[i] = _storage[i-1];
        }
        _storage[0] = e;
        _slotsUsed += 1;
    }
    
    @Override
    public void printAll(){
        for (int i = 0; i<_storageSize; i++){
            System.out.println(_storage[i]);
        }
        System.out.println("\n");
    }
    
    @Override
    public E remove(int index){
        //Assertion.test(index < length(), "Index out of range");
        if (size() <= index){
            return null;
        }
        E t = _storage[index];
        // shift things after the item to delete up
        for (int i = index; i<(_storageSize-1); i++){
            _storage[i] = _storage[i+1];
        }
        _storage[_storageSize-1] = null;
        _slotsUsed -= 1;
        return t;
    }
    
    @Override
    public E set(int index, E newValue){
        if ( index < 0 || index >= _slotsUsed){
            return null;
        }
        E e = _storage[index];
        _storage[index] = newValue;
        return e;
    }
    
    @Override
    public int size(){
        return _slotsUsed;
    }

    @Override
    public Object[] toArray() {
        E[] arr = (E[]) new Object[_slotsUsed];
        for (int i = 0; i<_slotsUsed; i++){
            arr[i] = _storage[i];
        }
        return arr;
    }
    
    @Override
	public String toString() {
    	return _storage.toString();
	}
    
    private void makeArrayLonger(){
        E[] tempStorage = _storage;
        _storageSize *= 2;
        _storage = (E[]) new Object[_storageSize];

        for (int i = 0; i<_storageSize/2; i++){
            _storage[i] = tempStorage[i];
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
