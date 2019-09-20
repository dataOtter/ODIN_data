package maps;

/**
 *
 * @author Maisha Jauernig
 * @param <K>
 * @param <V>
 */
public class MJ_Map_EntryNode <K,V> {
    private K _key;
    private V _value;
    private MJ_Map_EntryNode<K,V> _next;
    
    public MJ_Map_EntryNode(K k, V v){
        _key = k;
        _value = v;
    }
    
    public K getKey(){
        return _key;
    }
    
    public V getValue(){
        return _value;
    }
    
    public void setData(K k, V v){
        _key = k;
        setValue(v);
    }
    
    public void setValue(V v){
        _value = v;
    }
    
    public MJ_Map_EntryNode<K,V> getNext(){
        return _next;
    }
    
    public boolean hasNext(){
        return _next != null;
    }
    
    public void setNext(MJ_Map_EntryNode<K,V> n){
        _next = n;
    }
}
