package crypto;

import java.util.ArrayList;

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
	  testCaesarFrequences();
  }

  // TODO : TO BE COMPLETED
  public static void TestStringEquality(String string1, String string2, String info) {
    if (string1.equals(string2))
      System.out.println("TEST PASSED - " + info);
    else
      System.out.println("TEST FAILED - " + info);

  }

  public static void testRapidCaesar() {
    byte[] output = Encrypt.caesar(new byte[] { 105, 32, 119, 97, 110, 116 }, (byte) 50);
    for (byte b : output) {
      System.out.println(b);
    }
  }

  public static void testRapidVigenere() {
    byte[] output = Encrypt.vigenere(new byte[] { 105, 32, 119, 97, 110, 116 }, new byte[] { 50, -10, 100 });
    for (byte b : output) {
      System.out.println(b);
    }
  }

  public static void testBruteForceCaesar() {
    byte[] cipheredText = Encrypt.caesar(new byte[] { 105, 32, 119, 97, 110, 116 }, (byte) 50);
    byte[][] result = Decrypt.caesarBruteForce(cipheredText);
    String resultString = "";
    for (byte[] possibleLine : result) {
      resultString += Helper.bytesToString(possibleLine);
      resultString += " ==++== ";
    }
    Helper.writeStringToFile(resultString, "bruteForceCaesar.txt");

  }

  public static void testXor() {
    byte[] cipheredText = Encrypt.xor(new byte[] { 105, 32, 119, 97, 110, 116 }, (byte) 50);
    for (byte element : cipheredText)
      System.out.println(element);
    byte[] decipheredText = Encrypt.xor(cipheredText, (byte) 50);
    for (byte element : decipheredText)
      System.out.println(element);
  }

  public static void testBruteForceXor() {
    byte[] cipheredText = Encrypt.xor(new byte[] { 105, 32, 119, 97, 110, 116 }, (byte) 50);

    byte[][] result = Decrypt.xorBruteForce(cipheredText);
    String resultString = "";
    for (byte[] possibleLine : result) {
      resultString += Helper.bytesToString(possibleLine);
      resultString += " ==++== ";
    }
    Helper.writeStringToFile(resultString, "bruteForceXor.txt");

  }

  public static void testOtp() {
    byte[] cipheredText = Encrypt.oneTimePad(new byte[] { 105, 32, 119, 97, 110, 116 },
        new byte[] { 105, 32, 119, 97, 110, 116 });
    for (byte element : cipheredText)
      System.out.println(element);
    byte[] decipheredText = Encrypt.oneTimePad(cipheredText, new byte[] { 105, 32, 119, 97, 110, 116 });
    for (byte element : decipheredText)
      System.out.println(element);
  }

  public static void testRandomOtp() {
    byte[] key = Encrypt.generatePad(6);
    for (byte element : key)
      System.out.println(element);
    byte[] cipheredText = Encrypt.oneTimePad(new byte[] { 105, 32, 119, 97, 110, 116 }, key);
    for (byte element : cipheredText)
      System.out.println(element);
    byte[] decipheredText = Encrypt.oneTimePad(cipheredText, key);
    for (byte element : decipheredText)
      System.out.println(element);
  }
  
  public static void testRemoveSpaces() {
	  
	  byte[] cipheredText  = new byte[] { (byte) 105, (byte) 32, (byte) 119, (byte) 97, (byte) 110, (byte) 116 };
	  ArrayList<Byte> noSpace= new ArrayList<Byte> (Decrypt.removeSpaces(cipheredText));
	  
	  for(int i=0; i < noSpace.size(); i++){
          System.out.println( noSpace.get(i) );
      }
	  
  }

  public static void testCaesarFrequences() {
	  
	  byte[] cipherText = new byte[] { (byte) 105, (byte) 32, (byte) 119, (byte) 97, (byte) 110, (byte) 116 };
	  
	  float [] frequencies = Decrypt.computeFrequencies(cipherText);
	  
	  for(int i=0; i < frequencies.length; i++){
          System.out.println( frequencies[i] );
      }
	  
	  
  }
}
