package CPU;
/**
 * This is the Register class, and it acts as a parent class for other specific registers.
 *
 */
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Register {

    public String name;
    public int value;
    public int length;
    public Logger logger = Logger.getLogger("Register Logger");


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
//        String binaryString = Integer.toBinaryString(value);
//        int zerosNeeded = length - binaryString.length();
//
//        StringBuilder result = new StringBuilder();
//        for (int i = 0; i < zerosNeeded; i++) {
//            result.append("0");
//        }
//        result.append(binaryString);
//
//        return result.toString();
        final String binaryString;
        if (value < 0) {
            binaryString = Integer.toBinaryString((1 << length) + value);
        } else {
            binaryString = Integer.toBinaryString(value);
        }

        int zerosNeeded = length - binaryString.length();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < zerosNeeded; i++) {
            result.append("0");
        }
        result.append(binaryString);
        String output = result.length() > length ? result.substring(result.length() - length) : result.toString();
//        System.out.println("returning " + name + " binarystring back to front: " + output);
        return output;
    }
    public int getValue(){ return value;}

    /**
     * Set Method based on a binaryString(2's complement)
     */
    public void setValue(String BinaryString){
        if (BinaryString.length() <= length) {
            boolean isNegative = BinaryString.charAt(0) == '1';
            if (isNegative && BinaryString.length() == length) {
                String invertedBits = BinaryString.chars()
                        .mapToObj(c -> c == '0' ? "1" : "0")
                        .collect(Collectors.joining());

                int decimalValue = Integer.parseInt(invertedBits, 2) + 1;

                value = -decimalValue;
            } else {
                value = Integer.parseInt(BinaryString, 2);
            }

            logger.info(name + " GOT value: " + BinaryString + "(" + value + ")");
            System.out.println(name + " GOT value: " + BinaryString + "(" + value + ")");
        } else {
            logger.info("ERROR: No value was sent into " + name + "!!! " + BinaryString + "(" + Integer.parseInt(BinaryString,2) + ") is out of bounds.");
            System.out.println("ERROR: No value was sent into " + name + "!!! " + BinaryString + "(" + Integer.parseInt(BinaryString,2) + ") is out of bounds.");
        }
    }
    /**
     * Set Method based on a decimal integer val
     */
    public void setValue(int val){
        int minValue = -1 << (length - 1);
        int maxValue = (1 << (length - 1)) - 1;

        if (val >= minValue && val <= maxValue) {
            this.value = val;
            String binaryRepresentation = val >= 0 ?
                    String.format("%" + length + "s", Integer.toBinaryString(val)).replace(' ', '0') :
                    Integer.toBinaryString(val).substring(32 - length);
            System.out.println(name + " GOT value: " + binaryRepresentation + "(" + val + ")");
        } else {
            System.out.println("ERROR: No value was sent into " + name + "!!! " + Integer.toBinaryString(val) + "(" + val + ") is out of bounds.");
        }
    }

    public void resetRegister(){
        value = 0;
    }
}
