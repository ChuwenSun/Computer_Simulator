package CPU;
/**
 * This is the Memory Address Register, MAR object's behaviors are defined here
 */
public class MAR_Register extends Register{

    public MAR_Register(int val) {
        super("MAR", val, 12);
    }

    @Override
    public void setValue(String BinaryString){
        if (BinaryString.length() <= length){
            value = Integer.parseInt(BinaryString,2);
            System.out.println(name + " GOT value: " + BinaryString + "(" + value + ")");
        }else{
            System.out.println("ERROR: No value was sent into " + name + "!!! " + BinaryString + "(" + Integer.parseInt(BinaryString,2) + ") is out of bounds.");
        }
    }
}
