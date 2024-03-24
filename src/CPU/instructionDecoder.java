package CPU;

/**
 * This is the Instruction Decoder class. It is responsible to indentify opcode, R, IX, I, Address from a instruction.
 */

public class instructionDecoder {
    private int opcode, I, R, IX, address;

    public instructionDecoder(){
        opcode = 0;
        R = 0;
        IX = 0;
        I = 0;
        address = 0;
    }

    /**
     * This is a helper method to ture a decimal integer into a 16-bits binary String
     *
     * @return a 16-bits binary String with possible leading zeros
     */
    private String toBinaryString16bits(int value){
        String binaryString = Integer.toBinaryString(value);
        int zerosNeeded = 16 - binaryString.length();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < zerosNeeded; i++) {
            result.append("0");
        }
        result.append(binaryString);
        return result.toString();
    }

    /**
     * This will decode a instruction and identify opcode, R, IX, I, Address from decimalInstruct
     */
    public void decodeInstruction(int decimalInstruct){
        String binaryInstruct = toBinaryString16bits(decimalInstruct);
        decodeInstruction(binaryInstruct);
    }

    /**
     * This will decode a instruction and identify opcode, R, IX, I, Address from binaryInstruct
     */
    public void decodeInstruction(String binaryInstruct){
        opcode = Integer.valueOf(binaryInstruct.substring(0, 6), 2);
        R = Integer.valueOf(binaryInstruct.substring(6, 8), 2);
        IX = Integer.valueOf(binaryInstruct.substring(8, 10), 2);
        I = Integer.valueOf(binaryInstruct.substring(10, 11), 2);
        address = Integer.valueOf(binaryInstruct.substring(11, 16), 2);

        System.out.println("Instruction Decoded: " + opcode + "   GPR: " + R + "   IX: " + IX + "   I:  " + I + "   Address:  " + address);
    }

    public int getOpcode() {
        return opcode;
    }

    public int getI() {
        return I;
    }

    public int getR() {
        return R;
    }

    public int getIX() {
        return IX;
    }

    public int getAddress() {
        return address;
    }

    public int getRx(boolean ZeroOrTwo){
        if(ZeroOrTwo && R != 0 && R != 2){
            System.out.println("Error!!!! rx must be 0 or 2!!!");
            return -1;
        }
        return R;
    }

    public int getRy(boolean ZeroOrTwo){
        if(ZeroOrTwo && IX != 0 && IX != 2){
            System.out.println("Error!!!! ry must be 0 or 2!!!");
            return -1;
        }
        return R;
    }
    public String toBinaryStringWithLeadingZeros(int decimalValue, int length) {
        String binaryString = Integer.toBinaryString(decimalValue);
        int zerosNeeded = length - binaryString.length();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < zerosNeeded; i++) {
            result.append("0");
        }
        result.append(binaryString);
        return result.toString();
    }
    public boolean getAorL(){
        String IXBinaryString = toBinaryStringWithLeadingZeros(IX, 2);
        return IXBinaryString.charAt(0) == '1';
    }

    public boolean getLorR(){
        String IXBinaryString = toBinaryStringWithLeadingZeros(IX, 2);
        return IXBinaryString.charAt(1) == '1';
    }

    public int getCount(){
        String addressBinaryString = toBinaryStringWithLeadingZeros(address, 5);
        String countBinaryString = addressBinaryString.substring(1,5);
        return Integer.valueOf(countBinaryString, 2);
    }


    public void resetDecoder(){
        opcode = 0;
        R = 0;
        IX = 0;
        I = 0;
        address = 0;
    }
}
