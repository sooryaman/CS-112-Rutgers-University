package poly;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

import javax.swing.*;

public class PolytestGUI extends JPanel implements ActionListener{
   private static final long serialVersionUID = 1L;
   static String addString = "Add";
   static String multiplyString = "Multiply";
   static String evaluateString = "Evaluate";
   static Node poly1;

   public PolytestGUI() {
       super(new BorderLayout());
       JRadioButton addButton = new JRadioButton(addString);
       addButton.setMnemonic(KeyEvent.VK_A);
       addButton.setActionCommand(addString);
       addButton.setSelected(true);

       JRadioButton multiplyButton = new JRadioButton(multiplyString);
       multiplyButton.setMnemonic(KeyEvent.VK_B);
       multiplyButton.setActionCommand(multiplyString);
       multiplyButton.setSelected(true);

       JRadioButton evaluateButton = new JRadioButton(evaluateString);
       evaluateButton.setMnemonic(KeyEvent.VK_C);
       evaluateButton.setActionCommand(evaluateString);
       evaluateButton.setSelected(true);

       JLabel topText = new JLabel();
       topText.setText("<html>"+format(Polynomial.toString(poly1))+"</html>");

       ButtonGroup group = new ButtonGroup();
       group.add(addButton);
       group.add(multiplyButton);
       group.add(evaluateButton);

       addButton.addActionListener(this);
       multiplyButton.addActionListener(this);
       evaluateButton.addActionListener(this);

       JPanel radioPanel = new JPanel(new GridLayout(0, 1));
       radioPanel.add(topText);

       radioPanel.add(addButton);
       radioPanel.add(multiplyButton);
       radioPanel.add(evaluateButton);

       add(radioPanel, BorderLayout.LINE_START);
       setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

   }


   private static void createAndShowGUI() {
       //set window name.
       JFrame frame = new JFrame("Polytest");

       //program will exit when window is closed
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       //Create and set up the content pane.
       JComponent newContentPane = new PolytestGUI();
       newContentPane.setOpaque(true); //content panes must be opaque
       frame.setContentPane(newContentPane);

       //Display the window.
       frame.pack();
       frame.setVisible(true);
   }
   public static void main(String[] args) throws IOException {
       String fileName = JOptionPane.showInputDialog(null, "Enter the name of the polynomial file: ");
       Scanner sc2 = null;
       try {
           sc2 = new Scanner(new File(fileName));
       }
       catch (NullPointerException nE) {
           System.exit(0);
       }
       catch (IOException e1) {
           JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
           System.exit(1);
       }
       poly1 = Polynomial.read(sc2);
       System.out.println(Polynomial.toString(poly1));
       javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() { createAndShowGUI();}});

   }

   public void actionPerformed(ActionEvent e) {
       if (e.getActionCommand() == addString) {
           try {
               add();
           } catch (IOException e1) {
               JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
           }
       }
       if (e.getActionCommand() == multiplyString) {
           try {
               multiply();
           } catch (IOException e1) {
               JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
           }
       }
       if (e.getActionCommand() == evaluateString) {
           evaluate();
       }
   }

   public static void add() throws IOException {
       String fileName = JOptionPane.showInputDialog(null, "Enter the file containing the polynomial to add: ");
       if (fileName == null) return;
       Scanner sc2 = new Scanner(new File(fileName));
       Node poly2 = Polynomial.read(sc2);
       String answer = "<html>"+"Sum: "+format(Polynomial.toString(Polynomial.add(poly1,poly2)))+"</html>";
       JLabel label = new JLabel(answer);
       JOptionPane.showMessageDialog(null, label, Polynomial.toString(poly2), JOptionPane.INFORMATION_MESSAGE);
   }

   public static void multiply() throws IOException {
       String fileName = JOptionPane.showInputDialog(null, "Enter the file containing the polynomial to multiply: ");
       if (fileName == null) return;
       Scanner sc2 = new Scanner(new File(fileName));
       Node poly2 = Polynomial.read(sc2);
       String answer = "<html>"+"Product: "+format(Polynomial.toString(Polynomial.multiply(poly1,poly2)))+"</html>";
       JLabel label = new JLabel(answer);
       JOptionPane.showMessageDialog(null, label, Polynomial.toString(poly2), JOptionPane.INFORMATION_MESSAGE);
   }

   public static void evaluate() {
       String s = JOptionPane.showInputDialog(null, "Enter the evaluation point x: ");
       if (s == null) return;
       float x = 0;
       try {
           x = Float.parseFloat(s);
       }
       catch (NumberFormatException e1) {
           JOptionPane.showMessageDialog(null, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
           return;
       }
       JOptionPane.showMessageDialog(null, Float.toString(Polynomial.evaluate(poly1,x)), "Value at " + x + ": ", JOptionPane.INFORMATION_MESSAGE);
   }

   public static String format(String s) {
       s = s.replaceAll(" \\+ \\-", " - ");
       for (int i = 0; i < s.length(); i++) {
          if (s.charAt(i) == '^') {
              String pow = s.substring(i + 1, s.indexOf(" ", i));
              s = s.replaceAll("\\^"+pow, "<sup>"+pow+"</sup>");
          }
       }
       return s;
   }
}

