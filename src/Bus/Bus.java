package Bus;

import CPU.CPU_Registers;
import CPU.instructionDecoder;
import GUI.IO_Devices;
import Memory.Memory;

import javax.swing.*;

/**
 * The Bus Class that is responsible for Run(), Step() and Halt().
 */
public class Bus {
    private CPU_Registers CPU;
    private Memory memory;
    private boolean isHalt;

    private IO_Devices keyboardIO = new IO_Devices("Keyboard");
    private JTextArea outputConsole;
    public Bus(CPU_Registers CPU, Memory memory){
        this.CPU = CPU;
        this.memory = memory;
        this.isHalt = false;
    }

    public void setOutputConsole(JTextArea outputConsole){
        this.outputConsole = outputConsole;
    }


    /**
     * Run step() until a HALT is met. (Clicking the Run button will bring the program here)
     *
     */
    public void run(){
        while(!isHalt){
            step();
        }
        isHalt = false;
    }

    /**
     * set isHalt boolean value to true so the program will stop running. (Clicking the Halt button will bring the program here)
     *
     */
    public void halt(){
        isHalt = true;
//        CPU.PC.setValue(0);
    }
    /**
     * Run current instruction based on where PC(Program Counter Register) is.
     * Increment PC
     * (Clicking the Step button will bring the program here)
     */
    public void step(){
        // Get PC value and pass it into MAR
        CPU.MAR.setValue(CPU.PC.getValue());
        // Increment PC
        CPU.PC.stepForward();
        // Get memory[MAR] value and pass it into MBR, this is usually the instruction 16 bits string
        CPU.MBR.setValue(memory.get(CPU.MAR.getValue()));
        // Pass MBR value(the instruction 16 bits string) into IR
        CPU.IR.setValue(CPU.MBR.getValue());
        // Call the InstructionDecoder to decode the instruction. The decoder will get enough information to run an instruction after this line
        CPU.decoder.decodeInstruction(CPU.IR.getValue());
        // Execute the Load&Store instruction(modifying R or IX registers)
        executeInstruction(CPU.decoder);
    }
    /**
     * Execute the current instruction(info has been stored in decoder)
     * 1. firstly calculate EA using decoder
     * 2. Switch statement to do specific instruction based on opcode(We only have Load and Store in this part of project)
     */
    private void executeInstruction(instructionDecoder decoder) {
        int ea = decoder.getAddress();
        // I == 0, direct addressing
        if (decoder.getI() == 0){
            //get the value from corresponding IX register and modify EA
            if(decoder.getIX() > 0){
                ea += CPU.getIX(decoder.getIX()).getValue();
            }
            //I == 1, indirect addressing
        }else if(decoder.getI() == 1){
            //IX == 0, get value from the memory(EA)
            if(decoder.getIX() == 0){
                ea = memory.get(ea);
            //IX != 0, modify EA by getting value from memory(EA)
            }else{
                ea = memory.get(ea + CPU.getIX(decoder.getIX()).getValue());
            }
        }else{
            System.out.println("I is not 0/1, invalid I value");
        }

        //Load and Store instuctions actual execution phase based on opcode
        switch (decoder.getOpcode()){
            case 0: {
                //Run Halt
                halt();
                break;
            }
            //OpCode 01, LDR, r <- c(EA)
            case 1: {
                CPU.getR(decoder.getR()).setValue(memory.get(ea));
                break;
            }
            //Opcode 02, STR, Memory(EA) <- c(r)
            case 2: {
                memory.set(ea, CPU.getR(decoder.getR()).getValue());
                break;
            }
            //Opcode 03, LDA, r <- EA
            case 3: {
                CPU.getR(decoder.getR()).setValue(ea);
                break;
            }
            //Opcode 04, LDX, Xx <- c(EA)
            case 4: {
                CPU.getIX(decoder.getIX()).setValue(memory.get(ea));
                break;
            }
            //Opcode 05, STX, Memory(EA) <- c(Xx)
            case 5: {
                memory.set(ea, CPU.getIX(decoder.getIX()).getValue());
                break;
            }
            //Opcode 44(36 in decimal), SETCCE If c(r) = 0, the E bit of the condition code is set to 1, else the E bit of the condition code is set to 0(E bit is CC3)
            case 36: {
                if(CPU.getR(decoder.getR()).getValue() == 0){
                    CPU.getCC(3).set(true);
                }else{
                    CPU.getCC(3).set(false);
                }
                break;
            }
            //Opcode 06, JZ, Jump If Zero:
            //If the E bit of CC is 1, then PC <- EA
            //Else PC <- PC+1
            case 6: {
                if (CPU.getCC(3).get()) {
                    CPU.PC.setValue(ea);
                } else {
                    //CPU.PC.stepForward();
                    //No need to increment PC, PC will get incremented in step()
                }
                break;
            }
            //JNE
            case 7: {
                if (!CPU.getCC(3).get()){
                    CPU.PC.setValue(ea);
                }else{

                }
                break;
            }
            //Opcode 10(8 in decimal) JCC
            // JCC cc, x, address[,I],
            // Jump If Condition Code
            //cc replaces r for this instruction
            //cc takes values 0, 1, 2, 3 as above and specifies the bit in the Condition Code Register to check;
            //If cc bit  = 1, PC  EA
            //Else PC <- PC + 1
            case 8: {
                if (CPU.getCC(decoder.getR()).get()){
                    CPU.PC.setValue(ea);
                }else{

                }
                break;
            }
            //Opcode 11(9 in decimal) JMA, unconditional jump
            case 9:{
                CPU.PC.setValue(ea);
                break;
            }
            //Opcode 12(10 in decimal) JSR, PC already incremented,
//            R3 <- PC+1;
//            PC <- EA
            case 10:{
                CPU.getR(3).setValue(CPU.PC.getValue());
                CPU.PC.setValue(ea);
                break;
            }
            //Opcode 13(11 in decimal), RFS Immed
            //R0 <- Immed; PC <- c(R3)
            case 11:{
                CPU.getR(0).setValue(decoder.getAddress());
                CPU.PC.setValue(CPU.getR(3).getValue());
                break;
            }
            //Opcode 14(12 in decimal), SOB
            case 12:{
                CPU.getR(decoder.getR()).setValue(CPU.getR(decoder.getR()).getValue() - 1);

                if (CPU.getR(decoder.getR()).getValue() > 0){
                    CPU.PC.setValue(ea);
                }else{

                }
                break;
            }
            //Opcode 15(13 in decimal), JGE
            case 13:{
                if (CPU.getR(decoder.getR()).getValue() >= 0){
                    CPU.PC.setValue(ea);
                }else{

                }
                break;
            }
            //Opcode 16(14 in decimal), AMR
            case 14:{
                int result = CPU.getR(decoder.getR()).getValue() + memory.get(ea);
//                if (result > ), Java Integer will overflow first, so no need to set OVERFLOW flag here
                CPU.getR(decoder.getR()).setValue(result);
                break;
            }
            //Opcode 17(15 in decimal), SMR
            case 15:{
                int result = CPU.getR(decoder.getR()).getValue() - memory.get(ea);
                if (result < 0){
                    //UNDERFLOW
                    CPU.getCC(1).set(true);
                }else {
                    CPU.getR(decoder.getR()).setValue(result);
                }
                break;
            }
            //Opcode 20(16 in decimal), AIR
            case 16:{
                CPU.getR(decoder.getR()).setValue(CPU.getR(decoder.getR()).getValue() + decoder.getAddress());
                break;
            }
            //Opcode 21(17 in decimal), SIR
            case 17:{
                int result = CPU.getR(decoder.getR()).getValue() - decoder.getAddress();
                if (result < 0){
                    //UNDERFLOW
                    CPU.getCC(1).set(true);
                }else{
                    CPU.getR(decoder.getR()).setValue(result);
                }
                break;
            }
            //Opcode 22(18 in decimal), MLT
            case 18: {
                // Perform the multiplication using long to avoid overflow within the operation itself
                long result = (long) CPU.getR(decoder.getRx(true)).getValue() * CPU.getR(decoder.getRy(true)).getValue();

                // Convert the result to a binary string with leading zeros to ensure it's properly formatted
                 // Use 64 bits to ensure no data loss

                // Check if the binary string length exceeds 32 bits for the actual result
                if (result > 0xFFFFFFFFL) {
                    CPU.getCC(0).set(true);
                } else {
                    String fullBinaryStr = toBinaryStringWithLeadingZeros(result, 32);
                    // Split the 32-bit result into two 16-bit parts and set them into the registers
                    CPU.getR(decoder.getRx(true)).setValue(Integer.parseInt(fullBinaryStr.substring(0, 16), 2));
                    CPU.getR(decoder.getRx(true) + 1).setValue(Integer.parseInt(fullBinaryStr.substring(16, 32), 2));
                }
                break;
            }
            //Opcode 23(19 in decimal), DVD
            case 19: {
                if (CPU.getR(decoder.getRy(true)).getValue() == 0){
                    CPU.getCC(2).set(true);
                }else {
                    int quotient = CPU.getR(decoder.getRx(true)).getValue() / CPU.getR(decoder.getRy(true)).getValue();
                    int reminder = CPU.getR(decoder.getRx(true)).getValue() % CPU.getR(decoder.getRy(true)).getValue();
                    CPU.getR(decoder.getRx(true)).setValue(quotient);
                    CPU.getR(decoder.getRx(true) + 1).setValue(reminder);
                }
                break;
            }
            //Opcode 24(20 in decimal), TRR
            case 20: {
                if (CPU.getR(decoder.getRx(false)).getValue() == CPU.getR(decoder.getRy(false)).getValue()){
                    CPU.getCC(3).set(true);
                }else{
                    CPU.getCC(3).set(false);
                }
                break;
            }
            //Opcode 25(21 in decimal), AND
            case 21: {
                int result = CPU.getR(decoder.getRx(false)).getValue() & CPU.getR(decoder.getRy(false)).getValue();
                CPU.getR(decoder.getRx(false)).setValue(result);
                break;
            }
            //Opcode 26(22 in decimal), ORR
            case 22: {
                int result = CPU.getR(decoder.getRx(false)).getValue() | CPU.getR(decoder.getRy(false)).getValue();
                CPU.getR(decoder.getRx(false)).setValue(result);
                break;
            }
            //Opcode 27(23 in decimal), NOT
            case 23: {
                int result = ~CPU.getR(decoder.getRx(false)).getValue();
                CPU.getR(decoder.getRx(false)).setValue(result);
                break;
            }
            //Opcode 30(24 in decimal), SRC
            case 24:{
                int value = CPU.getR(decoder.getR()).getValue();
                int count = decoder.getCount();
                if(!decoder.getAorL()){

                    if (decoder.getLorR()){
                        //Arithmetically left shift
                        String binaryStr = toBinaryStringWithLeadingZeros(value, 16);
                        String signBit = binaryStr.substring(0, 1);
                        binaryStr = binaryStr.substring(1,16);
                        for(int i = 0; i < count; i++){
                            binaryStr = binaryStr + "0";
                        }
                        value = Integer.valueOf(signBit + binaryStr.substring(count), 2);
                    }else if(!decoder.getLorR()){
                        //Arithmetically right shift
                        String binaryStr = toBinaryStringWithLeadingZeros(value, 16);
                        String signBit = binaryStr.substring(0, 1);
                        binaryStr = binaryStr.substring(1,16);
                        for(int i = 0; i < count; i++){
                            binaryStr = "0" + binaryStr;
                        }
                        value = Integer.valueOf(signBit + binaryStr.substring(0, 15), 2);
                    }

                }else if(decoder.getAorL()){
                    if(decoder.getLorR()){
                        //Logically left shift
                        String binaryStr = toBinaryStringWithLeadingZeros(value, 16);
                        for(int i = 0; i < count; i++){
                            binaryStr = binaryStr + "0";
                        }
                        value = Integer.valueOf(binaryStr.substring(count),2);
                    }else if(!decoder.getLorR()){
                        //Logically right shift
                        String binaryStr = toBinaryStringWithLeadingZeros(value, 16);
                        for(int i = 0; i < count; i++){
                            binaryStr = "0" + binaryStr;
                        }
                        value = Integer.valueOf(binaryStr.substring(0, 16),2);
                    }
                }
                CPU.getR(decoder.getR()).setValue(value);
                break;
            }
            //Opcode 31(25 in decimal), RRC
            case 25:{
                int value = CPU.getR(decoder.getR()).getValue();
                int count = decoder.getCount();
                String binaryStr = toBinaryStringWithLeadingZeros(value, 16);


                for(int i = 0; i < count; i++){
                    if(decoder.getLorR()){
                        // Left rotate
                        binaryStr = binaryStr.substring(1) + binaryStr.charAt(0);
                    }else{
                        // Right rotate
                        binaryStr = binaryStr.charAt(15) + binaryStr.substring(0, 15);
                    }
                }
                value = Integer.parseInt(binaryStr, 2);
                CPU.getR(decoder.getR()).setValue(value);
                break;
            }
            //Opcode 32(26 in decimal), IN
            case 26:{
                keyboardIO.askForInput("Please input a value: ");
                String input = keyboardIO.getValue();
                int decimalValue = Integer.parseInt(input);
                CPU.getR(decoder.getR()).setValue(decimalValue);
                break;
            }
            //Opcode 33(27 in decimal), OUT
            case 27:{
                int value = CPU.getR(decoder.getR()).getValue();
                outputConsole.append("OUT: " + Integer.toString(value) + "\n");
                break;
            }
            default:
                break;
        }





    }
    public String toBinaryStringWithLeadingZeros(long value, int length) {
        String binaryString = Long.toBinaryString(value);
        int zerosNeeded = length - binaryString.length();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < zerosNeeded; i++) {
            result.append("0");
        }
        result.append(binaryString);

        return result.toString();
    }
    public void resetBus(){
        isHalt = false;
    }

}
