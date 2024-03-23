package Simulator;

import Bus.Bus;
import CPU.CPU_Registers;
import Memory.Memory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * The Simulator class represents the Computer itself and it will have:
 * a Memory Object
 * a CPU Object
 * A Bus
 */
public class Simulator {
    public Memory memory;
    public CPU_Registers CPU;
    public Bus bus;
    /**
     * The Constructor Method
     */
    public Simulator(){
        memory = new Memory();
        CPU = new CPU_Registers();
        bus = new Bus(CPU, memory);
    }
    /**
     * Load the memory with the input file
     */
    public void loadMemory(File iplFile) {
        try {
            Scanner scanner = new Scanner(iplFile);
            // read every line until it reaches the end
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // divide the line into two parts by the spaces in the middle
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    //get String 1 and String 2, which are the memory address and the value
                    String string1 = parts[0];
                    String string2 = parts[1];
                    //set the value in the memory object
                    memory.octalSet(string1, string2);
//                    System.out.println("String 1: " + string1 + ", String 2: " + string2);
                }
            }
//            memory.printAllMemoryValues();
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void refreshSimulator(){
        System.out.println("IM DONE!!!");
        memory = new Memory();
        CPU = new CPU_Registers();
        bus = new Bus(CPU, memory);
    }
}
