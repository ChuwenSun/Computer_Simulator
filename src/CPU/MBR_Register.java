package CPU;
/**
 * This is the Memory Buffer Register, MBR object's behaviors are defined here
 */
public class MBR_Register extends Register{

    public MBR_Register(int val) {
        super("MBR", val, 16);
    }

    @Override
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
}
