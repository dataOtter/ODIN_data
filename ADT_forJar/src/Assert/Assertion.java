package Assert;

/**
 *
 * @author Maisha Jauernig
 */
public class Assertion {
    
    /*public static void main(String[] args){
        //Assertion.test(1 == 2, "dummy");
        
        ExampleClass e1 = new ExampleClass();
        e1.f1(999);
        e1.f1(4);
        e1.f1(999);
    }*/
    
    public final static boolean ASSERTIONS_ON = true;
    
    public static void test(boolean b, String message){
        if (b == false){
            System.out.println(message);
            
            RuntimeException ex;
            ex = new RuntimeException();
            throw ex;
            
            //System.exit(0);
        }
    }
}
