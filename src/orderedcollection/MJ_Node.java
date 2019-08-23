package orderedcollection;

/**
 *
 * @author Maisha Jauernig
 */
class MJ_Node <T> {
    private T _data;
    private MJ_Node<T> _next;
    
    MJ_Node(T s){
        _data = s;
    }
    
    T getData(){
        return _data;
    }
    
    void setData(T s){
        _data = s;
    }
    
    MJ_Node<T> getNext(){
        return _next;
    }
    
    void setNext(MJ_Node<T> n){
        _next = n;
    }
}
