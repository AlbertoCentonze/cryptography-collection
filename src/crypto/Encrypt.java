package crypto;

import java.util.Random;
import java.util.Arrays;

public class Encrypt {

  public static final int CAESAR = 0;
  public static final int VIGENERE = 1;
  public static final int XOR = 2;
  public static final int ONETIME = 3;
  public static final int CBC = 4;

  public static final byte SPACE = 32;

  public static final int LOWER_BOUND = -128;

  final static Random rand = new Random();

  // -----------------------General-------------------------

  /**
   * General method to encode a message using a key, you can choose the method you
   * want to use to encode.
   * 
   * @param message the message to encode already cleaned
   * @param key     the key used to encode
   * @param type    the method used to encode : 0 = Caesar, 1 = Vigenere, 2 = XOR,
   *                3 = One time pad, 4 = CBC
   * 
   * @return an encoded String if the method is called with an unknown type of
   *         algorithm, it returns the original message
   */
  public static String encrypt(String message, String key, int type) {
    switch (type) {
      case CAESAR:
        return Helper.bytesToString(caesar(Helper.stringToBytes(message), Helper.stringToBytes(key)[0]));
      case VIGENERE:
        return Helper.bytesToString(vigenere(Helper.stringToBytes(message), Helper.stringToBytes(key)));
      case XOR:
        return Helper.bytesToString(xor(Helper.stringToBytes(message), Helper.stringToBytes(key)[0]));
      case ONETIME:
        byte[] encoded = oneTimePad(Helper.stringToBytes(message), Helper.stringToBytes(key));
        if (encoded == null)
          return "";
        else
          return Helper.byteArrayToString(encoded);
      case CBC:
        return Helper.bytesToString(cbc(Helper.stringToBytes(message), Helper.stringToBytes(key)));
    }
    return message;
  }

  // -----------------------Caesar-------------------------

  /**
   * Method to encode a byte array message using a single character key the key is
   * simply added to each byte of the original message
   * 
   * @param plainText     The byte array representing the string to encode
   * @param key           the byte corresponding to the char we use to shift
   * @param spaceEncoding if false, then spaces are not encoded
   * @return an encoded byte array
   */
  public static byte[] caesar(byte[] plainText, byte key, boolean spaceEncoding) {
    assert plainText != null;
    assert plainText.length != 0;

    // new array to store the result
    byte cipher[] = new byte[plainText.length];

    // for cycle that adds the key to the original byte
    for (int i = 0; i < plainText.length; i++) {

      if (spaceEncoding) // version with space encoding
        cipher[i] = (byte) (plainText[i] + key);

      else { // version without space encoding
        switch (plainText[i]) {
          case SPACE:
            cipher[i] = SPACE;
            break;
          default:
            cipher[i] = (byte) (plainText[i] + key);
            break;
        }
      }
    }

    return cipher;

  }

  /**
   * Method to encode a byte array message using a single character key the key is
   * simply added to each byte of the original message spaces are not encoded
   * 
   * @param plainText The byte array representing the string to encode
   * @param key       the byte corresponding to the char we use to shift
   * @return an encoded byte array
   */
  public static byte[] caesar(byte[] plainText, byte key) {
    return caesar(plainText, key, false);
  }

  // -----------------------XOR-------------------------

  /**
   * Method to encode a byte array using a XOR with a single byte long key
   * 
   * @param plaintext     the byte array representing the string to encode
   * @param key           the byte we will use to XOR
   * @param spaceEncoding if false, then spaces are not encoded
   * @return an encoded byte array
   */

  public static byte[] xor(byte[] plainText, byte key, boolean spaceEncoding) {
    // array that will store the result, deep copy of the argument plain text
    assert plainText != null;
    assert plainText.length != 0;

    byte[] cipherText = Arrays.copyOf(plainText, plainText.length);

    // cycle that apply xor with the bitwise operator to every byte in cipherText
    for (int i = 0; i < cipherText.length; i++) {
      if (spaceEncoding) { // with space encoding
        cipherText[i] = (byte) (plainText[i] ^ key);
      } else {
        switch (plainText[i]) { // without space encoding
          case SPACE:
            cipherText[i] = SPACE;
            break;
          default:
            cipherText[i] = (byte) (plainText[i] ^ key);
        }
      }
    }

    return cipherText;
  }

  /**
   * Method to encode a byte array using a XOR with a single byte long key spaces
   * are not encoded
   * 
   * @param plaintext the byte array representing the string to encode
   * @param key       the byte we will use to XOR
   * @return an encoded byte array
   */
  public static byte[] xor(byte[] plainText, byte key) {
    return xor(plainText, key, false);
  }
  // -----------------------Vigenere-------------------------

