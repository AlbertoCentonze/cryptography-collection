package crypto;

import java.util.Scanner;

public class Shell {
  private static Scanner keyboard = new Scanner(System.in);

  public static void main(String args[]) {
	  
	  
     // Demande à l'utilisateur s'il veut coder ou décoder un message
    String welcome = "If you need help understanding how the shell works (Write help) " + System.lineSeparator()
        + "Do you want to code a message or to decode a message ? (Write code or decode) : ";
    String[] whatToDo = { "encrypt", "decrypt", "help" };
    int choice = askSomething(welcome, whatToDo);

    
    
    
    if (choice == 2) { // help
      System.out.println(
          " At the start of the program we will let you choose whether you want to encode a message or decode a message. Then we will ask you to write this message (in letters). You will be able to choose between several different techniques."
              + System.lineSeparator() + System.lineSeparator() + "If you code: " + System.lineSeparator() + "- Caesar"
              + System.lineSeparator() + "- Vigenere " + System.lineSeparator() + "- Xor" + System.lineSeparator()
              + "- One-Time Pad (OTP)" + System.lineSeparator() + "- Cipher Block Chaining (CBC)"
              + System.lineSeparator() + System.lineSeparator() + "If you decode:" + System.lineSeparator()
              + "- Caesar Brute Force" + System.lineSeparator() + "- Xor" + System.lineSeparator() + "- CBC"
              + System.lineSeparator() + "- Clever deciphering of the Caesar" + System.lineSeparator()
              + "- Deciphering by Vigenère" + System.lineSeparator() + System.lineSeparator()
              
              + "In the case of message encoding, you will have to give the key, the size of which varies depending on the selected encryption technique."
              + System.lineSeparator()
              + "For the decryption of the CBC, it will be required to enter the first pad used to encode the message."
              + System.lineSeparator() + "The result will then be displayed. ");
      return;
    }
    else if (choice == 0) { // encrypt
    	
    	// Demande le message à l'utilisateur, demande tant qu'il n'est pas null.
    String askinsert = "Write the code you want to encrypt : ";
    String message = insertText(askinsert);    
    byte[] messageByte = Helper.stringToBytes(message);
    
      // Savoir avec quelle methode, demande tant que la méthode est incorrecte
    	String askWichMethod  = " Your message can be coded with Caesar, Vigenere, Xor, Onetime or CBC. Which one do you want ? (Write it in capital letters, ex : Xor => XOR) : ";
    	String[] wichMethod = {"CAESAR","VIGENERE","XOR","ONETIME","CBC"};
    	int methode = askSomething(askWichMethod, wichMethod);
   
      // Avec CAESAR
      if (methode == 0) {
        byte key = (byte)keylengthOf1();
        Encrypt.caesar(messageByte, key);
     
      }
      // Avec VIGENERE
      else if (methode == 1) {

        int keySize = keySize();
        byte tab[] = keyTab(keySize);
        Encrypt.vigenere(messageByte,tab);
       
      }
      // AvecXOR
      else if (methode == 2) {
        byte key = (byte) keylengthOf1();
        Encrypt.xor(messageByte, key);
       
      }
      // Avec ONETIME
      else if (methode == 3) {

        byte tab[] = keyOneTimePad(message);
        Encrypt.oneTimePad(messageByte, tab);
      }
      // Avec CBC
      else if (methode == 4) {

        int keySize = keySize();
        byte tab[] = keyTab(keySize);
        Encrypt.cbc(messageByte, tab);
      }
    }

    else if (choice == 1) { // decrypt
    	
    	// Demande le message à l'utilisateur, demande tant qu'il n'est pas null.
    
    	String askinsert = "Write the code you want to decrypt : ";     
        String message = insertText(askinsert);    
        byte[] messageByte = Helper.stringToBytes(message);
      //   do { }(while message != null);

    	String askWichMethodDecode ="Your message can be decoded with CaesarBruteForce, CaesarWithFrequencies, XorBruteForce, VigenereWithFrequencies or DecryptCBC. Which one do you want ? (Write it in lowercase letter, ex : DecryptCBC => decryptcbc) : ";    	
    	String [] wichMethodDecode = {"caesarbruteforce", "caesarwithfrequencies", "xorbruteforce", "vigenerewithfrequencies", "decryptcbc"};
    	int methodeDecode = askSomething(askWichMethodDecode, wichMethodDecode);    	   
    	
      // Avec caesarbruteforce
      if (methodeDecode == 0) {
    	  Decrypt.caesarBruteForce(messageByte);
      }
      // Avec caesarwithfrequencies
      else if (methodeDecode == 1) {
    	  Decrypt.caesarWithFrequencies(messageByte);
      }
      // Avec xorbruteforce
      else if (methodeDecode == 2) {
    	  Decrypt.xorBruteForce(messageByte);
      }
      // Avec vigenerewithfrequencies
      else if (methodeDecode == 3) {
    	  Decrypt.vigenereWithFrequencies(messageByte);
      }
      // Avec decryptcbc
      else if (methodeDecode == 4) {

        int keySize = keySize();

        byte tab[] = keyTab(keySize);
        Decrypt.decryptCBC(messageByte, tab);

      }

    }
  }

  public static int askSomething(String message, String[] options) {
    boolean correctAnswer = false;
    int answer = -1;
    while (!correctAnswer) {

      System.out.println(message);
      for (int i = 1; i <= options.length; ++i) {
        System.out.println(i + "  " + options[i - 1]);
      }
      answer = keyboard.nextInt() - 1;
      if (answer < options.length && answer >= 0) {
        correctAnswer = true;
      } else {
        System.out.println("The input was wrong, try again");
      }
    }
    return answer;
  }
  

  public static String insertText(String message) {
    String text = "";

    while (text.equals("")) {
      System.out.println(message);
      text = keyboard.nextLine();
    }

    return text;
  }


  public static int keylengthOf1() {

    // Savoir la clé, tant qu'elle n'est pas nulle
    System.out.println("What is the key you want to use (Write a number) ? : ");
    int key = keyboard.nextInt();
    return key;
  }

  public static int keySize() {

    // Savoir la clé, (tant qu'elle n'est pas nulle ??)
    System.out.println("What is the size of your key ? : ");
    int size = keyboard.nextInt();
    return size;
  }

  public static byte[] keyTab(int keySize) {

    // Creation d'untableau qui condiendra les clés
    System.out.println("The size of your key is : " + keySize);
    System.out.println("You have to put " +keySize+" number(s) to fill up your key (write numbers) : ");

    byte tabKey[] = new byte[keySize];
    for (int i = 0; i < keySize; ++i) {
      System.out.println();
      tabKey[i] = (byte)keyboard.nextInt();
    }

    return tabKey;
  }

  public static byte[] keyOneTimePad(String message) {

    // Une clé pour chque lettre (transformée en byte) du message
    System.out.println("The size of your message is : " + message.length());
    System.out.println("You have to put one key number per letter of your message (write numbers) : ");

    byte tabKey[] = new byte[message.length()];
    for (int i = 0; i < message.length(); ++i) {
      System.out.println();
      tabKey[i] = (byte)keyboard.nextInt();
    }

    return tabKey;
  }


  
}
