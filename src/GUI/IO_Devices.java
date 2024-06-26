package GUI;

import javax.swing.*;

public class IO_Devices {


    private String deviceName;
    private String value;
    public IO_Devices(String deviceName){
        this.deviceName = deviceName;
    }

    public String getValue() {
        return value;
    }

    public int getASCII_value(){
        int out = 3;
        if (value.length() == 1){
            out = (int) value.charAt(0);
        }
        return out;
    }

    //This method is used to get input from the user
    public void askForInput(String promptSentence) {
        //Create a new JFrame with the given deviceName
        JFrame frame = new JFrame(deviceName);
        //Show a dialog to the user, asking for input
        String input = JOptionPane.showInputDialog(frame, "[ " + deviceName + " input ]" + promptSentence);
        //Store the input in the value variable
        value = input;
    }
}
