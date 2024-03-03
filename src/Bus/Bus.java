package Bus;

import CPU.CPU_Registers;
import CPU.instructionDecoder;
import Memory.Memory;
/**
 * The Bus Class that is responsible for Run(), Step() and Halt().
 */
public class Bus {
    private CPU_Registers CPU;
    private Memory memory;
    private boolean isHalt;
    public Bus(CPU_Registers CPU, Memory memory){
        this.CPU = CPU;
        this.memory = memory;
        this.isHalt = false;
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
            default:
                break;
        }





    }


}
