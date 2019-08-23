package orderedcollection;

/**
 *
 * @author Maisha Jauernig
 * @param <T>
 */
public interface IMJ_OC <T> {
    int length();
    void prepend(T value);
    T getItem(int index);
    void append(T value);
    void deleteItem(int index);
    void setValue(int index, T newValue);
    void insert(int index, T value);
    void printAll();
    boolean contains(T value);
    IMJ_OC <T> getDeepCopy();
}
