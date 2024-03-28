package GUI;

import CPU.CPU_Registers;
import Simulator.Simulator;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.stream.Collectors;

/**
 * The GUI class is the responsible for all the front-end behaviors, it includes all the Java front-end component.
 * Front-end Layout is included in GUI.form
 *
 * Main Method of the Program is included in this file
 */
public class GUI {
    private IO_Devices keyboardIO = new IO_Devices("Keyboard");
    private JPanel mainPanel;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JTextField valueR0;
    private JButton btnR0;
    private JLabel label_R0;
    private JLabel label_R1;
    private JLabel label_R2;
    private JLabel label_R3;
    private JLabel label_X1;
    private JLabel label_X2;
    private JLabel label_X3;
    private JLabel label_PC;
    private JLabel label_IR;
    private JLabel label_MAR;
    private JLabel label_MBR;
    private JLabel label_MFR;
    private JLabel label_CC;
    private JTextField valueR1;
    private JTextField valueR2;
    private JTextField valueR3;
    private JTextField valueX1;
    private JTextField valueX2;
    private JTextField valueX3;
    private JTextField valuePC;
    private JTextField valueIR;
    private JTextField valueMAR;
    private JTextField valueMBR;
    private JTextField valueMFR;
    private JTextField valueCC;
    private JButton btnR1;
    private JButton btnR2;
    private JButton btnR3;
    private JButton btnX1;
    private JButton btnX2;
    private JButton btnX3;
    private JButton btnPC;
    private JButton btnIR;
    private JButton btnMAR;
    private JButton btnMBR;
    private JButton btnMFR;
    private JButton btnCC;
    private JLabel labelOctal;
    private JTextField valueOctal;
    private JLabel labelBinary;
    private JTextField valueBinary;
    private JPanel northPanel;
    private JButton btnIPL;
    private JButton btnLoad;
    private JButton btnStore;
    private JButton btnRun;
    private JButton btnStep;
    private JButton btnHalt;
    private JTextArea cacheTitle;
    private JTextArea cacheConsole;
    private JCheckBox expandCheckbox;
    private JTextArea OutputConsoleTitle;
    private JTextArea outputConsole;
    private JTextField inputField;
    private JButton testBtn;
    private JFileChooser IPLFileChooser = new JFileChooser();
    public GUI(){

    }

    private JTextArea getOutputConsole(){
        return this.outputConsole;
    }

    /**
     * Set input limiter for all the JTextField.
     * For example,
     * Registers value are not editable by keyboard;
     * Binary and Octal input field can only take 0-1 and 0-7, and it has a length limit.
     *
     */
    private void setInputLimiter(){
        ((AbstractDocument) valueBinary.getDocument()).setDocumentFilter(new BinaryDocumentFilter(16));
        ((AbstractDocument) valueOctal.getDocument()).setDocumentFilter(new OctalDocumentFilter(6));
        valueR0.setEditable(false);

        valueR1.setEditable(false);
        valueR2.setEditable(false);
        valueR3.setEditable(false);
        valueX1.setEditable(false);
        valueX2.setEditable(false);
        valueX3.setEditable(false);
        valuePC.setEditable(false);
        valueIR.setEditable(false);
        valueMAR.setEditable(false);
        valueMBR.setEditable(false);
        valueMFR.setEditable(false);
        valueCC.setEditable(false);

        cacheTitle.setEditable(false);
        cacheConsole.setEditable(false);
        OutputConsoleTitle.setEditable(false);
        outputConsole.setEditable(false);
        inputField.setEditable(false);
        inputField.setText("INPUT NOT AVAILABLE NOW");
    }

