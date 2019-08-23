package maps;

import Assert.Assertion;
import orderedcollection.*;

/**
 *
 * @author Maisha
 */
public class Map_StringToString implements IMap_StringToString {
    
    private IMJ_OC <String> _keys;
    private IMJ_OC <String> _values;
    
    /*public static void main(String[] args){
        Map_StringToString mss = new Map_StringToString();
        mss.add("name1", "Maisha");
        mss.add("name2", "Bilal");
        mss.print();
        System.out.println(mss.contains("name1"));
        //System.out.println(mss.contains("xxx"));
        System.out.println(mss.getValueOfKey("name2"));
        //System.out.println(mss.getValueOfKey("yyy"));
        System.out.println(mss.length());
        System.out.println(mss.delete("name1"));
        mss.print();
        //System.out.println(mss.delete("xyz"));
        mss.clear();
        mss.print();
    }*/
    
    public Map_StringToString(){
        _keys = new MJ_OC_Factory<String>().create();
        _values = new MJ_OC_Factory<String>().create();
    }
    
    @Override
    public boolean contains(String key){
        for (int i = 0; i<_keys.length(); i++){
            if (_keys.getItem(i).equals(key)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getValueOfKey(String key){
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
    public void add(String key, String val){
        _keys.append(key);
        _values.append(val);
    }
    
    @Override
    public boolean delete(String key){
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
    public void clear(){  // erases everything in the map
        _keys = new MJ_OC_Factory<String>().create();
        _values = new MJ_OC_Factory<String>().create();
    }
    
    @Override
    public int length(){  // gets the number of keys in the map
        return _keys.length();
    }
    
    @Override
    public void print(){
        for (int i = 0; i<_keys.length(); i++){
            System.out.println(_keys.getItem(i) + ": " + _values.getItem(i));
        }
    }
}
