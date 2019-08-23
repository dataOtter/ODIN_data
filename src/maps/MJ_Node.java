package maps;

/**
 *
 * @author Maisha Jauernig
 */
class MJ_Node <K,V> {
    private K _key;
    private V _value;
    private MJ_Node<K,V> _next;
    
    MJ_Node(K k, V v){
        _key = k;
        _value = v;
    }
    
    K getKey(){
        return _key;
    }
    
    V getValue(){
        return _value;
    }
    
    void setData(K k, V v){
        _key = k;
        setValue(v);
    }
    
    void setValue(V v){
        _value = v;
    }
    
    MJ_Node<K,V> getNext(){
        return _next;
    }
    
    void setNext(MJ_Node<K,V> n){
        _next = n;
    }
}
