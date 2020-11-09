package crypto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {

  // ---------------------------MAIN---------------------------
  public static void main(String args[]) {
    breakCipherTest();
  }

  public static void testBruteForce() {
    Helper.writeStringToFile(
        Decrypt.arrayToString(Decrypt
            .xorBruteForce(Encrypt.xor(Helper.stringToBytes(Helper.readStringFromFile("text_one.txt")), (byte) -120))),
        "bruteForceCaesar.txt");
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

    byte[] cipheredText = new byte[] { (byte) 105, (byte) 32, (byte) 119, (byte) 97, (byte) 110, (byte) 116 };
    ArrayList<Byte> noSpace = new ArrayList<Byte>(Decrypt.removeSpaces(cipheredText));

    for (int i = 0; i < noSpace.size(); i++) {
      System.out.println(noSpace.get(i));
    }

  }

  public static void testCaesarFrequences() {

    byte[] cipherText = new byte[] { 105, 32, 119, 97, 110, 116 };

    float[] frequencies = Decrypt.computeFrequencies(cipherText);

    for (int i = 0; i < frequencies.length; i++) {
      System.out.println(frequencies[i]);
    }

  }

  public static void testComputeFrequencies(String text) {
    byte[] textByte = Helper.stringToBytes(Helper.cleanString(text));
    float[] frequencies = Decrypt.computeFrequencies(textByte);

    String output = "";
    for (int i = 0; i < frequencies.length; ++i) {
      if (frequencies[i] == 0)
        continue;
      output += (char) (i - 128);
      output += " freq: ";
      output += frequencies[i];
      output += " ";
    }
    Helper.writeStringToFile(output, "frequenciesOutput.txt");
  }

  public static void testDecryptCBC() {
    byte[] key = new byte[] { 5, -17 };
    byte[] cipher = Encrypt.cbc(new byte[] { 10, 28, 30, 45, 56 }, key);
    byte[] decipher = Decrypt.decryptCBC(cipher, key);
    // Helper.printByteArray(cipher);
    // Helper.printByteArray(decipher);
  }

  public static void testCaesarFindKey() {
    byte[] text = Helper.stringToBytes(Helper.cleanString(Helper.readStringFromFile("text_one.txt")));
    byte[] encoded = Encrypt.caesar(text, (byte) -67);
    float[] frequencies = Decrypt.computeFrequencies(encoded);
    int i = 0;
    for (float f : frequencies) {
      System.out.println(f + " " + i);
      ++i;
    }
    System.out.println(Decrypt.caesarFindKey(frequencies));
  }

  public static void testVigenereKeyLength() {
    List<Byte> encoded = Decrypt.removeSpaces(
        Encrypt.vigenere(Helper.stringToBytes(Helper.readStringFromFile("long_text.txt")), new byte[] { 12, 34, 63 }));
    System.out.println(Decrypt.vigenereFindKeyLength(encoded));
  }

  public static void testVigenereKeyFinder() {
    byte[] cipher = Encrypt.vigenere(Helper.stringToBytes(Helper.readStringFromFile("text_one.txt")),
        new byte[] { 12, -34, 125, 89, -45 });
    Decrypt.vigenereWithFrequencies(cipher);
  }

  public static void testCaesarWithFrequencies() {
    byte[] text = Helper.stringToBytes(Helper.readStringFromFile("long_text.txt"));
    for (Integer key = -128; key < 128; ++key) {
      byte[] encoded = Encrypt.caesar(text, key.byteValue());
      byte resultKey = Decrypt.caesarWithFrequencies(encoded);
      if (resultKey != key)
        System.out.println("TEST FAILED at key " + key);
    }
  }

  public static void testVigenereWithFrequencies() {
    byte[] text = Helper.stringToBytes(Helper.readStringFromFile("text_one.txt"));
    int i = 0;
    while (i < 100) {
      byte[] key = Encrypt.generatePad(4);
      byte[] encoded = Encrypt.vigenere(text, key);
      byte[] resultKey = Decrypt.vigenereWithFrequencies(encoded);
      if (!Arrays.equals(resultKey, key))
        System.out.println("TEST FAILED");
      ++i;
    }
  }

  public static void testCaesarAllCombination() {
    byte[] text = Helper.stringToBytes(Helper.readStringFromFile("text_one.txt"));
    byte[] decoded = new byte[text.length];
    for (Integer key = -128; key < 128; ++key) {
      byte[] encoded = Encrypt.caesar(text, key.byteValue());
      decoded = Encrypt.caesar(encoded, (byte) -key.byteValue());
    }

    for (int i = 0; i < text.length; ++i) {
      byte a = text[i];
      byte b = decoded[i];
      if (a != b) {
        System.out.println(i);
      }
    }
  }

  public static void testXorAllCombinations() {
    byte[] text = Helper.stringToBytes(Helper.readStringFromFile("text_one.txt"));
    byte[] decoded = new byte[text.length];
    for (Integer key = -128; key < 128; ++key) {
      byte[] encoded = Encrypt.xor(text, key.byteValue());
      decoded = Encrypt.xor(encoded, key.byteValue());
    }

    for (int i = 0; i < text.length; ++i) {
      byte a = text[i];
      byte b = decoded[i];
      if (a != b) {
        System.out.println(i);
      }
    }
  }

  public static void testVigenereAllCombinations() {
    byte[] text = Helper.stringToBytes(Helper.readStringFromFile("long_text.txt"));
    int i = 0;
    int size = 1;
    while (i < 1000) {
      if (i > size * 10) {
        ++size;
      }
      byte[] key = Encrypt.generatePad(size);
      byte[] encoded = Encrypt.vigenere(text, key, true);

      byte[] decoded = Encrypt.vigenere(encoded, Helper.keyInverterVigenere(key), true);
      for (int c = 0; c < text.length; ++c) {
        byte a = text[c];
        byte b = decoded[c];
        if (a != b) {
          System.out.println(c);
        }
      }

      ++i;
    }
  }

  public static void testOtpAllCombinations() {
    byte[] text = Helper.stringToBytes(Helper.readStringFromFile("long_text.txt"));
    byte[] key = Encrypt.generatePad(text.length);
    byte[] encoded = Encrypt.oneTimePad(text, key);
    byte[] decoded = Encrypt.oneTimePad(encoded, key);
    for (int c = 0; c < text.length; ++c) {
      byte a = text[c];
      byte b = decoded[c];
      if (a != b) {
        System.out.println(c);
      }
    }
  }

  public static void testCBCAllCombinations() {
    byte[] text = Helper.stringToBytes(Helper.readStringFromFile("long_text.txt"));
    int i = 0;
    int size = 1;
    while (i < 1000) {
      if (i > size * 10) {
        ++size;
      }
      byte[] key = Encrypt.generatePad(size);
      byte[] encoded = Encrypt.cbc(text, key);
      byte[] decoded = Decrypt.decryptCBC(encoded, key);
      for (int c = 0; c < text.length; ++c) {
        byte a = text[c];
        byte b = decoded[c];
        if (a != b) {
          System.out.println(c);
        }
      }
      ++i;
    }
  }

  public static void breakCipherTest() {
    byte[] text = Helper.stringToBytes(Helper.readStringFromFile("long_text.txt"));
    byte[] encoded = Encrypt.vigenere(text, new byte[] { 120, 45, -89 });
    String encodedString = Helper.bytesToString(encoded);
    System.out.println(Decrypt.breakCipher(encodedString, 1));
  }
}
