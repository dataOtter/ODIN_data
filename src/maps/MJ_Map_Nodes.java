package maps;

import Assert.Assertion;
import maps.MJ_Map_EntryNode;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Maisha
 * @param <K>
 * @param <V>
 */
public class MJ_Map_Nodes <K,V> implements IMJ_Map <K,V> {
    private MJ_Map_EntryNode<K,V> _start;
    
    public MJ_Map_Nodes(){
        _start = null;
    }
    
    @Override
    public void clear(){
        _start = null;
    }
    
    @Override
    public boolean containsKey(Object key){
        MJ_Map_EntryNode<K,V> node = _start;
        
        for (int i = 0; i<size(); i++){
            if (node.getKey().equals(key)){
                return true;
            }
            node = node.getNext();
        }
        return false;
    }
    
    
    @Override
    public V get(Object key){
        Assertion.test(_start != null, "no data to retrieve");
        
        MJ_Map_EntryNode<K,V> node = _start;
        
        while (! node.getKey().equals(key)) {
                node = node.getNext();
            }
        return node.getValue();
    }

	@Override
	public K getKey(int idx) {
		MJ_Map_EntryNode<K,V> node = _start;
        
        for (int i = 0; i<size(); i++){
        	if (i == idx) {
        		return node.getKey();
        	}
            node = node.getNext();
        }
		return null;
	}
    
    @Override
    public boolean isEmpty(){
        return _start == null;
    }
    
    @Override
    public void print(){
        Assertion.test(_start != null, "Map is empty");
        for (MJ_Map_EntryNode<K,V> i = _start; i != null; i = i.getNext()){
            System.out.println(i.getKey() + ": " + i.getValue());
        }
        System.out.println("\n");
    }
    
    @Override
    public V put(K key, V val){
        V v = null;
        
        if (_start == null){
            _start = new MJ_Map_EntryNode<>(key, val);
            return null;
        }
        
        // if the key already exists and the value is a replacement 
        if (_start.getKey().equals(key)){
            v = _start.getValue();
            _start.setValue(val);
            return v;
        }
        
        MJ_Map_EntryNode<K,V> node = _start;
        while (node.hasNext()){
            if (node.getKey().equals(key)){
                v = node.getValue();
                node.setValue(val);
                return v;
            }
            node = node.getNext();
        }
        
        // if the key does not exist yet
        node.setNext(new MJ_Map_EntryNode<>(key, val));
        return v;
    }
    
    @Override
    public V replace(K key, V val){
        V v = null;
        
        if (_start != null){
            MJ_Map_EntryNode<K,V> node = _start;
            do{
                if (node.getKey().equals(key)){
                    v = node.getValue();
                    node.setValue(val);
                    return v;
                }
                node = node.getNext();
            }
            while (node.hasNext());
        }
        return v;
    }
    
    @Override
    public V remove(Object key){
        Assertion.test(_start != null, "no data to delete");
        V v;
        
        MJ_Map_EntryNode<K,V> nodeToDelete = _start;
        MJ_Map_EntryNode<K,V> nodeToLeft = null;
        
        while ( ! nodeToDelete.getKey().equals(key)){
                nodeToLeft = nodeToDelete;
                nodeToDelete = nodeToDelete.getNext();
                if (nodeToDelete == null) {
                    return null;
                }
            }
        
        v = nodeToDelete.getValue();

        if (_start == nodeToDelete){  // if trying to delete the first item
            if (_start.getNext() == null){  // and there is only one item, delete that
                _start = null;
            }
            else{  // and there is more than one item, set start to be the next item
                _start = _start.getNext();
            }
        }
        else{  // if trying to delete an item that is not the first item
            MJ_Map_EntryNode<K,V> nodeToRight = nodeToDelete.getNext();  // get the item after the one to delete
            if (nodeToRight != null){  // if there is an item after the one to delete,
                nodeToLeft.setNext(nodeToRight);  // point the previous item to that
            }
            else{  // otherwise point the previous item to null
                nodeToLeft.setNext(null);
            }
        }
        return v;
    }
    
    @Override
    public int size(){
        if (_start == null){
            return 0;
        }
        int counter = 0;
        for (MJ_Map_EntryNode<K,V> i = _start; i != null; i = i.getNext()){
            counter += 1;
        }
        return counter;
    }

    /*@Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
