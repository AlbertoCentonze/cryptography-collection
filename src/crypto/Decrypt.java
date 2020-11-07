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
    float[] frequencies = new float[256];
    int notSpacesCounter = 0;
    for (int i = 0; i < cipherText.length; i++) {
      if (cipherText[i] != 32) {
        notSpacesCounter++;
      }
    }
    for (int letter = 0; letter < 256; letter++) {
      int letterOccurencies = 0;

      if (letter == 32)
        continue;

      for (int i = 0; i < cipherText.length; i++) {
        if ((letter > 127 && (letter - 256) == cipherText[i]) || letter == cipherText[i])
          letterOccurencies++;

        frequencies[letter] = (float) letterOccurencies / notSpacesCounter;
      }
    }

    for (

        int x = 0; x < frequencies.length; ++x) {
      System.out.println(x + ": " + frequencies[x]);
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
    int offset = 0;

    for (int i = 0; i < ALPHABETSIZE; i++) {
      for (int j = 0; j < ENGLISHFREQUENCIES.length; j++) {
        int bound = offset % 26;
        if (i + bound < 256) {
          scalarProducts[i] += ENGLISHFREQUENCIES[j] * charFrequencies[i + bound];
        } else {
          scalarProducts[i] += ENGLISHFREQUENCIES[j] * charFrequencies[i - 256 + bound];
        }
        ++offset;
      }
    }

    double maximumValue = 0.0;
    int maximumIndex = 0;
    for (int i = 0; i < 256; i++) {
      if (maximumValue < scalarProducts[i]) {
        maximumValue = scalarProducts[i];
        maximumIndex = i;
      }
    }
    key = (byte) (maximumIndex - 97);
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
    List<Byte> cipherWithoutSpaces = removeSpaces(cipher);
    int keyLength = vigenereFindKeyLength(cipherWithoutSpaces);
    System.out.println("The length of the key is: " + keyLength);
    byte[] key = vigenereFindKey(cipherWithoutSpaces, keyLength);
    System.out.println("The key is: " + Helper.byteArrayToString(key));

    return null;
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

    // STEP 1

    ArrayList<Integer> coincidences = new ArrayList<Integer>();
    int frequenceCounter = 0;
    for (int offset = 1; offset < cipher.size(); ++offset) {
      for (int i = offset; i < cipher.size() - offset; ++i) {
        int x = i - offset;
        if (cipher.get(x) == cipher.get(i)) {
          // System.out.println("x " + x + " " + cipher.get(x) + " i " + i + " " +
          // cipher.get(i));
          ++frequenceCounter;
        }
      }
      coincidences.add(frequenceCounter);
      frequenceCounter = 0;
    }
    System.out.println(coincidences);

    // STEP 2

    ArrayList<Integer> localMaximums = new ArrayList<Integer>();
    for (int i = 0; i < Math.ceil(coincidences.size() / 2); ++i) {
      int minimumCounter = 0;
      if (i == 0) {
        for (int c = 0; c <= 2; ++c) {
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 3) {
          localMaximums.add(i);
        }
      } else if (i == 1) {
        for (int c = -1; c <= 2; ++c) {
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 4) {
          localMaximums.add(i);
        }
      } else if (i == coincidences.size() - 1) {
        for (int c = -2; c <= 0; ++c) {
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 3) {
          localMaximums.add(i);
        }
      } else if (i == coincidences.size() - 2) {
        for (int c = -2; c <= 1; ++c) {
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 4) {
          localMaximums.add(i);
        }
      } else {
        for (int c = -2; c <= 2; ++c) {
          if (coincidences.get(i + c) <= coincidences.get(i)) {
            ++minimumCounter;
          }
        }
        if (minimumCounter >= 5) {
          localMaximums.add(i);
          // TODO fix that
        }
      }
    }

    // STEP 3

    Map<Integer, Integer> frequencies = new HashMap<Integer, Integer>();
    for (int i = 1; i < localMaximums.size(); ++i) {
      int size = localMaximums.get(i) - localMaximums.get(i - 1);
      if (frequencies.get(size) == null) {
        frequencies.put(size, 1);
      } else {
        frequencies.replace(size, frequencies.get(size) + 1);
      }
    }
    int mostFrequentSize = 0;
    int mostFrequentSizeFrequence = 0;
    for (Integer key : frequencies.keySet()) {
      Integer frequence = frequencies.get(key);
      if (mostFrequentSizeFrequence < frequence) {
        mostFrequentSize = key;
        mostFrequentSizeFrequence = frequence;
      }
    }

    return mostFrequentSize;

  }

  /**
   * Method that compute the coincidence of an array of byte with itself shited of
   * a given offset for a Vigenere cipher text.
   * 
   * @param cipher the byte array representing the encoded text without space
   * @return return the coincidence table
   */

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
    byte[] key = new byte[keyLength];
    ArrayList<ArrayList<Byte>> subCiphers = new ArrayList<ArrayList<Byte>>();
    for (int i = 0; i < keyLength; ++i) {
      subCiphers.add(new ArrayList<Byte>());
    }

    for (int i = 0; i < cipher.size(); ++i) {
      int j = i % keyLength;
      subCiphers.get(j).add(cipher.get(i));
    }

    for (int i = 0; i < subCiphers.size(); ++i) {
      Byte[] classArray = subCiphers.get(i).toArray(new Byte[subCiphers.get(i).size()]);
      byte[] primitiveArray = new byte[classArray.length];
      for (int c = 0; c < classArray.length; ++c) {
        primitiveArray[c] = classArray[c]; // not-so-autoboxing
      }
      key[i] = Decrypt.caesarWithFrequencies(primitiveArray);
    }

    return key;
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