    /**
     * Refresh all the register value JTextField so it is showing the newest value of the register by reaching to the backend(simulator.CPU)
     */
    private void updateRegisterValues(Simulator simulator){
        CPU_Registers CPU = simulator.CPU;
        valueR0.setText(CPU.R0.toBinaryStringWithLeadingZeros());
        System.out.println("valueR0 set to " + CPU.R0.toBinaryStringWithLeadingZeros());
        valueR1.setText(CPU.R1.toBinaryStringWithLeadingZeros());
        valueR2.setText(CPU.R2.toBinaryStringWithLeadingZeros());
        valueR3.setText(CPU.R3.toBinaryStringWithLeadingZeros());

        valueX1.setText(CPU.X1.toBinaryStringWithLeadingZeros());
        valueX2.setText(CPU.X2.toBinaryStringWithLeadingZeros());
        valueX3.setText(CPU.X3.toBinaryStringWithLeadingZeros());

        valuePC.setText(CPU.PC.toBinaryStringWithLeadingZeros());
        valueIR.setText(CPU.IR.toBinaryStringWithLeadingZeros());
        valueMAR.setText(CPU.MAR.toBinaryStringWithLeadingZeros());
        valueMBR.setText(CPU.MBR.toBinaryStringWithLeadingZeros());
        valueMFR.setText(CPU.MFR.toBinaryStringWithLeadingZeros());

        String cc0 = CPU.cc0.get() ? "1" : "0";
        String cc1 = CPU.cc1.get() ? "1" : "0";
        String cc2 = CPU.cc2.get() ? "1" : "0";
        String cc3 = CPU.cc3.get() ? "1" : "0";
        String cc = cc0 + cc1 + cc2 + cc3;
        valueCC.setText(cc);
        simulator.memory.updateCacheConsole(cacheConsole);
    }
    /**
     * Helper method for Binary to Octal conversion
     *
     * @return Octal value String
     *
     */
    public static String binaryToOctal(String binaryStr) {
//        int binaryLen = binaryStr.length();
//        StringBuilder octalStr = new StringBuilder();
//
//        int padding = 3 - (binaryLen % 3);
//        if (padding != 3) {
//            binaryStr = "0".repeat(padding) + binaryStr;
//        }
//
//        for (int i = 0; i < binaryStr.length(); i += 3) {
//            String threeBits = binaryStr.substring(i, i + 3);
//            int octalDigit = Integer.parseInt(threeBits, 2);
//            octalStr.append(octalDigit);
//        }
//
//        return octalStr.toString();
        binaryStr = String.format("%16s", binaryStr).replace(' ', '0');

        boolean isNegative = binaryStr.charAt(0) == '1';

        String octalStr;
        if (isNegative) {
            String positiveBinary = convertFromTwosComplement(binaryStr);
            int decimal = Integer.parseInt(positiveBinary, 2);
            octalStr = Integer.toOctalString(decimal);
            octalStr = "-" + octalStr;
        } else {
            int decimal = Integer.parseInt(binaryStr, 2);
            octalStr = Integer.toOctalString(decimal);
        }

        return octalStr;
    }

    private static String convertFromTwosComplement(String binaryStr) {
        String inverted = binaryStr.chars()
                .mapToObj(c -> c == '1' ? "0" : "1")
                .collect(Collectors.joining());

        long decimal = Long.parseLong(inverted, 2) + 1;
        String positiveBinary = Long.toBinaryString(decimal);

        positiveBinary = String.format("%16s", positiveBinary).replace(' ', '0');
        if (positiveBinary.length() > 16) {
            positiveBinary = positiveBinary.substring(positiveBinary.length() - 16);
        }

        return positiveBinary;
    }

