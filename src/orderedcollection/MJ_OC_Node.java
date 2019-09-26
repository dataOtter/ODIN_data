package orderedcollection;

/**
 *
 * @author Maisha Jauernig
 */
class MJ_OC_Node <E> {
    private E _data;
    private MJ_OC_Node<E> _next;
    
    MJ_OC_Node(E e) {
        _data = e;
        _next = null;
    }
    
    E getData() {
        return _data;
    }
    
    void setData(E e) {
        _data = e;
    }
    
    boolean hasNext() {
        return _next != null;
    }
    
    MJ_OC_Node<E> getNext() {
        return _next;
    }
    
    void setNext(MJ_OC_Node<E> n) {
        _next = n;
    }
    
    @Override
    public String toString() {
    	return _data.toString();
    }
}
