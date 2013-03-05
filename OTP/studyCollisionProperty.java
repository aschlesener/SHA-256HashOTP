/* Author: Amy Schlesener (amy@amyschlesener.com) 02/28/2013 */
	
/* This program calculates the number of collisions for however many one-time-passwords generated. */	

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.ListIterator;
import java.security.SecureRandom; //better than random
import java.util.Arrays;
import java.io.*; //for testing purposes
 //SHA-2 hash function implementation adapted from http://www.mkyong.com/java/java-sha-hashing-example/

public class studyCollisionProperty
{
    public static void main(String[] args)throws Exception
    {
	
	Console console = System.console(); //Get input for testing purposes
	//Start with initialization vector : 
 	SecureRandom randomGenerator = new SecureRandom();
	byte[] randomNumber = new byte[20];
	randomGenerator.nextBytes(randomNumber);
	String IV = new String(randomNumber);
 	String app1Hash;
	String app1newHash;
	String app1Password;
 	String app2Hash;
	String app2newHash;
	String app2Password;

	//do first run with intialization vector
	generateOTP firstApp = new generateOTP();
 	app1Hash = firstApp.genHash(IV);
	app1Password = firstApp.genPassword(app1Hash); 	

int collisionCounter = 0;
//make an arraylist of nodes - each OTP will be stored in a node, along with its hash
final int TABLE_SIZE = 1000; //max number of OTPs we can have, i.e. no collisions
ArrayList<Node> passwordTable = new ArrayList<>(TABLE_SIZE);

//function to study collision property: generate 100,000 OTP, determine collision property
	String temp;
	for(int i = 1; i <= TABLE_SIZE; i++) {
		String originalHash = app1Hash; //store original value of app1Hash
 		app1Hash = firstApp.genHash(app1Hash); //send old hash as seed for next sha hash
		app1Password = firstApp.genPassword(app1Hash); //new OTP will be calculated using the new hash
		//search for OTP duplicates - if find, check for hash equivalence; if same, add to arraylist but 
		Node record = new Node(originalHash, app1Password);
		
		if(passwordTable.contains(record)) { //duplicate node found - do nothing
			System.out.println("duplicate node found");
		}
		//NOTE: THIS IS VERY INEFFICENT AND TIME-CONSUMING.
		else { //scan through list to find collision (duplicate OTP but diff orglHash
			passwordTable.add(record); //adds current OTP node to current posn in list
			for (int j = 0; j < i; j++) {
				
				if((passwordTable.get(j).OTP).equals(app1Password) && !(passwordTable.get(j).oldHash).equals(originalHash)) { //collision found!
				collisionCounter++;
		}

			}
		}
	}

System.out.println("Number of OTPs generated: " + TABLE_SIZE);
System.out.println("Number of collisions: " + collisionCounter); 
			//0 collisions found at 10
			//0 collisions found at 100
			//2 collisions found at 1000
			//521 collisions found at 10,000
			//49,720 collisions found at 100,000


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
	//now we need to generate numbers based on the SHA hash output. so take last 12 chars of sb string, 		convert in pairs to integer values (usually ends up being only first few hex values used to make 6-int long number string) 
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
			else if (Password.length() == 4) {              
				if(hexNum<10) {  // if hex num is only 1 char long, add just that 
					//(and continue in loop to add first char of next hex num)
					Password += hexNumString.charAt(0);
				}
				else {					//add first two chars of hex num ()) 
					Password += hexNumString.charAt(0);
					Password += hexNumString.charAt(1);
				}
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

//Node will contain both the hash of a message (represented as the OTP) and the actual message (represented as the old hash), so we can compare nodes in our ArrayList and find collisions via searching for nodes that have the same OTP but different messages (oldHash).
class Node {
public String oldHash;
public String OTP;
	
	public Node(String _oldHash, String _OTP) {
		oldHash = _oldHash;
		OTP = _OTP;
}
}

