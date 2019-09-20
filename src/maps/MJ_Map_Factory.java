package maps;

/**
 *
 * @author Maisha
 * @param <K>
 * @param <V>
 */
public class MJ_Map_Factory <K,V> {
    
    public IMJ_Map <K,V> create(){
        //return new <K,V> MJ_Map_OC();
        return new MJ_Map_Nodes<K, V>();
    }
}
