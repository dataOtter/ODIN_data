package maps;

/**
 *
 * @author Maisha
 * @param <K>
 * @param <V>
 */
public interface IMJ_Map <K, V> {
    
    boolean contains(K key);
    V getValueOfKey(K key);
    void add(K key, V val);
    void setValueOfKey(K key, V val);
    boolean delete(K key);
    K getKeyAtIdx(int i);
    void clear();  // erases everything in the map
    int length();  // gets the number of keys in the map
    void print();
    
}
