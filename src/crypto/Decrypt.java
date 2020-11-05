package crypto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decrypt {

  public static final byte SPACE = 32;

  public static final int ALPHABETSIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1; // 256
  public static final int APOSITION = 97 + ALPHABETSIZE / 2;

  // source : https://en.wikipedia.org/wiki/Letter_frequency
  public static final double[] ENGLISHFREQUENCIES = { 0.08497, 0.01492, 0.02202, 0.04253, 0.11162, 0.02228, 0.02015,
      0.06094, 0.07546, 0.00153, 0.01292, 0.04025, 0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.07587, 0.06327,
      0.09356, 0.02758, 0.00978, 0.0256, 0.0015, 0.01994, 0.00077 };

  public static final int CAESAR = 0;
  public static final int VIGENERE = 1;
  public static final int XOR = 2;

  /**
   * Method to break a string encoded with different types of cryptosystems
   * 
   * @param type the integer representing the method to break : 0 = Caesar, 1 =
   *             Vigenere, 2 = XOR
   * @return the decoded string or the original encoded message if type is not in
   *         the list above.
   */
  public static String breakCipher(String cipher, int type) {
    byte[] encoded = Helper.stringToBytes(cipher);
    if (type == CAESAR) {
      byte originalKey = caesarWithFrequencies(encoded);
      return Helper.bytesToString(Encrypt.caesar(encoded, originalKey));
    } else if (type == VIGENERE) {
      byte[] originalKey = vigenereWithFrequencies(encoded);
      return "";
    } else if (type == XOR) {
      return arrayToString(xorBruteForce(encoded));
    }
    return cipher;

  }

  /**
   * Converts a 2D byte array to a String
   * 
   * @param bruteForceResult a 2D byte array containing the result of a brute
   *                         force method
   */
  public static String arrayToString(byte[][] bruteForceResult) {
    String result = "";

    for (byte[] element : bruteForceResult) {
      result += element;
      result += System.lineSeparator();
    }

    return result;
  }

  // -----------------------Caesar-------------------------

  /**
   * Method to decode a byte array encoded using the Caesar scheme This is done by
   * the brute force generation of all the possible options
   * 
   * @param cipher the byte array representing the encoded text
   * @return a 2D byte array containing all the possibilities
   */
  public static byte[][] caesarBruteForce(byte[] cipher) {
    final int LOWER_BOUND = -128;
    byte[][] result = new byte[256][cipher.length];
    for (int i = 0; i < result.length; ++i) {
      Integer integerKey = Integer.valueOf(LOWER_BOUND + i);
      byte byteKey = integerKey.byteValue();

      result[i] = Encrypt.caesar(cipher, byteKey);
    }
    return result;
  }

  /**
   * Method that finds the key to decode a Caesar encoding by comparing
   * frequencies
   * 
   * @param cipherText the byte array representing the encoded text
   * @return the encoding key
   */
  public static byte caesarWithFrequencies(byte[] cipherText) {
    // TODO : COMPLETE THIS METHOD

    return -1; // TODO: to be modified
  }

  /**
   * Method that computes the frequencies of letters inside a byte array
   * corresponding to a String
   * 
   * @param cipherText the byte array
   * @return the character frequencies as an array of float
   */
  public static float[] computeFrequencies(byte[] cipherText) {

    float[] frequencies = new float[256];

    for (int i = 0; i < frequencies.length; i++) {
      float coincidence = 0;
      if (i == 32) {
        continue;
      }
      for (int j = 0; j < cipherText.length; ++j) {

        if (cipherText[j] == (i - 128)) {
          ++coincidence;
        }
      }
      frequencies[i] = coincidence / 256;
    }
    return frequencies;
  }

  /**
   * Method that finds the key used by a Caesar encoding from an array of
   * character frequencies
   * 
   * @param charFrequencies the array of character frequencies
   * @return the key
   */
  public static byte caesarFindKey(float[] charFrequencies) {
    byte key;

    double[] scalarProducts = new double[256];
    for (int offset = 0; offset < ALPHABETSIZE; ++offset) {
      for (int i = 0 + offset; i < 26 + offset; ++i) {
        int x = i % 256;
        int j = i - offset;
        scalarProducts[offset] = charFrequencies[x] * ENGLISHFREQUENCIES[j];
      }
    }

    double maximumValue = 0.0;
    int maximumIndex = 0;
    for (int i = 0; i < scalarProducts.length; ++i) {
      if (maximumValue < scalarProducts[i]) {
        maximumIndex = i;
        maximumValue = scalarProducts[i];
      }
    }

    key = (byte) maximumIndex;

    return key;
  }

  // -----------------------XOR-------------------------

  /**
   * Method to decode a byte array encoded using a XOR This is done by the brute
   * force generation of all the possible options
   * 
   * @param cipher the byte array representing the encoded text
   * @return the array of possibilities for the clear text
   */
  public static byte[][] xorBruteForce(byte[] cipher) {

    final int LOWER_BOUND = -128;
    byte[][] result = new byte[255][cipher.length];
    for (int i = 0; i < result.length; ++i) {
      Integer integerKey = Integer.valueOf(LOWER_BOUND + i);

      byte byteKey = integerKey.byteValue();

      result[i] = Encrypt.xor(cipher, byteKey);
    }
    return result;

  }

  // -----------------------Vigenere-------------------------
  // Algorithm : see https://www.youtube.com/watch?v=LaWp_Kq0cKs
  /**
   * Method to decode a byte array encoded following the Vigenere pattern, but in
   * a clever way, saving up on large amounts of computations
   * 
   * @param cipher the byte array representing the encoded text
   * @return the byte encoding of the clear text
   */
  public static byte[] vigenereWithFrequencies(byte[] cipher) {
    // TODO : COMPLETE THIS METHOD
    return null; // TODO: to be modified
  }

  /**
   * Helper Method used to remove the space character in a byte array for the
   * clever Vigenere decoding
   * 
   * @param array the array to clean
   * @return a List of bytes without spaces
   */
  public static List<Byte> removeSpaces(byte[] array) {

    ArrayList<Byte> noSpace = new ArrayList<Byte>();

    for (int i = 0; i < array.length; i++) {

      if (array[i] != SPACE && i < array.length) {
        noSpace.add(array[i]);
      }
    }
    return noSpace;
  }

  /**
   * Method that computes the key length for a Vigenere cipher text.
   * 
   * @param cipher the byte array representing the encoded text without space
   * @return the length of the key
   */
  public static int vigenereFindKeyLength(List<Byte> cipher) {

    /*
     * ArrayList< ArrayList<Byte> > stock = new ArrayList< ArrayList<Byte> >();
     * ArrayList<Byte> stock = new ArrayList <Byte>();
     * 
     * for(int i=0;i<cipher.size();i++){
     * 
     * ArrayList<Byte> shiftedCipher= new ArrayList<Byte>();
     * 
     * for(int j=i+1;j<cipher.size();j++){
     * 
     * shiftedCipher.add(cipher.get(j)); byte Coincidence = 0;
     * 
     * if(cipher.contains(shiftedCipher.get(j))) { ++Coincidence; }
     * shiftedCipher.add(Coincidence); }
     * 
     * 
     * 
     * int key=-1;
     * 
     * return key;
     */
    return 0;
  }

  /**
   * Takes the cipher without space, and the key length, and uses the dot product
   * with the English language frequencies to compute the shifting for each letter
   * of the key
   * 
   * @param cipher    the byte array representing the encoded text without space
   * @param keyLength the length of the key we want to find
   * @return the inverse key to decode the Vigenere cipher text
   */
  public static byte[] vigenereFindKey(List<Byte> cipher, int keyLength) {
    // TODO : COMPLETE THIS METHOD
    return null; // TODO: to be modified
  }

  // -----------------------Basic CBC-------------------------

  /**
   * Method used to decode a String encoded following the CBC pattern
   * 
   * @param cipher the byte array representing the encoded text
   * @param iv     the pad of size BLOCKSIZE we use to start the chain encoding
   * @return the clear text
   */
  public static byte[] decryptCBC(byte[] cipher, byte[] iv) {
    int blockLength = iv.length;
    byte[] decihperedText = new byte[cipher.length];
    byte[] key = Arrays.copyOf(iv, blockLength);
    int iterationsRequired = (int) Math.ceil((double) cipher.length / blockLength);
    int currentIteration = 0;
    while (currentIteration < iterationsRequired) {
      int startIndex = currentIteration * blockLength;
      int endIndex = (currentIteration + 1) * blockLength;
      byte[] decipheredPart = Arrays.copyOfRange(cipher, startIndex, endIndex);
      decipheredPart = Encrypt.oneTimePad(decipheredPart, key);
      key = Arrays.copyOfRange(cipher, startIndex, endIndex);
      for (int i = startIndex; i < decihperedText.length; ++i) {
        decihperedText[i] = decipheredPart[i % blockLength];
      }
      ++currentIteration;
    }

    return decihperedText;
  }

}
