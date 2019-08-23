package maps;

import Assert.Assertion;
import orderedcollection.*;

/**
 *
 * @author Maisha
 * @param <K>
 * @param <V>
 */
public class MJ_Map <K,V> implements IMJ_Map <K,V> {
    
    private IMJ_OC <K> _keys;
    private IMJ_OC <V> _values;
    
    /*public static void main(String[] args){
        IMJ_Map<Integer, String> mss = new MJ_Map<>();
        mss.add(1, "Maisha");
        mss.add(2, "Bilal");
        mss.print();
        System.out.println(mss.contains(1));
        //System.out.println(mss.contains("xxx"));
        System.out.println(mss.getValueOfKey(2));
        //System.out.println(mss.getValueOfKey("yyy"));
        System.out.println(mss.length());
        System.out.println(mss.delete(1));
        mss.print();
        //System.out.println(mss.delete("xyz"));
        mss.clear();
        mss.print();
    }*/
    
    public MJ_Map(){
        _keys = new MJ_OC_Factory<K>().create();
        _values = new MJ_OC_Factory<V>().create();
    }
    
    @Override
    public boolean contains(K key){
        Assertion.test(_keys != null, "there are no keys");
        for (int i = 0; i<_keys.length(); i++){
            if (_keys.getItem(i).equals(key)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public V getValueOfKey(K key){
        Assertion.test(_keys != null, "there are no keys");
        int idx = -1;
        for (int i = 0; i<_keys.length(); i++){
            if (_keys.getItem(i).equals(key)){
                idx = i;
                break;
            }
        }
        Assertion.test(idx > -1, "Key not found");
        return _values.getItem(idx);
    }
    
    @Override
    public void add(K key, V val){
        _keys.append(key);
        _values.append(val);
    }
    
    @Override
    public void setValueOfKey(K key, V val){
        Assertion.test(_keys != null, "there are no keys");
        int idx = -1;
        for (int i = 0; i<_keys.length(); i++){
            if (_keys.getItem(i).equals(key)){
                idx = i;
                break;
            }
        }
        Assertion.test(idx > -1, "Key not found");
        _values.setValue(idx, val);
    }
    
    @Override
    public boolean delete(K key){
        Assertion.test(_keys != null, "there are no keys");
        boolean success = false;
        for (int i = 0; i<_keys.length(); i++){
            if (_keys.getItem(i).equals(key)){
                _keys.deleteItem(i);
                _values.deleteItem(i);
                success = true;
            }
        }
        Assertion.test(success, "Key not found");
        return success;
    }
    
    @Override
    public K getKeyAtIdx(int i){
        return _keys.getItem(i);
    }
    
    @Override
    public void clear(){
        _keys = new MJ_OC_Factory<K>().create();
        _values = new MJ_OC_Factory<V>().create();
    }
    
    @Override
    public int length(){
        return _keys.length();
    }
    
    @Override
    public void print(){
        Assertion.test(_keys != null, "there are no items to print");
        for (int i = 0; i<_keys.length(); i++){
            System.out.println(_keys.getItem(i) + ": " + _values.getItem(i));
        }
    }
    
}