  /**
   * Method to encode a byte array using a byte array keyword The keyword is
   * repeated along the message to encode The bytes of the keyword are added to
   * those of the message to encode
   * 
   * @param plainText     the byte array representing the message to encode
   * @param keyword       the byte array representing the key used to perform the
   *                      shift
   * @param spaceEncoding if false, then spaces are not encoded
   * @return an encoded byte array
   */
  public static byte[] vigenere(byte[] plainText, byte[] keyword, boolean spaceEncoding) {
    assert (plainText != null);
    assert (plainText.length != 0);
    assert (keyword != null);
    assert (keyword.length != 0);

    // empty array to store the result
    byte cipherText[] = new byte[plainText.length];

    if (spaceEncoding) { // with space encoding
      for (int i = 0; i < plainText.length; i++) {
        int keywordPointer = i % keyword.length; // tells which part of the key to use
        cipherText[i] = (byte) (plainText[i] + keyword[keywordPointer]); // basically caesar
      }
    } else { // without space encoding
      int encodedLettersCounter = 0; // keep track only of encoded characters that are not 32
      for (int i = 0; i < plainText.length; i++) {
        int keywordPointer = encodedLettersCounter % keyword.length; // tells which part of the key to use
        switch (plainText[i]) {
          case SPACE:
            cipherText[i] = SPACE;
            break;
          default:
            cipherText[i] = (byte) (plainText[i] + keyword[keywordPointer]); // basically caesar
            ++encodedLettersCounter;
            break;
        }
      }
    }

    return cipherText;
  }

  /**
   * Method to encode a byte array using a byte array keyword The keyword is
   * repeated along the message to encode spaces are not encoded The bytes of the
   * keyword are added to those of the message to encode
   * 
   * @param plainText the byte array representing the message to encode
   * @param keyword   the byte array representing the key used to perform the
   *                  shift
   * @return an encoded byte array
   */
  public static byte[] vigenere(byte[] plainText, byte[] keyword) {
    return vigenere(plainText, keyword, false);
  }

  // -----------------------One Time Pad-------------------------

  /**
   * Method to encode a byte array using a one time pad of the same length. The
   * method XOR them together.
   * 
   * @param plainText the byte array representing the string to encode
   * @param pad       the one time pad
   * @return an encoded byte array
   */
  public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
    assert plainText.length == pad.length;
    assert plainText != null;
    assert pad != null;
    assert plainText.length != 0;
    assert pad.length != 0;

    // deepcopy of plaintext to get the desired result
    byte[] cipherText = Arrays.copyOf(plainText, plainText.length);

    for (int i = 0; i < cipherText.length; i++) {
      cipherText[i] = (byte) (plainText[i] ^ pad[i]); // basically xor
    }

    return cipherText;
  }

  // -----------------------Basic CBC-------------------------

  /**
   * Method applying a basic chain block counter of XOR without encryption method.
   * Encodes spaces.
   * 
   * @param plainText the byte array representing the string to encode
   * @param iv        the pad of size BLOCKSIZE we use to start the chain encoding
   * @return an encoded byte array
   */
  public static byte[] cbc(byte[] plainText, byte[] iv) {
    assert plainText != null;
    assert plainText.length != 0;
    assert iv != null;
    assert iv.length != 0;

    int blockLength = iv.length; // a variable to keep track of lentgh of every block
    byte[] cihperedText = new byte[plainText.length]; // empty array to store every crytpted block
    byte[] key = Arrays.copyOf(iv, blockLength); // variable that keep track of the key
    int iterationsRequired = (int) Math.ceil((double) plainText.length / blockLength); // the number of iteration
                                                                                       // required to encrypt
    int currentIteration = 0; // just a counter to stop when it's done

    while (currentIteration < iterationsRequired) { // cycle that encrypt every block
      int startIndex = currentIteration * blockLength; // determine the start of each block
      int endIndex = (currentIteration + 1) * blockLength; // determine the end of each block
      byte[] cipheredPart = Arrays.copyOfRange(plainText, startIndex, endIndex); // cuts the array to encrypt just the
                                                                                 // block
      cipheredPart = Encrypt.oneTimePad(cipheredPart, key); // encrypt the block with otp
      key = cipheredPart; // updates the key with the new ciphered block
      for (int i = startIndex; i < cihperedText.length; ++i) { // store the encrypted block into cipheredText
        cihperedText[i] = cipheredPart[i % blockLength];
      }
      ++currentIteration;
    }

    return cihperedText;
  }

  /**
   * Generate a random pad/IV of bytes to be used for encoding
   * 
   * @param size the size of the pad
   * @return random bytes in an array
   */
  public static byte[] generatePad(int size) {
    assert size != 0;

    byte[] randomByteSequence = new byte[size];
    for (int i = 0; i < size; ++i) {
      randomByteSequence[i] = (byte) (LOWER_BOUND + rand.nextInt(256)); // just a random number from -128 to 127
    }
    return randomByteSequence;
  }
}
