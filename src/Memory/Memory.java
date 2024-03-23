package Memory;

import CPU.Cache.Cache;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * The Memory class that represents the Memory in the Computer
 */
public class Memory{

    //The main hashmap object for the memory<mem location, value>
    HashMap<Integer, Integer> memory;
    Cache cache;

    //The int object to store the size of the memory, can be 2048 or 4096 in this project
    private int memorySize = 2048;

    Logger logger = Logger.getLogger("Memory");

    //Constructor method for the memory object
    public Memory(){
        memory = new HashMap<>();
        cache = new Cache();
    }

    //Expand the memory size to 4096
    public void expandSize(){
        memorySize = 4096;
    }
    // Resume the memory size back to 2048
    public void resumeSize(){
        memorySize = 2048;
    }


    /**
     * Helper method to turn octal String into a binary string
     *
     * @return octal String
     */
    public static String octalToBinary(String octalStr) {
        StringBuilder binaryStr = new StringBuilder();

        for (int i = 0; i < octalStr.length(); i++) {
            int octalDigit = Character.digit(octalStr.charAt(i), 8);
            String binaryGroup = Integer.toBinaryString(octalDigit);

            String paddedBinaryGroup = "0".repeat(3 - binaryGroup.length()) + binaryGroup;
            binaryStr.append(paddedBinaryGroup);
        }

        return binaryStr.toString();
    }
    /**
     * Set a memory address with a value, both parameter are in octal, they are Strings
     */
    public void octalSet(String octalAddress, String octalValue){
        String address = octalToBinary(octalAddress);
        String value = octalToBinary(octalValue);
        binarySet(address, value);
    }
    /**
     * Set a memory address with a value, both parameter are in binary, they are Strings
     */
    public void binarySet(String address, String value){
        int addressInt = Integer.valueOf(address, 2);
        int valueInt = Integer.valueOf(value, 2);
        set(addressInt, valueInt);
    }
    /**
     * Set a memory address with a value, both parameter are in decimal, they are Integers
     */
    public void set(int addressInt, int valueInt){
        if (addressInt < memorySize){
            ArrayList<Integer> currentLine = getCurrentLine(addressInt);
            memory.put(addressInt, cache.setValue(addressInt, valueInt, currentLine));
            logger.info("Memory " + Integer.toBinaryString(addressInt) + "(" + addressInt + ") " + "GOT the value " + Integer.toBinaryString(valueInt) + "(" + valueInt + ")");
            System.out.println("Memory " + Integer.toBinaryString(addressInt) + "(" + addressInt + ") " + "GOT the value " + Integer.toBinaryString(valueInt) + "(" + valueInt + ")");
        }else{
            logger.severe("FAILED!!! Invalid Memory.Memory address " + Integer.toBinaryString(addressInt) + "(" + addressInt + "). " + "The value " + Integer.toBinaryString(valueInt) + "(" + valueInt + ") was not ingested into the memory location");
            System.out.println("FAILED!!! Invalid Memory.Memory address " + Integer.toBinaryString(addressInt) + "(" + addressInt + "). " + "The value " + Integer.toBinaryString(valueInt) + "(" + valueInt + ") was not ingested into the memory location");
        }
    }
    /**
     * get a memory value with a address, address is in decimal and it is a int
     */
    public int get(int address){
        if(address < memorySize){
            ArrayList<Integer> currentLine = getCurrentLine(address);
            return cache.getValue(address, currentLine);
        }else{
            System.out.println("FAILED!!! Trying to get memory address out-of-bounds!  " + address + "(" + address + "). -1 returned");
            return -1;
        }
    }

    /**
     * get a memory value with a address, address is in binary and it is a String
     */
    public int get(String address){
        int addressInt = Integer.valueOf(address, 2);
        return get(addressInt);
    }
    /**
     * Debug/Logging method, print out all the memory values that are not 0
     */
    public void printAllMemoryValues(){
        for (Integer key: memory.keySet()){
            logger.info("ADDRESS: " + key + "(Octal -> " + Integer.toOctalString(key) + ")(Binary -> " + Integer.toBinaryString(key) + ")    :    Value: " + memory.get(key) + "(Octal -> " + Integer.toOctalString(memory.get(key)) + ")(Binary -> " + Integer.toBinaryString(memory.get(key)) + ")");
            System.out.println("ADDRESS: " + key + "(Octal -> " + Integer.toOctalString(key) + ")(Binary -> " + Integer.toBinaryString(key) + ")    :    Value: " + memory.get(key) + "(Octal -> " + Integer.toOctalString(memory.get(key)) + ")(Binary -> " + Integer.toBinaryString(memory.get(key)) + ")");
        }
        logger.info("Finished Printing all the values");
        System.out.println("Finished Printing all the values");
    }

    /**
     *  Get the current line of 8 values in the current operating address
     */
    public ArrayList<Integer> getCurrentLine(int addressInt){
        ArrayList<Integer> currentLine = new ArrayList<>();
        int i = addressInt - addressInt % 8;
        for(int j = 0; j < 8; j++){

            if(memory.containsKey(i)){
                currentLine.add(memory.get(i));
            }else{
                currentLine.add(0);
            }
            i++;
        }
        return currentLine;
    }

    public void updateCacheConsole(JTextArea cacheConsole){
        cache.updateCacheConsole(cacheConsole);
    }

    public void resetMem(){
        memory.clear();
        cache.resetCache();

    }




}