package maps;

import Assert.Assertion;

/**
 *
 * @author Maisha
 * @param <K>
 * @param <V>
 */
public class MJ_Map2 <K,V> implements IMJ_Map <K,V> {
    private MJ_Node<K,V> _start;
    
    public <K,V> MJ_Map2(){}
    
    
    @Override
    public boolean contains(K key){
        MJ_Node<K,V> node = _start;
        
        for (int i = 0; i<length(); i++){
            if (node.getKey().equals(key)){
                return true;
            }
            node = node.getNext();
        }
        return false;
    }
    
    
    @Override
    public V getValueOfKey(K key){
        Assertion.test(_start != null, "no data to retrieve");
        
        MJ_Node<K,V> node = _start;
        
        while (! node.getKey().equals(key)) {
                node = node.getNext();
            }
        return node.getValue();
    }
    
    
    @Override
    public void add(K key, V val){
        MJ_Node<K,V> newNode = new MJ_Node<>(key, val);
        MJ_Node<K,V> node = _start;
        
        if (_start == null){
            _start = new MJ_Node<>(key, val);
        }
        
        else{
            while (node.getNext() != null){
                node = node.getNext();
            }
            node.setNext(newNode);
        }
    }
    
    @Override
    public void setValueOfKey(K key, V val){
        Assertion.test(_start != null, "no data to retrieve");
        
        MJ_Node<K,V> node = _start;
        
        while (! node.getKey().equals(key)) {
                node = node.getNext();
            }
        node.setValue(val);
    }
    
    @Override
    public boolean delete(K key){
        Assertion.test(_start != null, "no data to delete");
        boolean success;
        
        MJ_Node<K,V> nodeToDelete = _start;
        MJ_Node<K,V> prevNode = null;
        
        while ( ! nodeToDelete.getKey().equals(key)){
                prevNode = nodeToDelete;
                nodeToDelete = nodeToDelete.getNext();
                if (nodeToDelete == null) {
                    return false;
                }
            }

        if (_start == nodeToDelete){  // if trying to delete the first item
            if (_start.getNext() == null){  // and there is only one item, delete that
                _start = null;
            }
            else{  // and there is more than one item, set start to be the next item
                _start = _start.getNext();
            }
            success = true;
        }
        else{  // if trying to delete an item that is not the first item
            MJ_Node<K,V> newNext = nodeToDelete.getNext();  // get the item after the one to delete
            if (newNext != null){  // if there is an item after the one to delete,
                prevNode.setNext(newNext);  // point the previous item to that
            }
            else{  // otherwise point the previous item to null
                prevNode.setNext(null);
            }
            success = true;
        }
        return success;
    }
    
    
    private MJ_Node<K,V> getNodeAtIdx(int idx){
        Assertion.test(_start != null, "No data to retrieve");
        
        int counter = 0;
        MJ_Node<K,V> node = _start;
        
        while (counter != idx){
            node = node.getNext();
            counter += 1;
            Assertion.test(node != null, "index out of range, node not found");
        }
        return node;
    }
    
    
    @Override
    public K getKeyAtIdx(int i){
        MJ_Node<K,V> node = getNodeAtIdx(i);
        return node.getKey();
    }
    
    @Override
    public void clear(){
        _start = null;
    }
    
    @Override
    public int length(){
        if (_start == null){
            return 0;
        }
        int counter = 0;
        for (MJ_Node<K,V> i = _start; i != null; i = i.getNext()){
            counter += 1;
        }
        return counter;
    }
    
    @Override
    public void print(){
        Assertion.test(_start != null, "Map is empty");
        for (MJ_Node<K,V> i = _start; i != null; i = i.getNext()){
            System.out.println(i.getKey() + ": " + i.getValue());
        }
        System.out.println("\n");
    }
}
