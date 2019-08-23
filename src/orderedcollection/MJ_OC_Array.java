package orderedcollection;

import Assert.Assertion;

/**
 *
 * @author Maisha Jauernig
 * @param <T>
 */
public class MJ_OC_Array <T> implements IMJ_OC <T> {
    private T[] _storage;
    private int _storageSize;
    private int _slotsUsed;
    private final int _memoryStepSize;
    
    public MJ_OC_Array(int len){
        _storage = (T[]) new Object[len];
        _storageSize = len;
        _memoryStepSize = len;
        _slotsUsed = 0;
    }
    
    @Override
    public int length(){
        return _slotsUsed;
    }
    
    @Override
    public void prepend(T s){
        if (_slotsUsed == _storageSize){
            makeArrayLonger();
        }
        for (int i = _slotsUsed; i>0; i--){
            _storage[i] = _storage[i-1];
        }
        _storage[0] = s;
        _slotsUsed += 1;
    }
    
    private void makeArrayLonger(){
        T[] tempStorage = _storage;
        _storageSize *= 2;
        _storage = (T[]) new Object[_storageSize];

        for (int i = 0; i<_storageSize/2; i++){
            _storage[i] = tempStorage[i];
        }
    }
    
    @Override
    public T getItem(int index){
        Assertion.test(index < length(), "Index out of range");
        return _storage[index];
    }
    
    @Override
    public boolean contains(T value){
        for (int i = 0; i<_slotsUsed; i++){
            if (_storage[i] == value){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void append(T s){
        if (_slotsUsed == _storageSize){
            makeArrayLonger();
        }
        _storage[_slotsUsed] = s;
        _slotsUsed += 1;
    }
    
    @Override
    public void deleteItem(int index){
        Assertion.test(index < length(), "Index out of range");
        
        for (int i = index; i<(_storageSize-1); i++){
            _storage[i] = _storage[i+1];
        }
        _storage[_storageSize-1] = null;
        _slotsUsed -= 1;
    }
    
    @Override
    public void setValue(int index, T newValue){
        _storage[index] = newValue;
    }
    
    @Override
    public void insert(int index, T value){
        Assertion.test(index > _slotsUsed, "Index out of range");
        
        if (index == _slotsUsed){
            append(value);
        }
        else if (index == 0){
            prepend(value);
        }
        else{
            _slotsUsed += 1;
            if (_slotsUsed == _storageSize){
                makeArrayLonger();
            }
            T[] tempStorage = (T[]) new Object[_storageSize];
            
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
    public void printAll(){
        for (int i = 0; i<_storageSize; i++){
            System.out.println(_storage[i]);
        }
        System.out.println("\n");
    }
    
    @Override
    public IMJ_OC <T> getDeepCopy(){
        IMJ_OC <T> stor = new MJ_OC_Array(_memoryStepSize);
        for (int i = 0; i<_storageSize; i++){
            stor.append(_storage[i]);
        }
        return stor;
    }
}
