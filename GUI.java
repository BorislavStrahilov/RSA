/**
 * ************************************************************************************************
 * Antonio Wayne Shelton - ashelt7@uic.edu 
 * Borislav Strahilov - bstrah2@uic.edu
 *
 * CS342 Project 03 - A java-based implementation of the RSA encryption
 * algorithm
 * 
 * files Version 1.0
 *
 *************************************************************************************************
 */

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
 
public class GUI extends JFrame{
 
 // get the monitor in use so that window can 
 // pop up in the middle of the user's screen
 public static GraphicsDevice gd = 
   GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
 
 //the GridLayout that will contain the four buttons
 GridLayout layoutGrid;
 
 //the width and height of the JFrame window
 private int width  = 400;
 private int height = 300;
 

 // The 4 JButtons that will be used
 private JButton keyCreateButton; //button that will call the key creation functions
 private JButton fileBlockButton; //button that will call the file blocking function
 private JButton fileUnblockButton;//button that will call the file unblocking function
 private JButton rsaButton;   //button that will call either encrypt or decrypt function
 
 // The 4 custom handlers for each of the buttons
 private keyCreateHandler kcHandler;
 private fileBlockHandler fbHandler;
 private fileUnblockHandler fubHandler;
 private rsaHandler rsaHandler;
 
 public Huint qNumber;
 public Huint pNumber;
 
 public File pub_key;
 public File priv_key;
 
 // CONSTRUCTOR
 public GUI() {
  
  //create a grid layout of size 2x2 with 3 pixel
  //width and height between each part of the grid
  layoutGrid = new GridLayout(2, 2, 25, 25);
  
  //set the new GridLayout as the JFrame's layout
  setLayout(layoutGrid);

  // create the button Objects
  keyCreateButton   = new JButton("Create Key");
  fileBlockButton   = new JButton("Block a File");
  fileUnblockButton = new JButton("Unblock a File");
  rsaButton     = new JButton("RSA");
  
  // create the action handler objects
  kcHandler  = new keyCreateHandler(this);
  fbHandler  = new fileBlockHandler();
  fubHandler = new fileUnblockHandler();
  rsaHandler = new rsaHandler();
  
  // set color of buttons to a solid white color
  keyCreateButton.setBackground(Color.white);
  fileBlockButton.setBackground(Color.white);
  fileUnblockButton.setBackground(Color.white);
  rsaButton.setBackground(Color.white);  
  
  //rsaButton.setIcon(new ImageIcon("padlock.png"));
    
  // set the action handlers for the buttons
  keyCreateButton.addActionListener(kcHandler);
  fileBlockButton.addActionListener(fbHandler);
  fileUnblockButton.addActionListener(fubHandler);
  rsaButton.addActionListener(rsaHandler);
  
  // add the four buttons to the JFrame
  add(keyCreateButton);
  add(fileBlockButton);
  add(fileUnblockButton);
  add(rsaButton);

  // JFRAME OPTIONS
  setLocation( (gd.getDisplayMode().getWidth() / 2) - (width/2),    // center the window at user's screen
     (gd.getDisplayMode().getHeight() / 2) - (height/2)
     );          
  setTitle("RSA Encryption/Decription");     // title of JFrame
  setSize(width, height);        // size of the window
  setDefaultCloseOperation(EXIT_ON_CLOSE);   // default action is to close when X is clicked
  setVisible(true);          // setting the JFrame to visible
  
  
 }

 // Inner class that will be the handler for the keyCreate button
 private class keyCreateHandler implements ActionListener{

  //strings to hold the Q and P values
  //both will need to be converted to huint
  private String pNumberString, qNumberString;

  
  // 0 will mean that the user wants to chose
  // their own prime numbers. 1 will mean 
  // that they want the program to generate
  // them.
  private int autoGenerateNumbers;
  
  private boolean chosen_p_is_prime = false;
  private boolean chosen_q_is_prime = false;
  
  private GUI parent;
  
  public keyCreateHandler(GUI parent){
   this.parent = parent;
  }
  
