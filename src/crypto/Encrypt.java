package crypto;

import java.util.Random;
import static crypto.Helper.*;

public class Encrypt {
	
	public static final int CAESAR = 0;
	public static final int VIGENERE = 1;
	public static final int XOR = 2;
	public static final int ONETIME = 3;
	public static final int CBC = 4; 
	
	public static final byte SPACE = 32;
	
	final static Random rand = new Random();
	
	//-----------------------General-------------------------
	
	/**
	 * General method to encode a message using a key, you can choose the method you want to use to encode.
	 * @param message the message to encode already cleaned
	 * @param key the key used to encode
	 * @param type the method used to encode : 0 = Caesar, 1 = Vigenere, 2 = XOR, 3 = One time pad, 4 = CBC
	 * 
	 * @return an encoded String
	 * if the method is called with an unknown type of algorithm, it returns the original message
	 */
	public static String encrypt(String message, String key, int type) {
		// TODO: COMPLETE THIS METHOD
		
		return null; // TODO: to be modified
	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 * Method to encode a byte array message using a single character key
	 * the key is simply added to each byte of the original message
	 * @param plainText The byte array representing the string to encode
	 * @param key the byte corresponding to the char we use to shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key, boolean spaceEncoding) {
		assert(plainText != null);
		
		
		//Création tableau qui va changer les byte
		 byte tab[] = new byte[plainText.length];
		 	
		 //Remplissage du tableau 
		 for(int i = 0; i < plainText.length; i++) {
			 
			 
			 	if(spaceEncoding) //si l'on code l'espace "  "
          tab[i]= (byte) (plainText[i] + (byte)key);
			 	
			 	
			 	
			 	//Si l'addition de la clé dépasse les bornes, on recommence a -128 ou 127
			 	if((byte) (plainText[i] + (byte)key) > 127) {
			
			 		plainText[i]= (byte) (plainText[i] + (byte)key -256);
			
			 	} else if((plainText[i] + (byte)key) < -128){
			
			 		plainText[i]= plainText[i]= (byte) (plainText[i] + (byte)key + 256);
			 	}
				
							 	 			 				 	
        else { //si l'on ne code pas les epaces
          switch(plainText[i]){
            case (byte) 32:
              tab[i] = 32;
              break;
            default:
              tab[i] = (byte) (plainText[i] + (byte)key);
          }
			}
		 }
		 
		 //Valeur attendue pour le cipherText: {-101, 32, -87, -109, -96, -90}
			
		 assert(plainText != null);
			// TODO: COMPLETE THIS METHOD
			
			return tab; // TODO: to be modified
		
		
		
	}
	
	/**
	 * Method to encode a byte array message  using a single character key
	 * the key is simply added  to each byte of the original message
	 * spaces are not encoded
	 * @param plainText The byte array representing the string to encode
	 * @param key the byte corresponding to the char we use to shift
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key) {
		// TODO: COMPLETE THIS METHOD
		return caesar(plainText, key, true); // TODO: to be modified
	}
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to encode a byte array using a XOR with a single byte long key
	 * @param plaintext the byte array representing the string to encode
	 * @param key the byte we will use to XOR
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key, boolean spaceEncoding) {
		
		
		//création d'un tableau a deux dimensions : nombre de lettres (plainText.length) 
													//et nombre de bit que chaque lettre aura (égal à 8)
		byte tab [][]= new byte [plainText.length][8];
				
		//Remplissage du tableau
		for(int i =0; i<plainText.length;i++) {
			
			// Transformation de chaque nombre (mot)en bit 
			for(int j=0; j<8; j++) {
				
			//un mot = un tableau de 8 places rempli avec des 1 ou 0	
				do {									
				int calcul= plainText[i]/2;
	    		int résultat = plainText[i]%2;
	    	    tab [i][j]=(byte)résultat;
	    	
	    	}while(plainText[i]==0);
			
		}
		}
		
		
		
		// TODO: COMPLETE THIS METHOD
		return null; // TODO: to be modified
	}
	/**
	 * Method to encode a byte array using a XOR with a single byte long key
	 * spaces are not encoded
	 * @param key the byte we will use to XOR
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key) {
		// TODO: COMPLETE THIS METHOD
		return null; // TODO: to be modified
	}
	//-----------------------Vigenere-------------------------
	
	/**
	 * Method to encode a byte array using a byte array keyword
	 * The keyword is repeated along the message to encode
	 * The bytes of the keyword are added to those of the message to encode
	 * @param plainText the byte array representing the message to encode
	 * @param keyword the byte array representing the key used to perform the shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array 
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword, boolean spaceEncoding) {
		// TODO: COMPLETE THIS METHOD		
		return null; // TODO: to be modified
	}
	
	/**
	 * Method to encode a byte array using a byte array keyword
	 * The keyword is repeated along the message to encode
	 * spaces are not encoded
	 * The bytes of the keyword are added to those of the message to encode
	 * @param plainText the byte array representing the message to encode
	 * @param keyword the byte array representing the key used to perform the shift
	 * @return an encoded byte array 
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword) {
		// TODO: COMPLETE THIS METHOD
		return null; // TODO: to be modified
	}
	
	
	
	//-----------------------One Time Pad-------------------------
	
	/**
	 * Method to encode a byte array using a one time pad of the same length.
	 *  The method  XOR them together.
	 * @param plainText the byte array representing the string to encode
	 * @param pad the one time pad
	 * @return an encoded byte array
	 */
	public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
		// TODO: COMPLETE THIS METHOD
		return null; // TODO: to be modified
	}
	
	
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method applying a basic chain block counter of XOR without encryption method. Encodes spaces.
	 * @param plainText the byte array representing the string to encode
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return an encoded byte array
	 */
	public static byte[] cbc(byte[] plainText, byte[] iv) {
		// TODO: COMPLETE THIS METHOD
		
		return null; // TODO: to be modified
	}
	
	
	/**
	 * Generate a random pad/IV of bytes to be used for encoding
	 * @param size the size of the pad
	 * @return random bytes in an array
	 */
	public static byte[] generatePad(int size) {
		// TODO: COMPLETE THIS METHOD

		return null; // TODO: to be modified

	}
	
	
	
}
