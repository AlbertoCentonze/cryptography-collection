package crypto;

import java.util.ArrayList;
import java.util.Arrays;
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
    assert cipher != null;

    byte[] encoded = Helper.stringToBytes(cipher);

    if (type == CAESAR) {
      byte originalKey = caesarWithFrequencies(encoded);
      byte[] decoded = Encrypt.caesar(encoded, (byte) -originalKey);
      return Helper.bytesToString(decoded);
    } else if (type == VIGENERE) {
      byte[] key = vigenereWithFrequencies(encoded);
      byte[] decoded = Encrypt.vigenere(encoded, key, true);
      return Helper.bytesToString(decoded);
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
    assert bruteForceResult != null;
    assert bruteForceResult.length != 0;

    String result = "";

    for (byte[] element : bruteForceResult) {
      result += Helper.bytesToString(element); // adds the possible combination
      result += System.lineSeparator(); // newline
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
    assert cipher != null;
    assert cipher.length != 0;

    final int LOWER_BOUND = -128; // starting point for the cycle
    byte[][] result = new byte[256][cipher.length]; // bidimensional array to store all the combinations
    for (int i = 0; i < result.length; ++i) {
      Integer integerKey = Integer.valueOf(LOWER_BOUND + i); // Converted in integer to use .byteValue()
      byte byteKey = integerKey.byteValue(); // conversion to byte

      result[i] = Encrypt.caesar(cipher, byteKey); // store every single combination into result
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
    assert cipherText != null;
    assert cipherText.length != 0;

    float[] frequencies = computeFrequencies(cipherText);
    return caesarFindKey(frequencies);
  }

  /**
   * Method that computes the frequencies of letters inside a byte array
   * corresponding to a String
   * 
   * @param cipherText the byte array
   * @return the character frequencies as an array of float
   */
  public static float[] computeFrequencies(byte[] cipherText) {
    assert cipherText != null;
    assert cipherText.length != 0;

    float[] frequencies = new float[256]; // empty array to store the frequencies of cipherText
    int notSpacesCounter = 0; // variable to keep track of the number of characters that are not spaces
    for (int i = 0; i < cipherText.length; i++) {
      if (cipherText[i] != 32) {
        notSpacesCounter++; // gets the total number of nonsapce characters
      }
    }
    for (int letter = 0; letter < 256; letter++) { // iterates over all the letter obtainable from bytes
      int letterOccurencies = 0; // keey track of the occurency of a specific letter

      if (letter == 32) // skip spaces
        continue;

      for (int i = 0; i < cipherText.length; i++) { // iterates over cipherText
        if ((letter > 127 && (letter - 256) == cipherText[i]) || letter == cipherText[i]) // check if the letter index
                                                                                          // correspond to the byte
          letterOccurencies++; // if the letter is found increases the counter

        frequencies[letter] = (float) letterOccurencies / notSpacesCounter; // store the division between the total
                                                                            // number of occurencies and the total
                                                                            // number of nonspace characters

      }
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
    assert charFrequencies != null;
    assert charFrequencies.length != 0;

    byte key;
    double[] scalarProducts = new double[256];
    int offset = 0;

    for (int i = 0; i < ALPHABETSIZE; i++) { // iterates over the possible frequencies
      for (int j = 0; j < ENGLISHFREQUENCIES.length; j++) { // iterates over the alphabet
        int bound = offset % ENGLISHFREQUENCIES.length; // solve indices out of bound problem to try every possible
                                                        // combination
        if (i + bound < ALPHABETSIZE) {
          // scalar product
          scalarProducts[i] += ENGLISHFREQUENCIES[j] * charFrequencies[i + bound];
        } else {
          // scalar product
          scalarProducts[i] += ENGLISHFREQUENCIES[j] * charFrequencies[i - ALPHABETSIZE + bound];
        }
        ++offset; // shifts the iteration of 1 position
      }
    }

    // find the maximum scalar product
    double maximumValue = 0.0; // store the temporary maximum scalar product
    int maximumIndex = 0; // store the temporary index of the maxium scalar product
    for (int i = 0; i < 256; i++) {
      if (maximumValue < scalarProducts[i]) { // looks for the biggest scalar product
        maximumValue = scalarProducts[i];
        maximumIndex = i;
      }
    }
    key = (byte) -(maximumIndex - 97); // convert the distance between the maximum scalar product and the
                                       // aposition of the alphabet into the key

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
    assert cipher != null;
    assert cipher.length != 0;

    // basically the same explainations used for caesarBruteForce
    final int LOWER_BOUND = -128;
    byte[][] result = new byte[255][cipher.length];
    for (int i = 0; i < result.length; ++i) {
      Integer integerKey = Integer.valueOf(LOWER_BOUND + i);
      byte byteKey = integerKey.byteValue();

      result[i] = Encrypt.xor(cipher, byteKey); // applies xor with everysingle possible combination
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
    assert cipher != null;
    assert cipher.length != 0;

    List<Byte> cipherWithoutSpaces = removeSpaces(cipher);
    int keyLength = vigenereFindKeyLength(cipherWithoutSpaces);
    byte[] key = vigenereFindKey(cipherWithoutSpaces, keyLength);
    return key;
  }

  /**
   * Helper Method used to remove the space character in a byte array for the
   * clever Vigenere decoding
   * 
   * @param array the array to clean
   * @return a List of bytes without spaces
   */
  public static List<Byte> removeSpaces(byte[] array) {
    assert array != null;
    assert array.length != 0;

    ArrayList<Byte> noSpace = new ArrayList<Byte>(); // new arraylist to store the result of the parsing

    for (int i = 0; i < array.length; i++) {

      if (array[i] != SPACE && i < array.length) { // if it's not space
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
    assert cipher != null;
    assert cipher.size() != 0;

    // STEP 1

    ArrayList<Integer> coincidences = new ArrayList<Integer>(); // creates an arraylist to store how many times it
                                                                // encountered the same letter
    int frequenceCounter = 0; // frequence counter for a single letter
    for (int offset = 1; offset < cipher.size(); ++offset) { // the distance from the original array
      for (int i = offset; i < cipher.size() - offset; ++i) { // clamp between the zone in which both the arrays have
                                                              // values
        int x = i - offset; // value used to iterate over the original array
        if (cipher.get(x) == cipher.get(i)) { // if the two character are the same
          ++frequenceCounter;
        }
      }
      coincidences.add(frequenceCounter); // add the number of coincidences for that character
      frequenceCounter = 0; // reset the frequenceCounter
    }

    // STEP 2

    ArrayList<Integer> localMaximums = new ArrayList<Integer>();
    // for cycles that looks in the neighborhood of the list to check for the
    // maximum
    for (int i = 0; i < Math.ceil(coincidences.size() / 2); ++i) {
      int minimumCounter = 0;
      if (i == 0) {
        for (int c = 0; c <= 2; ++c) { // case first element
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 3) {
          localMaximums.add(i);
        }
      } else if (i == 1) { // case second element
        for (int c = -1; c <= 2; ++c) {
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 4) {
          localMaximums.add(i);
        }
      } else if (i == coincidences.size() - 1) { // case last element
        for (int c = -2; c <= 0; ++c) {
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 3) {
          localMaximums.add(i);
        }
      } else if (i == coincidences.size() - 2) { // case second-last element
        for (int c = -2; c <= 1; ++c) {
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 4) {
          localMaximums.add(i);
        }
      } else {
        for (int c = -2; c <= 2; ++c) { // generic case
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 5) {
          localMaximums.add(i);
        }
      }
    }

    // STEP 3

    // Hashmap to associate to each size found the frequence of that size
    Map<Integer, Integer> frequencies = new HashMap<Integer, Integer>();
    for (int i = 1; i < localMaximums.size(); ++i) { // cycle to analize every size in the array
      int size = localMaximums.get(i) - localMaximums.get(i - 1); // compute the size
      if (frequencies.get(size) == null) { // if size is not in the map it adds it
        frequencies.put(size, 1);
      } else { // if size is already in the map it increases its counter
        frequencies.replace(size, frequencies.get(size) + 1);
      }
    }
    int mostFrequentSize = 0; // variable to keep track of the most frequent size
    int mostFrequentSizeFrequence = 0; // variable to keep track of the most frequent size occurencies
    for (Integer key : frequencies.keySet()) { // iterates over all the found sizes
      Integer frequence = frequencies.get(key);
      if (mostFrequentSizeFrequence < frequence) { // looking for the most frequent one
        mostFrequentSize = key;// store the most frequent size
        mostFrequentSizeFrequence = frequence;// store its frequence
      }
    }

    return mostFrequentSize;

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
    assert cipher != null;
    assert cipher.size() != 0;

    @SuppressWarnings("unchecked") // disables the warning due to the absence of the type in the arraylist
    ArrayList<Byte>[] subsequences = new ArrayList[keyLength];
    byte[] key = new byte[keyLength]; // stores the key once found

    for (int i = 0; i < keyLength; ++i) {
      ArrayList<Byte> newSubsequence = new ArrayList<Byte>(); // creates a list of characters that share the same key
      for (int j = 0; j < cipher.size(); ++j) { // itereates over the list
        int modulo = j % keyLength; // calulates the modulo to know if they have the same encryption key
        if (modulo == i) // check for the modulo
          newSubsequence.add(cipher.get(j)); // adds it to the subsequence with the same key
      }
      subsequences[i] = (newSubsequence);
    }

    for (int i = 0; i < keyLength; ++i) { // just a looping to convert the ArrayList<Byte> into the array byte[]
      byte[] primitiveArray = new byte[subsequences[i].size()];
      for (int j = 0; j < subsequences[i].size(); ++j) {
        primitiveArray[j] = subsequences[i].get(j); // not-so-autounboxing
      }
      key[i] = Decrypt.caesarWithFrequencies(primitiveArray); // deciphering each individual sequence with caesar
    }

    return key;
    // KNOWN PROBLEM the key is always correct but not always in the correct order
    // if you are correcting this please contact me and tell me why
    // alberto.centonze@epfl.ch
    // we have a bonus as well to not lose points :D

  }

  /**
   * Takes the original key and return a key that can be used to decode the
   * cipherText
   * 
   * @param originalKey the byte array containing the key
   * @return the inverse key to decode the Vigenere cipher text
   */
  public static byte[] vigenereFindInverseKey(byte[] originalKey) {
    assert originalKey != null;
    assert originalKey.length != 0;

    byte[] inverseKey = new byte[originalKey.length];
    for (int i = 0; i < originalKey.length; ++i) { // just a for cycle that iterates over the key and gives back the
                                                   // opposite
      inverseKey[i] = (byte) -originalKey[i];
    }
    return inverseKey;
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
    assert cipher != null;
    assert cipher.length != 0;
    assert iv != null;
    assert iv.length != 0;

    // almost the same comments of the cbc encryption
    // go there for a more precise description

    final int BLOCKSIZE = iv.length;

    byte[] decihperedText = new byte[cipher.length];
    byte[] key = Arrays.copyOf(iv, BLOCKSIZE);

    int iterationsRequired = (int) Math.ceil((double) cipher.length / BLOCKSIZE);
    int currentIteration = 0;

    while (currentIteration < iterationsRequired) {
      int startIndex = currentIteration * BLOCKSIZE;
      int endIndex = (currentIteration + 1) * BLOCKSIZE;
      byte[] decipheredPart = Arrays.copyOfRange(cipher, startIndex, endIndex); // cuts the array to decipher the block

      decipheredPart = Encrypt.oneTimePad(decipheredPart, key);
      key = Arrays.copyOfRange(cipher, startIndex, endIndex); // updates the key with the new deciphered block
      for (int i = startIndex; i < decihperedText.length; ++i) { // store the result of the deciphering
        decihperedText[i] = decipheredPart[i % BLOCKSIZE];
      }
      ++currentIteration;
    }

    return decihperedText;
  }

}