  public void actionPerformed(ActionEvent e) {
   
   //show a message box asking user if they
   //want to pick their own prime numbers
   autoGenerateNumbers = JOptionPane.showConfirmDialog(
       GUI.this, 
       "Would you like to pick your own prime numbers?",
       "Key Creation",
       JOptionPane.YES_NO_OPTION);
   
   
   //if the user chose to enter in their own values
   if(autoGenerateNumbers == 0){
    
    //ask user for a P value
    do{
    pNumberString = JOptionPane.showInputDialog(
        GUI.this, 
        "Enter a prime number P", 
        "Key Creation",
        JOptionPane.PLAIN_MESSAGE);
    
    
    parent.pNumber = new Huint(pNumberString);
    chosen_p_is_prime = RSA.testPrime(parent.pNumber);
    
    //let user know the number is not prime
    if(!chosen_p_is_prime)
     JOptionPane.showMessageDialog(GUI.this, pNumberString + " is not a prime number");
    

    }while(!chosen_p_is_prime);
    
    System.out.println("P is prime");
    
    //ask user for a Q Value
    do{
    qNumberString = JOptionPane.showInputDialog(
        GUI.this, 
        "Enter a prime number Q", 
        "Key Creation",
        JOptionPane.PLAIN_MESSAGE);
    

    parent.qNumber = new Huint(qNumberString);
    chosen_q_is_prime = RSA.testPrime(parent.qNumber);
    
    //let user know the number is not prime
    if(!chosen_q_is_prime)
     JOptionPane.showMessageDialog(GUI.this, qNumberString + " is not a prime number");
    
    }while(!chosen_q_is_prime);
        
    System.out.println("Q is prime");
    //create the keys
    RSA.keyCreation(qNumber, pNumber);
    JOptionPane.showMessageDialog(GUI.this,"Public and Private keys created.");

   }//end user picked number
   
   //auto generate primes
   else{
    FileHandler.generateRandomKeys();
    JOptionPane.showMessageDialog(GUI.this,"Public and Private keys created.");
   }
   
  }//end actionPerformed method
  
 }
 
 // Inner class that will be the handler for the fileBlock button
 private class fileBlockHandler implements ActionListener{

  private JFileChooser fileChooser = new JFileChooser();
  
  public void actionPerformed(ActionEvent e) {
      
   int returnVal = fileChooser.showOpenDialog(GUI.this);
      
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File text_message = fileChooser.getSelectedFile();
                
                FileHandler.createBlockedFile(text_message);
       JOptionPane.showMessageDialog(GUI.this, "Blocked file <rsa_blocked_2.txt> created from <" + text_message.getName() +">");
            }
            
   
  }
  
 }
 
 private class fileUnblockHandler implements ActionListener{
  private JFileChooser fileChooser = new JFileChooser();
  
  public void actionPerformed(ActionEvent e) {
      
   int returnVal = fileChooser.showOpenDialog(GUI.this);
      
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File blocked_message = fileChooser.getSelectedFile();
                
                FileHandler.createUnblockedFile(blocked_message);
       JOptionPane.showMessageDialog(GUI.this, "Unblocked file <rsa_unblocked_2.txt> created from <" + blocked_message.getName() + ">" );

            }
   
  }
 }
 
 // Inner class that will be the handler for the RSA button
 private class rsaHandler implements ActionListener{

  private JFileChooser fileChooser_key = new JFileChooser();
  private JFileChooser fileChooser_block = new JFileChooser();
  
  File key_file;
  File blocked_file;
  
  public void actionPerformed(ActionEvent e) {
   
   //ask user to pick either private or public key file
   JOptionPane.showMessageDialog(GUI.this, "Choose <pubkey.xml> to encrypt or <prikey.xml> to decrypt" );
   
   int returnVal = fileChooser_key.showOpenDialog(GUI.this);
   
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                key_file = fileChooser_key.getSelectedFile();
            }
            
            //ask user to pick a blocked file that will get crypted
   JOptionPane.showMessageDialog(GUI.this, "Now choose a blocked file to get crypted" );
   
   returnVal = fileChooser_block.showOpenDialog(GUI.this);
   
            if (returnVal == JFileChooser.APPROVE_OPTION) {
             blocked_file = fileChooser_block.getSelectedFile();
            }
   
            FileHandler.crypt(key_file, blocked_file);
            
  }
  
 }
 
 

}// end GUI class



