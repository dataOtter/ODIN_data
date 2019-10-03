package orderedcollection;

import Assert.Assertion;

/**
 *
 * @author Maisha Jauernig
 * @param <E>
 */
public class MJ_OC_List <E> extends MJ_OC <E>{
    private MJ_OC_Node<E> _start = null;
    private int _len = 0;
    
    public MJ_OC_List(){
    	super();
    }
    
    @Override
    public boolean add(E e){
        MJ_OC_Node<E> newNode = new MJ_OC_Node<>(e);
        
        if (_start == null){
            _start = newNode;
        }
        
        else{
            MJ_OC_Node<E> node = _start;
            
            while (node.hasNext()){
                node = node.getNext();
            }
            node.setNext(newNode);
        }
        _len++;
        return true;  // eventually have something that makes sure it was appended
    }
    
    @Override
    public void add(int index, E value){
        int len = size();
        Assertion.test(index < len, "Index out of range");
        
        if (index == len){
            add(value);
        }
        else if (index == 0){
            prepend(value);
        }
        else{
            MJ_OC_Node<E> n = new MJ_OC_Node<>(value);
            MJ_OC_Node<E> nRight = getNodeAtIdx(index);
            MJ_OC_Node<E> nLeft = getNodeAtIdx(index-1);
            n.setNext(nRight);
            nLeft.setNext(n);
        }
        _len++;
    }

    @Override
    public void clear() {
        _start = null;
        _len = 0;
    }
    
    @Override
    public E get(int index){
        if (_len <= index){
            return null;
        }
        return getNodeAtIdx(index).getData();
    }
    
    @Override
    public IMJ_OC <E> getDeepCopy(){
        IMJ_OC <E> copy = new MJ_OC_List<E>();
        for (int i = 0; i<_len; i++){
            copy.add(this.get(i));
        }
        return copy;
    }
    
    @Override
    public void prepend(E e){
        if (_start == null){
            _start = new MJ_OC_Node<>(e);
        }
        else{
            MJ_OC_Node<E> newNode = new MJ_OC_Node<>(e);
            newNode.setNext(_start);
            _start = newNode;
        }
        _len++;
    }
    
    @Override
    public void printAll(){
        Assertion.test(_start != null, "Collection is empty");
        for (MJ_OC_Node<E> i = _start; i != null; i = i.getNext()){
            System.out.println(i.getData());
        }
        System.out.println("\n");
    }
    
    @Override
    public E remove(int index){
        if (_len <= index){
            return null;
        }
        
        E t;
        if (index == 0){  // removing first item
            t = _start.getData();
            if (_start.getNext() == null){  // if _start is the only item
                _start = null;
            }
            else{  // if there is more than one item
                _start = _start.getNext();  // set start to be the next item
            }
        }
        
        else{  // removing an item that is not the first item
            MJ_OC_Node<E> nodeToLeft = getNodeAtIdx(index-1);
            Assertion.test(nodeToLeft != null, "Object at previous index does not exist");
            
            MJ_OC_Node<E> nodeToDelete = nodeToLeft.getNext();
            Assertion.test(nodeToDelete != null, "No object at index to delete");
            
            MJ_OC_Node<E> nodeToRight = nodeToDelete.getNext();
            nodeToLeft.setNext(nodeToRight);
            
            t = nodeToDelete.getData();
        }
        _len--;
        return t;
    }
    
    @Override
    public E set(int index, E newValue){
        if ( index < 0 || index >= _len){
            return null;
        }
        MJ_OC_Node<E> n = getNodeAtIdx(index);
        if (n == null){
            return null;
        }
        E e = n.getData();
        n.setData(newValue);
        return e;
    }
    
    @Override
    public int size() {
        return _len;
    }
    
    @Override
	public String toString() {
    	String s = "";
        for (int i = 0; i<_len; i++){
            s += this.get(i) + ", ";
        }
        s = s.substring(0, s.length()-2);
		return s;
	}

	private MJ_OC_Node<E> getNodeAtIdx(int idx) {
        if (_start == null) {
            return null;
        }
        int counter = 0;
        MJ_OC_Node<E> node = _start;
        
        while (counter != idx) {
            node = node.getNext();
            counter += 1;
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
