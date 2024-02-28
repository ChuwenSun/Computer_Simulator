package CPU;

/**
 * This is the CPU class that is representing the CPU of the Computer Simulator,
 * it contains all the register objects here, and one Instruction Decoder
 *
 */


public class CPU_Registers {
    public GPR R0, R1, R2, R3;
    public IX_Register X1, X2, X3;
    public CC cc1, cc2, cc3, cc4;
    public IR_Register IR;
    public MAR_Register MAR;
    public MBR_Register MBR;
    public MFR_Register MFR;
    public Program_Counter PC;
    public instructionDecoder decoder;

    /**
     * Constructor method: initialize all the registers
     */
    public CPU_Registers(){init_registers();}

    public void init_registers(){
        cc1 = new CC(false);
        cc2 = new CC(false);
        cc3 = new CC(false);
        cc4 = new CC(false);
        R1 = new GPR("R0",0);
        R0 = new GPR("R1",0);
        R2 = new GPR("R2",0);
        R3 = new GPR("R3",0);

        X1 = new IX_Register("X1",0);
        X2 = new IX_Register("X2",0);
        X3 = new IX_Register("X3",0);

        IR = new IR_Register(0);
        MAR = new MAR_Register(0);
        MBR = new MBR_Register(0);
        MFR = new MFR_Register(0);
        PC = new Program_Counter(0);
        decoder = new instructionDecoder();
    }

    /**
     * Find the corresponding IX register based on caller asking
     *
     * @return The IX register based on parameter integer.(if (i == 1){ return X1;})
     */
    public IX_Register getIX(int i) {
        return switch (i) {
            case 1 -> X1;
            case 2 -> X2;
            case 3 -> X3;
            default -> {
                System.out.println("Invalid range of IX, 0 returned");
                yield new IX_Register("Invalid range of IX", 0);
            }
        };

    }
    /**
     * Find the corresponding GPR register based on caller asking
     *
     * @return The GPR register based on parameter integer.(if (i == 0){ return R0;})
     */
    public GPR getR(int i) {
        return switch (i) {
            case 0 -> R0;
            case 1 -> R1;
            case 2 -> R2;
            case 3 -> R3;
            default -> {
                System.out.println("Invalid range of R, 0 returned");
                yield new GPR("Invalid range of R", 0);
            }
        };

    }
}
