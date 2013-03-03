import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
/* Author: Amy Schlesener (amy@amyschlesener.com) 03/01/2013 */
	
/* This program generates a six-integer one-time password based off of SHA-256 hash */	
/*This is the GUI version*/

import java.security.MessageDigest;
import java.util.Random;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Arrays;
import java.security.GeneralSecurityException;
import java.io.*; //for testing purposes
/**
 *
 * @author schlesa
 */
public class generateOTP_UI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws Exception {
new PasswordGenGUI().setVisible(true);  
new OTPValidatorGUI().setVisible(true);       


Console console = System.console(); //Get input for testing purposes
	//Start with initialization vector : 
	Random rand = new Random();
	int randomNum = rand.nextInt();
	String IV = Integer.toString(randomNum);
	//System.out.println("Random number (IV): "+ IV);
	String IVtest = "123456";
	String randomNumString;

 	String app1Hash;
	String app1newHash;
	String app1Password;

 	String app2Hash;
	String app2newHash;
	String app2Password;


	//create object of other classes so they can be passed to the GUI functions
	//genHash test = new genHash();

	//do first run with intialization vector
	generateOTP_UI firstApp = new generateOTP_UI();
 	app1Hash = firstApp.genHash(IVtest);
	app1Password = firstApp.genPassword(app1Hash); 	

	generateOTP_UI secondApp = new generateOTP_UI();
 	app2Hash = secondApp.genHash(IVtest);
	app2Password = firstApp.genPassword(app2Hash); 	

	

//APP2 - expects to start at 1
	/*boolean sync = false;;
	if(app1Counter == app2Counter) { //both synced up so we can prompt user for password
		sync = true;
		System.out.println("synced!");
	}
	else if ((app1Counter-app2Counter)<100) { //out of sync but within 100
		for (int i = 0; i < (app1Counter-1); i++) {
	 		app2Hash = secondApp.genHash(app2Hash); //send old hash as seed for next sha hash
			app2Password = secondApp.genPassword(app2Hash); //new OTP will be calculated using the new hash
			app2Counter++;
			System.out.println(app2Counter);
		}
		//TODO: prompt user to try again
		sync = true; //now we should by synced up again
		System.out.println("we appear to be out of sync");
	}
	else if ((app1Counter+1)==app2Counter) { //state at beginning - nothing has been clicked yet
		System.out.println("what are we doing here?");	
	}
	else sync = false;

	if(sync == true) { //counts are synced, generate calculated OTP, check against user's input
		System.out.println("true");
	 	app2Hash = secondApp.genHash(app2Hash); //send old hash as seed for next sha hash
		app2Password = secondApp.genPassword(app2Hash); //new OTP will be calculated using the new hash
		app2Counter++;
        	System.out.println("app2 Hash: " + app2Hash);
		System.out.println("app2 OTP: " + app2Password);
		System.out.println(app2Counter);

		//prompt for input - replace with GUI prompt later
		String inputPassword = console.readLine("Password: ");		
		if (inputPassword.equals(app2Password)) { //user inputted correct password, grant access
			System.out.println("CORRECT PASSWORD. ACCESS GRANTED.");
		}
		else { //password incorrect, deny access
			System.out.println("Wrong password. Access denied.");
		}		

	}
	else { //totally out of sync, intrusion attempt detected, SHUT DOWN EVERYTHING
		 //temporary placeholder
	}

 */
    }
//this is where we generate our hash 
	public String genHash(String input)throws Exception {
       		MessageDigest md = MessageDigest.getInstance("SHA-256");
       		 md.update(input.getBytes());
       		 byte byteData[] = md.digest();
        	//convert the byte to hex format method 1
        	StringBuffer sb = new StringBuffer();
        	for (int i = 0; i < byteData.length; i++) {
         		sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        	}
 		//This generates a single hash 

		return sb.toString();
	}
	
	public String genPassword(String hash) {
	//now we need to generate numbers based on the SHA hash output. so take last 12 chars of sb string
 		//convert in pairs to integer values (usually ends up being only first few hex values used to
// make 6-int long number string) 
		char[] hashArray = hash.toCharArray();
		char[] shortHashArray;
		shortHashArray = new char[12];
		char[] tempChar = new char[6];
		for(int i = 0; i < 12; i++) {
			shortHashArray[i] = hashArray[52+i]; //shortHashArray is last 12 digits of entire hash
		}
		
		String Password = "";
		int hexNum;
		for (int i = 0; i < 6; i++) {
			tempChar[0] = shortHashArray[2*i];
			tempChar[1] = shortHashArray[2*i+1];
			String temp = new String(tempChar); //section off blocks of 2 (hex numbers)
			temp = temp.trim();
			hexNum = Integer.parseInt(temp, 16); //convert hex num string to decimal;
			String hexNumString = Integer.toString(hexNum);
				
			if (Password.length() == 5) { //just add first char of hex num

				Password += hexNumString.charAt(0);
				
			}
			else if (Password.length() == 4) { //add only first two chars of hex num
				Password += hexNumString.charAt(0);
				Password += hexNumString.charAt(1);
			}
			else if (Password.length() >= 6) { //password too large, quit
				break;
			}
			else {
				Password += hexNumString; //put converted decimal into password string
			}


		}

		return Password;
	}

}


