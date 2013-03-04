/* Author: Amy Schlesener (amy@amyschlesener.com) 02/28/2013 */
	
/* This program generates a six-integer one-time password based off of SHA-256 hash */	

import java.security.MessageDigest;
import java.util.Random;
import java.util.LinkedList;
import java.util.ListIterator;
import java.security.SecureRandom; //better than random
import java.util.Arrays;
import java.io.*; //for testing purposes
 //SHA-2 hash function implementation adapted from http://www.mkyong.com/java/java-sha-hashing-example/


public class generateOTP
{
    public static void main(String[] args)throws Exception
    {
	
	Console console = System.console(); //Get input for testing purposes
	//Start with initialization vector : 
	Random rand = new Random();
	int randomNum = rand.nextInt();
	String IV = Integer.toString(randomNum);
	//System.out.println("Random number (IV): "+ IV);
	String IVtest = "123456";
	String randomNumString;
	//String test2 = "5aba1db3b561abe65a12fd109b50ca5ecfc88e5d106d4b511c7653b843d0e3d4";
 	//SecureRandom randomGenerator = new SecureRandom();
	//byte[] randomNumber = new byte[20];
	//randomGenerator.nextBytes(randomNumber);
 	String app1Hash;
	String app1newHash;
	String app1Password;

 	String app2Hash;
	String app2newHash;
	String app2Password;
	//counter starts at 0 - no clicks yet
	int app1Counter=0;
	int app2Counter=0; 

	//do first run with intialization vector
	generateOTP firstApp = new generateOTP();
 	app1Hash = firstApp.genHash(IVtest);
	app1Password = firstApp.genPassword(app1Hash); 	

	generateOTP secondApp = new generateOTP();
 	app2Hash = secondApp.genHash(IVtest);
	app2Password = firstApp.genPassword(app2Hash); 	


for(int i = 0; i < 20; i++) {
 		app1Hash = firstApp.genHash(app1Hash); //send old hash as seed for next sha hash
		app1Password = firstApp.genPassword(app1Hash); //new OTP will be calculated using the new hash
		app1Counter++;
        	System.out.println("app1 Hash: " + app1Hash);
		System.out.println("app1 OTP: " + app1Password);
		System.out.println(app1Counter);
	}


	/*LinkedList<String> hashList = new LinkedList();
	ListIterator<String> itr = hashList.listIterator();
	int collisionCounter = 0;
*/

//function to study collision property: generate 100,000 OTP, determine collision property

	/*for(int i = 0; i < 2; i++) {
 		Hash = firstRun.genHash(storeHash); //send old hash as seed for next sha hash
		Password = firstRun.genPassword(storeHash); //new OTP will be calculated using the new hash
		hashList.add(storeHash); //store all these hashes in a list
		while(itr.hasNext()) { //search through list for collisions
			if(storeHash == itr.next()) collisionCounter++;
		}
	}*/

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
			System.out.println(shortHashArray[i]);
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

