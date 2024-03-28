package CPU;
/**
 * This is the Machine Fault Register, MFR object's behaviors are defined here
 */
public class MFR_Register extends Register{

    public MFR_Register(int val) {
        super("MFR", val, 4);
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
