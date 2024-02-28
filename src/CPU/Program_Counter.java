package CPU;
/**
 * This is the Program_Counter Register, PC object's behaviors are defined here
 */
public class Program_Counter extends Register{

    public Program_Counter(int val) {
        super("PC", val, 12);
    }

    /**
     * Increment the value in PC by 1
     */
    public void stepForward(){
        int val = getValue() + 1;
        this.setValue(val);
    }
}
