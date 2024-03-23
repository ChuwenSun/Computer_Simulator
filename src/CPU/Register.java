package CPU;
/**
 * This is the Register class, and it acts as a parent class for other specific registers.
 *
 */
import java.util.logging.Logger;

public class Register {

    private String name;
    private int value;
    private int length;
    private Logger logger = Logger.getLogger("Register Logger");


    /**
     * This is the Constructor Method
     */
    public Register(String name, int value, int length) {
        this.name = name;
        this.length = length;

    }

    /**
     * Front end method to show the binary value of this register with possible leading zeros
     *
     * @return a binary String with leading Zeros based on the length requirement of this Register
     */
    public String toBinaryStringWithLeadingZeros() {
        String binaryString = Integer.toBinaryString(value);
        int zerosNeeded = length - binaryString.length();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < zerosNeeded; i++) {
            result.append("0");
        }
        result.append(binaryString);

        return result.toString();
    }
    public int getValue(){ return value;}

    /**
     * Set Method based on a binaryString
     */
    public void setValue(String BinaryString){
        if (BinaryString.length() <= length){
            value = Integer.parseInt(BinaryString,2);
            logger.info(name + " GOT value: " + BinaryString + "(" + value + ")");
            System.out.println(name + " GOT value: " + BinaryString + "(" + value + ")");
        }else{
            logger.info("ERROR: No value was sent into " + name + "!!! " + BinaryString + "(" + Integer.parseInt(BinaryString,2) + ") is out of bounds.");
            System.out.println("ERROR: No value was sent into " + name + "!!! " + BinaryString + "(" + Integer.parseInt(BinaryString,2) + ") is out of bounds.");
        }
    }
    /**
     * Set Method based on a decimal integer val
     */
    public void setValue(int val){
        if(Math.pow(2, length) > val){
            this.value = val;
            System.out.println(name + " GOT value: " + Integer.toBinaryString(val) + "(" + value + ")");
        }else {
            System.out.println("ERROR: No value was sent into " + name + "!!! " + Integer.toBinaryString(val) + "(" + val + ") is out of bounds.");
        }
    }

    public void resetRegister(){
        value = 0;
    }
}
