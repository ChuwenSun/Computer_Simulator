package CPU;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the Instruction Decoder class. It is responsible to indentify opcode, R, IX, I, Address from a instruction.
 */

public class instructionDecoder {
    private int opcode, I, R, IX, address;
    private static Map<Integer, String> opcodeInstructMap = new HashMap<>();

    public instructionDecoder(){
        opcode = 0;
        R = 0;
        IX = 0;
        I = 0;
        address = 0;
        initializeMap();
    }



    /**
     * This is a helper method to ture a decimal integer into a 16-bits binary String
     *
     * @return a 16-bits binary String with possible leading zeros
     */
    private String toBinaryString16bits(int value){
//        String binaryString = Integer.toBinaryString(value);
//        int zerosNeeded = 16 - binaryString.length();
//
//        StringBuilder result = new StringBuilder();
//        for (int i = 0; i < zerosNeeded; i++) {
//            result.append("0");
//        }
//        result.append(binaryString);
//        return result.toString();
        String binaryString = Integer.toBinaryString(value & 0xFFFF);

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
        return IX;
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

    public String currentInstructString(){
        return opcodeInstructMap.get(opcode);
    }
    //easy for debug
    private void initializeMap() {
        opcodeInstructMap.put( 1,"LDR");
        opcodeInstructMap.put( 2,"STR");
        opcodeInstructMap.put( 3,"LDA");
        opcodeInstructMap.put(4,"LDX" );
        opcodeInstructMap.put(5,"STX" );
        opcodeInstructMap.put(36,"SETCCE");
        opcodeInstructMap.put(6,"JZ");
        opcodeInstructMap.put(7,"JNE");
        opcodeInstructMap.put(8,"JCC");
        opcodeInstructMap.put(9,"JMA");
        opcodeInstructMap.put(10,"JSR");
        opcodeInstructMap.put(11,"RFS");
        opcodeInstructMap.put(12,"SOB");
        opcodeInstructMap.put(13,"JGE");
        opcodeInstructMap.put(14,"AMR");
        opcodeInstructMap.put(15,"SMR");
        opcodeInstructMap.put(16,"AIR");
        opcodeInstructMap.put(17,"SIR");
        opcodeInstructMap.put(18,"MLT");
        opcodeInstructMap.put(19,"DVD");
        opcodeInstructMap.put(20,"TRR");
        opcodeInstructMap.put(21,"AND");
        opcodeInstructMap.put(22,"ORR");
        opcodeInstructMap.put(23,"NOT");
        opcodeInstructMap.put(24,"SRC");
        opcodeInstructMap.put(25,"RRC");
        opcodeInstructMap.put(26,"IN" );
        opcodeInstructMap.put(27,"OUT");

    }
}
