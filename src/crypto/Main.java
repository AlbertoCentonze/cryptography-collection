package crypto;

import crypto.Helper;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {

  // ---------------------------MAIN---------------------------
  public static void main(String args[]) {

    String inputMessage = Helper.readStringFromFile("text_one.txt");
    String key = "2cF%5";

    String messageClean = Helper.cleanString(inputMessage);

    byte[] messageBytes = Helper.stringToBytes(messageClean);
    byte[] keyBytes = Helper.stringToBytes(key);

    System.out.println("Original input sanitized : " + messageClean);
    System.out.println();

    System.out.println("------Caesar------");
    testCaesar(messageBytes, keyBytes[0]);

    // TODO: TO BE COMPLETED

  }

  // Run the Encoding and Decoding using the caesar pattern
  public static void testCaesar(byte[] string, byte key) {
    // Encoding
    System.out.println("Encoding with Caesar");
    System.out.println("String: " + string);
    System.out.println("Key: " + key);
    byte[] result = Encrypt.caesar(string, key);
    String s = Helper.bytesToString(result);
    System.out.println("Encoded: " + s);

    // Decoding with key 
    System.out.println("Decoding with reverse key");
    String sD = Helper.bytesToString(Encrypt.caesar(result, (byte) (-key)));
    System.out.println(sD);

    // Decoding without key
    byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
    String sDA = Decrypt.arrayToString(bruteForceResult);
    // TODO Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");

    byte decodingKey = Decrypt.caesarWithFrequencies(result);
    String sFD = Helper.bytesToString(Encrypt.caesar(result, decodingKey));
    System.out.println("Decoded without knowing the key : " + sFD);
  }

  // TODO : TO BE COMPLETED
  public static void TestStringEquality(String string1, String string2, String info) {
	  if (string1.equals(string2))
		System.out.println("TEST PASSED - " + info);
	  else
		System.out.println("TEST FAILED - " + info);
		  
  }
}

  
  