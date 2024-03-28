package CPU.Cache;

import Memory.Memory;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the Cache class that is representing the Cache of the Computer Simulator,
 * it will help memory manipulation.
 *
 */
public class Cache {
    public FixedSizeFIFOCache<String, ArrayList<Integer>> cacheBlocks;
    final int sizeCache = 16;
    final int sizeLine = 8;
    public Cache(){
        //the key Integer in this hashmap is the tag: key % 8 == 0
        this.cacheBlocks = new FixedSizeFIFOCache<>(16);
        for(int i = 0; i < sizeCache; i++){

            ArrayList<Integer> line = new ArrayList<>();
            for(int j = 0; j < sizeLine; j++){
                line.add(0);
            }
            this.cacheBlocks.put(String.valueOf(-i-1), line);

        }
    }


    public int setValue(int memoryAddress, int value, ArrayList<Integer> currentLine){
        String tag = getTag(memoryAddress);
        if (cacheBlocks.containsKey(tag)){
            cacheBlocks.get(tag).set(memoryAddress % 8, value);
        }else{
            currentLine.set(memoryAddress % 8, value);
            cacheBlocks.put(tag, currentLine);
        }
        return value;
    }

    public int getValue(int memoryAddress, ArrayList<Integer> currentLine){
        String tag = getTag(memoryAddress);
        int output = 0;
        if (cacheBlocks.containsKey(tag)){
            output = cacheBlocks.get(tag).get(memoryAddress % 8);
        }else{
            cacheBlocks.put(tag, currentLine);
            output = cacheBlocks.get(tag).get(memoryAddress % 8);
        }
        return output;
    }
    public boolean exist(int memoryAddress){
        return cacheBlocks.containsKey(getTag(memoryAddress));
    }
    /**
     * Helper method to determine the tag of a decimal value memory address
     *
     * @return The GPR register based on parameter integer.(if (i == 0){ return R0;})
     */
    public static String getTag(int decimalValue) {
        // Convert the decimal value to an octal string
        StringBuilder octalStr = new StringBuilder(Integer.toOctalString(decimalValue));

        // Ensure the octal string has at least four digits to safely remove the last one so the output can be 3 digits
        while (octalStr.length() < 4) {
            octalStr.insert(0, "0");
        }

        // Remove the last digit of the octal string
        return octalStr.substring(0, octalStr.length() - 1);
    }

    public String toBinaryStringWithLeadingZeros(int value) {
        final String binaryString;
        if (value < 0) {
            binaryString = Integer.toBinaryString((1 << sizeCache) + value);
        } else {
            binaryString = Integer.toBinaryString(value);
        }

        int zerosNeeded = sizeCache - binaryString.length();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < zerosNeeded; i++) {
            result.append("0");
        }
        result.append(binaryString);
        return result.length() > sizeCache ? result.substring(result.length() - sizeCache) : result.toString();
    }

    public void updateCacheConsole(JTextArea cacheConsole){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        cacheConsole.setText("[ " + formattedDateTime + " ] CURRENT STATE OF CACHE:\n");
        cacheBlocks.forEach((key, value) -> {
//            System.out.println();
//            System.out.println(key);
//            System.out.print(value);
            cacheConsole.append(key + ": [");
            value.forEach(number -> cacheConsole.append(toBinaryStringWithLeadingZeros(number) + " "));
            cacheConsole.append("]\n");
        });

    }

    public void resetCache(){
        cacheBlocks.clear();
        for(int i = 0; i < sizeCache; i++){
            ArrayList<Integer> line = new ArrayList<>();
            for(int j = 0; j < sizeLine; j++){
                line.add(0);
            }
            this.cacheBlocks.put(String.valueOf(-i-1), line);

        }
    }
}
