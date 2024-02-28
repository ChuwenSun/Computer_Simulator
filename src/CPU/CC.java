package CPU;

/**
 * This is the Condition Code sub-register class, it will not extends Register.java because it has special behavior
 * In this program, CC will be treated as a register with 4 1-bit sub-registers
 */

public class CC{
    private boolean cc;

    public CC(boolean val) {cc = val;}

    public boolean get(){return this.cc;}

    public void set(boolean val){
        this.cc = val;
    }
}
