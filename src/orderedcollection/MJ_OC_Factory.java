package orderedcollection;

/**
 *
 * @author Maisha Jauernig
 * @param <T>
 */
public class MJ_OC_Factory <T> {
    
    public IMJ_OC<T> create(){
        //return new MJ_OC_Array<>(5);
        return new MJ_OC_List<>();
    }
}
