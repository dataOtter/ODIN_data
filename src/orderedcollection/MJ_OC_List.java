package orderedcollection;

import Assert.Assertion;

/**
 *
 * @author Maisha Jauernig
 * @param <T>
 */
public class MJ_OC_List <T> implements IMJ_OC <T>{
    private MJ_Node<T> _start;
    
    public MJ_OC_List(){}
    
    @Override
    public int length(){
        if (_start == null){
            return 0;
        }
        int counter = 0;
        for (MJ_Node<T> i = _start; i != null; i = i.getNext()){
            counter += 1;
        }
        return counter;
    }
    
    @Override
    public void prepend(T s){
        if (_start == null){
            _start = new MJ_Node<>(s);
        }
        else{
            MJ_Node<T> newNode = new MJ_Node<>(s);
            newNode.setNext(_start);
            _start = newNode;
        }
    }
    
    @Override
    public T getItem(int index){
        //Assertion.test(index <= length(), "Index out of range");
        MJ_Node<T> n = getNodeAtIdx(index);
        if (n != null){
            return (T) n.getData();
        }
        return null;
    }
    
    @Override
    public boolean contains(T value){
        MJ_Node<T> node = _start;
        
        while (node != null){
            if (node.getData() == value){
                return true;
            }
            node = node.getNext();
        }
        return false;
    }
    
    @Override
    public void append(T s){
        MJ_Node<T> newNode = new MJ_Node<>(s);
        MJ_Node<T> node = _start;
        
        if (_start == null){
            _start = new MJ_Node<>(s);
        }
        
        else{
            while (node.getNext() != null){
                node = node.getNext();
            }
            node.setNext(newNode);
        }
    }
    
    @Override
    public void deleteItem(int index){
        Assertion.test(index < length(), "Index out of range");
        
        if (_start == null){  // nothing to delete
            return;
        }
        if (index == 0){  // if trying to delete the first item
            if (_start.getNext() == null){  // and there is only one item, delete that
                _start = null;
            }
            else{  // and there is more than one item, set start to be the next item
                _start = _start.getNext();
            }
        }
        else{  // if trying to delete an item that is not the first item
            MJ_Node<T> node = getNodeAtIdx(index-1);  // get the item before the one to delete
            if (node == null){
                return;
            }
            MJ_Node<T> toDelete = node.getNext();
            if (toDelete == null){
                return;
            }
            MJ_Node<T> newNext = toDelete.getNext();  // get the item after the one to delete
            node.setNext(newNext);
        }
    }
    
    @Override
    public void setValue(int index, T newValue){
        MJ_Node<T> n = getNodeAtIdx(index);
        if (n != null){
            n.setData(newValue);
        }
    }
    
    private MJ_Node<T> getNodeAtIdx(int idx){
        if (_start == null){
            return null;
        }
        
        int counter = 0;
        MJ_Node<T> node = _start;
        
        while (counter != idx){
            node = node.getNext();
            counter += 1;
            if (node == null){
                return null;
            }
        }
        return node;
    }
    
    
    @Override
    public void insert(int index, T value){
        int len = length();
        //Assertion.test(index > len, "Index out of range");
        
        if (index == len){
            append(value);
        }
        else if (index == 0){
            prepend(value);
        }
        else{
            MJ_Node<T> n = new MJ_Node<>(value);
            MJ_Node<T> nRight = getNodeAtIdx(index);
            MJ_Node<T> nLeft = getNodeAtIdx(index-1);
            n.setNext(nRight);
            nLeft.setNext(n);
        }
    }
    
    
    @Override
    public void printAll(){
        Assertion.test(_start != null, "Collection is empty");
        for (MJ_Node<T> i = _start; i != null; i = i.getNext()){
            System.out.println(i.getData());
        }
        System.out.println("\n");
    }
    
    @Override
    public IMJ_OC <T> getDeepCopy(){
        IMJ_OC <T> stor = new MJ_OC_List();
        for (int i = 0; i<this.length(); i++){
            stor.append(this.getItem(i));
        }
        return stor;
    }
}
