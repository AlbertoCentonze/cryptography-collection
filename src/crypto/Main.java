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
    testXor();
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
    byte[] output = Encrypt.vigenere(new byte[] { 105, 32, 119, 97, 110, 116 }, new byte[] {50, -10, 100} );
    for (byte b : output) {
      System.out.println(b);
    }
  }

  public static void testBruteForceCaesar(){
    byte[] cipheredText = Encrypt.caesar(new byte[] { 105, 32, 119, 97, 110, 116 }, (byte) 50);
    byte[][] result = Decrypt.caesarBruteForce(cipheredText);
    String resultString = "";
    for (byte[] possibleLine : result ){
      resultString += Helper.bytesToString(possibleLine);
      resultString += " ==++== ";
    }
    Helper.writeStringToFile(resultString, "bruteForceCaesar.txt");

  }

  public static void testXor(){
    byte[] cipheredText = Encrypt.xor(new byte[] { 105, 32, 119, 97, 110, 116 }, (byte) 50);
    for (byte element : cipheredText)
      System.out.println(element);
    byte[] decipheredText = Encrypt.xor(cipheredText, (byte) 50);
    for (byte element : decipheredText)
      System.out.println(element);
  }
}
