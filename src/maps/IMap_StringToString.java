package maps;

/**
 *
 * @author Maisha
 */
public interface IMap_StringToString {
    
    boolean contains(String key);
    String getValueOfKey(String key);
    void add(String key, String val);
    boolean delete(String key);
    void clear();  // erases everything in the map
    int length();  // gets the number of keys in the map
    void print();
}
