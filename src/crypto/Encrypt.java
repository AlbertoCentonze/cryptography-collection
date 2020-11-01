package crypto;

import java.util.Random;
import java.util.Arrays;
import static crypto.Helper.*;

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
      // TODO check if it's correct
      case CAESAR:
        return Helper.bytesToString(caesar(Helper.stringToBytes(message), Helper.stringToBytes(key)[0]));
      case VIGENERE:
        return Helper.bytesToString(vigenere(Helper.stringToBytes(message), Helper.stringToBytes(key)));
      case XOR:
        return Helper.bytesToString(xor(Helper.stringToBytes(message), Helper.stringToBytes(key)[0]));
      case ONETIME:
        return Helper.bytesToString(oneTimePad(Helper.stringToBytes(message), Helper.stringToBytes(key)));
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
    assert (plainText != null);

    // Création tableau qui va changer les byte
    byte cipher[] = new byte[plainText.length];

    // Remplissage du tableau
    for (int i = 0; i < plainText.length; i++) {

      if (spaceEncoding) // si l'on code l'espace " "
        cipher[i] = (byte) (plainText[i] + key);

      else { // si l'on ne code pas les epaces
        switch (plainText[i]) {
          case SPACE:
            cipher[i] = SPACE;
            break;
          default:
            cipher[i] = (byte) (plainText[i] + key);
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
    byte[] cipheredText = Arrays.copyOf(plainText, plainText.length);

    for (int i = 0; i < cipheredText.length; i++) {
      if (spaceEncoding) {
        switch (plainText[i]) {
          case SPACE:
            cipheredText[i] = SPACE;
            break;
          default:
            cipheredText[i] = (byte) (plainText[i] ^ key);
        }
      } else {
        cipheredText[i] = (byte) (plainText[i] ^ key);
      }
    }

    return cipheredText;
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

    // Création tableau qui va changer les byte
    byte cipher[] = new byte[plainText.length];

    // Remplissage du tableau
    if (spaceEncoding) { // si l'on code l'espace " "
      for (int i = 0; i < plainText.length; i++) {
        int keywordPointer = i % keyword.length;

        cipher[i] = (byte) (plainText[i] + keyword[keywordPointer]);
      }
    } else { // si l'on ne code pas les epaces
      for (int i = 0; i < plainText.length; i++) {
        int keywordPointer = i % keyword.length;
        switch (plainText[i]) {
          case SPACE:
            cipher[i] = SPACE;
            break;
          default:
            cipher[i] = (byte) (plainText[i] + keyword[keywordPointer]);
            break;
        }
      }
    }

    return cipher;
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
    byte[] cipheredText = Arrays.copyOf(plainText, plainText.length);

    for (int i = 0; i < cipheredText.length; i++) {
      cipheredText[i] = (byte) (plainText[i] ^ pad[i]);
    }

    return cipheredText;
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
    // TODO: COMPLETE THIS METHOD

    return null; // TODO: to be modified
  }

  /**
   * Generate a random pad/IV of bytes to be used for encoding
   * 
   * @param size the size of the pad
   * @return random bytes in an array
   */
  public static byte[] generatePad(int size) {
    byte[] randomByteSequence = new byte[size];
    for (int i = 0; i < size; ++i) {
      randomByteSequence[i] = (byte) (LOWER_BOUND + rand.nextInt(256));
    }
    return randomByteSequence;
  }

}