    /**
     * Helper method for Octal to Binary conversion
     *
     * @return Binary value String
     *
     */
    public static String octalToBinary(String octalStr) {
        StringBuilder binaryStr = new StringBuilder();
        boolean isNegative = octalStr.startsWith("-");

        if (isNegative) {
            octalStr = octalStr.substring(1); // Remove the leading '-' for processing
        }

        if (octalStr.contains("-")) {
            return ""; // Invalid string due to internal '-' characters
        }

        for (int i = 0; i < octalStr.length(); i++) {
            int octalDigit = Character.digit(octalStr.charAt(i), 8);
            String binaryGroup = Integer.toBinaryString(octalDigit);
            binaryStr.append(String.format("%3s", binaryGroup).replace(' ', '0'));
        }

        String resultBinary = binaryStr.toString();
        // Ensure the binary string is 16 bits
        resultBinary = String.format("%16s", resultBinary).replace(' ', '0');

        if (isNegative) {
            resultBinary = toTwosComplement(resultBinary);
        }else{
            resultBinary = resultBinary.replaceFirst("^0+(?!$)", "");
        }

        return resultBinary;
//        StringBuilder binaryStr = new StringBuilder();
//
//        for (int i = 0; i < octalStr.length(); i++) {
//            int octalDigit = Character.digit(octalStr.charAt(i), 8);
//            String binaryGroup = Integer.toBinaryString(octalDigit);
//
//            String paddedBinaryGroup = "0".repeat(3 - binaryGroup.length()) + binaryGroup;
//            binaryStr.append(paddedBinaryGroup);
//        }
//
//        return binaryStr.toString();
    }

    private static String toTwosComplement(String binaryStr) {
        // Invert bits
        binaryStr = binaryStr.replaceAll("0", "x").replaceAll("1", "0").replaceAll("x", "1");

        // Add 1 to the result
        int length = binaryStr.length();
        StringBuilder result = new StringBuilder(binaryStr);
        boolean carry = true;
        for (int i = length - 1; i >= 0 && carry; i--) {
            if (result.charAt(i) == '1') {
                result.setCharAt(i, '0');
            } else {
                result.setCharAt(i, '1');
                carry = false;
            }
        }

        return result.toString();
    }

