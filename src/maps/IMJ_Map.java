package maps;

import java.util.Map;

import Assert.Assertion;

/**
 * An object that maps keys to values. A map cannot contain duplicate keys; 
 * each key can map to at most one value.
 * @author Maisha
 * @param <K>
 * @param <V>
 */
public interface IMJ_Map <K, V> {//extends Map <K,V> {
	
	public void clear();
	public boolean containsKey(Object key);
    public V get(Object key);
    public boolean isEmpty();
    public V put(K key, V val);
    public V remove(Object key);
    public int size();
    
    /**
     * @return Returns a deep copy of this.
     */
    IMJ_Map <K, V> getDeepCopy();
	
	/**
     * @param idx
     * @return Returns the key at index idx. 
     */
    K getKey(int idx);
    
    /**
     * Pretty prints the contents of this map.
     */
    void print();
    
    /**
     * Replaces the entry for the specified key only if it is currently mapped to some value.
     * @param key
     * @param val
     * @return Returns the previous value associated with the specified key, 
     * or null if there was no mapping for the key. (A null return can also 
     * indicate that the map previously associated null with the key, 
     * if the implementation supports null values.)
     */
    V replace(K key, V val);
}
