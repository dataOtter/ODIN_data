package maps;

import Assert.Assertion;
import orderedcollection.*;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Maisha Jauernig
 * @param <K>
 * @param <V>
 */
public class MJ_Map_OC <K,V> implements IMJ_Map <K,V> {
    
    private IMJ_OC <K> _keys;
    private IMJ_OC <V> _values;
    
    public MJ_Map_OC() {
        _keys = new MJ_OC_Factory<K>().create();
        _values = new MJ_OC_Factory<V>().create();
    }
    
    private MJ_Map_OC(IMJ_OC <K> keys, IMJ_OC <V> values) {
        _keys = keys;
        _values = values;
    }
    
    @Override
    public void clear(){
        _keys = new MJ_OC_Factory<K>().create();
        _values = new MJ_OC_Factory<V>().create();
    }
    
    @Override
    public boolean containsKey(Object key){
        Assertion.test(_keys != null, "there are no keys");
        for (int i = 0; i<_keys.size(); i++){
            if (_keys.get(i).equals(key)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public V get(Object key){
        Assertion.test(_keys != null, "there are no keys");
        int idx = -1;
        for (int i = 0; i<_keys.size(); i++){
            if (_keys.get(i).equals(key)){
                idx = i;
                break;
            }
        }
        Assertion.test(idx > -1, "Key not found");
        return _values.get(idx);
    }

	@Override
	public IMJ_Map<K, V> getDeepCopy() {
		IMJ_OC <K> copyKeys = _keys.getDeepCopy();
	    IMJ_OC <V> copyVals = _values.getDeepCopy();
	    IMJ_Map<K, V> copy = new MJ_Map_OC<K, V>(copyKeys, copyVals);
		return copy;
	}
    
    @Override
    public K getKey(int idx){
    	return _keys.get(idx);
    }
    
    @Override
    public boolean isEmpty(){
        return _keys.isEmpty();
    }
    
    @Override
    public void print(){
        Assertion.test(_keys != null, "there are no items to print");
        for (int i = 0; i<_keys.size(); i++){
            System.out.println(_keys.get(i) + ": " + _values.get(i));
        }
    }
    
    @Override
    public V put(K key, V val){
        V v = null;
        if (_keys.contains(key)){
            int i = _keys.indexOf(key);
            v = _values.get(i);
            _values.set(i, val);
        }
        else{
            _keys.add(key);
            _values.add(val);
        }
        return v;
    }
    
    @Override
    public V remove(Object key){
        Assertion.test(_keys != null, "there are no keys");
        V v = null;
        for (int i = 0; i<_keys.size(); i++){
            if (_keys.get(i).equals(key)){
                _keys.remove(i);
                v = _values.get(i);
                _values.remove(i);
            }
        }
        //Assertion.test(v != null, "Key not found");
        return v;
    }
    
    @Override
    public V replace(K key, V val){
        Assertion.test(_keys != null, "there are no keys");
        V v = null;
        if (_keys.contains(key)){
            int i = _keys.indexOf(key);
            v = _values.get(i);
            _values.set(i, val);
        }
        return v;
    }
    
    @Override
    public int size(){
        return _keys.size();
    }
    
    @Override 
    public String toString() {
    	String s = "";
        for (int i = 0; i<this.size(); i++){
        	K k = _keys.get(i);
        	V v = _values.get(i);
            s += k + ": " + v + "\n";
        }
		return s.substring(0, s.length()-1);
    }

    /*@Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