class PasswordGenGUI extends JFrame {
      /**
     * Creates new form PasswordGenGUI
     */
	//counter starts at 0 - no clicks yet
	public static int app1Counter = 0;
	public static int app2Counter=1; //counter starts at 1 - even without generating p/w via click it expects a p/w
      static String IV = "123456";
public static String app1Password;
	//static String app1Hash = new generateOTP_UI().genHash(IV);
	static String app1Hash;

static {
	
	try {
		app1Hash = new generateOTP_UI().genHash(IV);
	}
	catch(Exception ex) {
		throw new RuntimeException(ex);
	}

}


    public PasswordGenGUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        displayOTP = new javax.swing.JButton();
        passwordBox = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        displayOTP.setText("Click to Generate OTP");
        displayOTP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
	try {
                displayOTPActionPerformed(evt);
            }
	catch(Exception ex)
	{
	throw new RuntimeException(ex);
	}
	}
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(displayOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(passwordBox, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displayOTP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void displayOTPActionPerformed(java.awt.event.ActionEvent evt)throws Exception {                                           
       //String test = "123456";
      

	app1Hash = new generateOTP_UI().genHash(app1Hash); //send old hash as seed for next sha hash
		
	 app1Password = new generateOTP_UI().genPassword(app1Hash); //new OTP will be calculated using the new hash
        app1Counter++;
        System.out.println("app1 Hash: " + app1Hash);
	System.out.println("app1 OTP: " + app1Password);
	System.out.println(app1Counter);
 	passwordBox.setText(app1Password);
    }   

                                       

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])throws Exception {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PasswordGenGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PasswordGenGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PasswordGenGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PasswordGenGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PasswordGenGUI().setVisible(true);
            }
        });


    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton displayOTP;
    private javax.swing.JTextField passwordBox;
    // End of variables declaration              
            
}
//import static PasswordGenGUI.app1counter;
class OTPValidatorGUI extends javax.swing.JFrame {
	static int app2Counter=1; //counter starts at 1 - even without generating p/w via click it expects a p/w
       static String IV = "123456";

	//static String app1Hash = new generateOTP_UI().genHash(IV);
	static String app2Hash;

static {
	
	try {
		app2Hash = new generateOTP_UI().genHash(IV);
	}
	catch(Exception ex) {
		throw new RuntimeException(ex);
	}

}
    /**
     * Creates new form OTPValidator
     */
    public OTPValidatorGUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        checkButton = new javax.swing.JButton();
        passwordEntry = new javax.swing.JTextField();
        validityDisplay = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        checkButton.setText("Click to check if password is valid");
        checkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
	try {
                checkButtonActionPerformed(evt);
            }
	catch(Exception ex)
	{
	throw new RuntimeException(ex);
	}
                
            }
        });

        passwordEntry.setText("(Enter password here)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(passwordEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(validityDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(passwordEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkButton)
                .addGap(28, 28, 28)
                .addComponent(validityDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void checkButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
         //String password = "123456";
         String app2Password = PasswordGenGUI.app1Password;
         String test = passwordEntry.getText();
         if ((test.toString()).equals(app2Password)) {
              validityDisplay.setText("Password correct, access granted.");          
         }
         else {
              validityDisplay.setText("Password incorrect, access DENIED.");
         }
         

    }                                           

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])throws Exception {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OTPValidatorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OTPValidatorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OTPValidatorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OTPValidatorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OTPValidatorGUI().setVisible(true);
            }
        });

	

    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton checkButton;
    private javax.swing.JTextField passwordEntry;
    private javax.swing.JTextField validityDisplay;
    // End of variables declaration      

	


             
}

