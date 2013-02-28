import java.security.MessageDigest;
import java.util.Random;
import java.security.SecureRandom; //better than random
import java.util.Arrays;
 //SHA-2 hash function implementation adapted from http://www.mkyong.com/java/java-sha-hashing-example/


public class generateOTP
{
    public static void main(String[] args)throws Exception
    {
	//Start with initialization vector : 
	Random rand = new Random();
	int randomNum = rand.nextInt();
	String IV = Integer.toString(randomNum);
	System.out.println("Random number (IV): "+ IV);
	String IVtest = "123456";
 	//SecureRandom randomGenerator = new SecureRandom();
	//byte[] randomNumber = new byte[20];
	//randomGenerator.nextBytes(randomNumber);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(IV.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 //This generates a single hash based on an initialization vector. 
        System.out.println("Hash (in hex): " + sb.toString());
 
//now we need to generate numbers based on the SHA hash output. so take last 12 chars of sb string, convert in pairs to decimal 
	char[] hashArray = sb.toString().toCharArray();
	char[] shortHashArray;
	shortHashArray = new char[12];
	char[] tempChar = new char[6];
	for(int i = 0; i < 12; i++) {
	shortHashArray[i] = hashArray[52+i]; //shortHashArray is last 12 digits of entire hash
	}

	String Password = "";
	int hexNum;

//System.out.println(hashString);
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
		else if (Password.length() >= 6) { //password too large, quit
			break;
		}
		else {
			Password += hexNumString; //put converted decimal into password string
		}


	}
	System.out.println("OTP: " + Password);




}
}