    /**
     * add Action Listeners for all the front end parts
     *
     */
    private void addActionListeners(Simulator simulator){
        realizeOctalBinaryConversion(simulator);
        realizeRegisterBtns(simulator);
        realizeTopBtns(simulator);
    }
    /**
     * add Action Listeners for all the Buttons at the top of the window
     * Including: IPL, Run, Step, Halt, Load, Store
     *
     */
    private void realizeTopBtns(Simulator simulator) {
        btnIPL.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Getting file
                if (e.getSource() == btnIPL) {
                    int select = IPLFileChooser.showOpenDialog(null);
                    if (select == IPLFileChooser.APPROVE_OPTION) {
                        File IPLFile = IPLFileChooser.getSelectedFile();
                        System.out.println("IPL: " + IPLFile.getName());
                        //simulator = new Simulator();
                        //refresh the computer whenever IPL is clicked
                        simulator.refreshSimulator();
                        valueOctal.setText("");
                        valueBinary.setText("");
                        // Call the simulator to load memory with input file
                        simulator.loadMemory(IPLFile);
                        updateRegisterValues(simulator);
                    }
                }
            }
        }));

        btnRun.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnRun) {
                    //Call the bus to run
                    simulator.bus.run();
                    //update front end values
                    updateRegisterValues(simulator);
                }
            }
        }));

        btnStep.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnStep) {
                    //call the bus to step
                    simulator.bus.step();
                    //update front end values
                    updateRegisterValues(simulator);
                }
            }
        }));
        testBtn.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == testBtn) {
                    simulator.memory.printAllMemoryValues();
                }
            }
        }));

        btnHalt.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnHalt) {
                    //call the bus to halt
                    simulator.bus.halt();
                    //update front end values
                    updateRegisterValues(simulator);
                }
            }
        }));

        btnLoad.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnLoad) {
                    //load button take the Memory[MAR.value] and give that value to MBR
                    int marVal = simulator.CPU.MAR.getValue();
                    simulator.CPU.MBR.setValue(simulator.memory.get(marVal));
                    //update front end values
                    updateRegisterValues(simulator);
                }
            }
        }));

        btnStore.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnStore) {
                    //store button make Memory[MAR.value] = MBR.value
                    int marVal = simulator.CPU.MAR.getValue();
                    int mbrVal = simulator.CPU.MBR.getValue();
                    simulator.memory.set(marVal, mbrVal);
                    //update front end values
                    updateRegisterValues(simulator);
                }
            }
        }));

        expandCheckbox.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == expandCheckbox) {

                    //based on the status of the expand check box to decide the size of memory: 2048 // 4096
                    if (expandCheckbox.isSelected()){
                        simulator.memory.expandSize();
                    }else{
                        simulator.memory.resumeSize();
                    }
                }
            }
        }));
    }


    /**
     * add Action Listeners for Binary to Octal Conversion, and Octal to Binary Conversion
     *
     */
    private void realizeOctalBinaryConversion(Simulator simulator){
        CPU_Registers CPU = simulator.CPU;
        valueOctal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valueBinary.setText(octalToBinary(valueOctal.getText()));
            }
        });

        valueBinary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valueOctal.setText(binaryToOctal(valueBinary.getText()));
            }
        });
    }
    /**
     * add Action Listeners for all the register loading button, they will load the value from binary window into the register
     *
     * CC and MFR are only for display so their buttons are not activated
     *
     */
    private void realizeRegisterBtns(Simulator simulator){
        CPU_Registers CPU = simulator.CPU;
        btnR0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.R0.setValue(valueBinary.getText());
                valueR0.setText(CPU.R0.toBinaryStringWithLeadingZeros());
            }
        });
        btnR1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.R1.setValue(valueBinary.getText());
                valueR1.setText(CPU.R1.toBinaryStringWithLeadingZeros());
            }
        });
        btnR2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.R2.setValue(valueBinary.getText());
                valueR2.setText(CPU.R2.toBinaryStringWithLeadingZeros());
            }
        });
        btnR3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.R3.setValue(valueBinary.getText());
                valueR3.setText(CPU.R3.toBinaryStringWithLeadingZeros());
            }
        });
        btnX1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.X1.setValue(valueBinary.getText());
                valueX1.setText(CPU.X1.toBinaryStringWithLeadingZeros());
            }
        });
        btnX2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.X2.setValue(valueBinary.getText());
                valueX2.setText(CPU.X2.toBinaryStringWithLeadingZeros());
            }
        });
        btnX3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.X3.setValue(valueBinary.getText());
                valueX3.setText(CPU.X3.toBinaryStringWithLeadingZeros());
            }
        });
        btnPC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.PC.setValue(valueBinary.getText());
                valuePC.setText(CPU.PC.toBinaryStringWithLeadingZeros());
            }
        });
        btnMAR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.MAR.setValue(valueBinary.getText());
                valueMAR.setText(CPU.MAR.toBinaryStringWithLeadingZeros());
            }
        });
        btnMBR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.MBR.setValue(valueBinary.getText());
                valueMBR.setText(CPU.MBR.toBinaryStringWithLeadingZeros());
            }
        });
        btnIR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPU.IR.setValue(valueBinary.getText());
                valueIR.setText(CPU.IR.toBinaryStringWithLeadingZeros());
            }
        });
    }

    /**
     * The main method
     *
     */
    public static void main(String args[]) {
//        JFrame jFrame = new JFrame("Team 6 Computer Simulator.Simulator");
//        GUI gui = new GUI();
//        Simulator simulator = new Simulator();
//        //CPU_Registers CPU = new CPU_Registers();
//        gui.setInputLimiter();
//        gui.updateRegisterValues(simulator);
//        gui.addActionListeners(simulator);
//        jFrame.setContentPane(gui.mainPanel);
//        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jFrame.pack();
//        jFrame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Initialize the GUI components
                GUI gui = new GUI();
                Simulator simulator = new Simulator();

                gui.setInputLimiter();
                gui.updateRegisterValues(simulator);
                gui.addActionListeners(simulator);
                simulator.getOutputConsole(gui.getOutputConsole());
                // Create the JFrame with title
                JFrame jFrame = new JFrame("Team 6 Computer Simulator.Simulator");

                // Set up the JFrame
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.setContentPane(gui.mainPanel);
                jFrame.pack();
                jFrame.setVisible(true);
            }
        });
    }
}
