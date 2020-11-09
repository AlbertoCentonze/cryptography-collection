package crypto;

import java.util.Scanner;

public class Shell {
  private static Scanner keyboard = new Scanner(System.in);

  public static String byteArrayToString(byte[] array) {
    String output = "";
    for (byte element : array) {
      output += element;
      output += " ";
    }

    return output;
  }

  public static void main(String args[]) {

    // Demande à l'utilisateur s'il veut coder ou décoder un message
    String welcome = "If you need help understanding how the shell works (Write help) " + System.lineSeparator()
        + "Do you want to code a message or to decode a message ? (Write code or decode) : ";
    String[] whatToDo = { "encrypt", "decrypt", "help" };
    int choice = askSomething(welcome, whatToDo);

    if (choice == 2) { // help
      System.out.println(
          " At the start of the program you will choose whether encrypting a message or decrypting a message. You will be able to write this message (in lowercase letters) and choose between several different techniques."
              + System.lineSeparator() + System.lineSeparator() + "If you code: " + System.lineSeparator() + "- Caesar"
              + System.lineSeparator() + "- Vigenere " + System.lineSeparator() + "- Xor" + System.lineSeparator()
              + "- One-Time Pad (OTP)" + System.lineSeparator() + "- Cipher Block Chaining (CBC)"
              + System.lineSeparator() + System.lineSeparator() + "If you decode:" + System.lineSeparator()
              + "- Caesar Brute Force" + System.lineSeparator() + "- Caesar With Frequencies" + System.lineSeparator()
              + "- Xor Brute Force" + System.lineSeparator() + "- Vigenere With Frequencies" + System.lineSeparator()
              + "- Decrypt CBC" + System.lineSeparator() + System.lineSeparator()
              + "If you choose to encode a message, you will have to give the key. " + System.lineSeparator()
              + "Depending on the selected encryption technique, you may have to choose a key with multiple variables. To do so, you will choose the size of the key, and then all the variables ."
              + System.lineSeparator()
              + "For the decryption of the CBC, it will be required to enter the first pad used to encode the message."
              + System.lineSeparator() + System.lineSeparator() + "The result will then be displayed. ");
      return;
    } else if (choice == 0) { // encrypt

      // Demande le message à l'utilisateur, demande tant qu'il n'est pas null.
      String askinsert = "Write the code you want to encrypt : (You have to write it in lower case letters) ";
      String message = insertText(askinsert);
      byte[] messageByte = Helper.stringToBytes(message);

      // Savoir avec quelle methode, demande tant que la méthode est incorrecte
      String askWichMethod = " Your message can be coded with Caesar, Vigenere, Xor, Onetime or CBC. Which one do you want ? (Write it in capital letters, ex : Xor => XOR) : ";
      String[] wichMethod = { "CAESAR", "VIGENERE", "XOR", "ONETIME", "CBC" };
      int methode = askSomething(askWichMethod, wichMethod);

      // Avec CAESAR
      if (methode == 0) {
        byte key = (byte) keylengthOf1();
        byte[] encodedMessage = Encrypt.caesar(messageByte, key);
        String stringResult = Helper.bytesToString(encodedMessage);
        String byteResult = byteArrayToString(encodedMessage);
        System.out.println("The encoded message is : " + stringResult);
        System.out.println("The encoded message (in byte) is : " + byteResult);

      }
      // Avec VIGENERE
      else if (methode == 1) {

        int keySize = keySize();
        byte tab[] = keyTab(keySize);
        byte[] encodedMessage = Encrypt.vigenere(messageByte, tab);
        output(encodedMessage);

      }
      // AvecXOR
      else if (methode == 2) {
        byte key = (byte) keylengthOf1();
        byte[] encodedMessage = Encrypt.xor(messageByte, key);
        output(encodedMessage);

      }
      // Avec ONETIME
      else if (methode == 3) {

        byte tab[] = keyOneTimePad(message);
        byte[] encodedMessage = Encrypt.oneTimePad(messageByte, tab);
        output(encodedMessage);
      }
      // Avec CBC
      else if (methode == 4) {

        int keySize = keySize();
        byte tab[] = keyTab(keySize);
        byte[] encodedMessage = Encrypt.cbc(messageByte, tab);
        output(encodedMessage);
      }
    }

    else if (choice == 1) { // decrypt

      // Demande le message à l'utilisateur, demande tant qu'il n'est pas null.
      String askinsert = "Write the code you want to decrypt : ";
      String message = insertText(askinsert);
      byte[] messageByte = Helper.stringToBytes(message);

      String askWichMethodDecode = "Your message can be decoded with CaesarBruteForce, CaesarWithFrequencies, XorBruteForce, VigenereWithFrequencies or DecryptCBC. Which one do you want ? (Write it in lowercase letter, ex : DecryptCBC => decryptcbc) : ";
      String[] wichMethodDecode = { "caesarbruteforce", "caesarwithfrequencies", "xorbruteforce",
          "vigenerewithfrequencies", "decryptcbc" };
      int methodeDecode = askSomething(askWichMethodDecode, wichMethodDecode);

      // Avec caesarbruteforce
      if (methodeDecode == 0) {
        byte[][] result = Decrypt.caesarBruteForce(messageByte);
        String resultString = "";
        for (byte[] possibleLine : result) {
          resultString += Helper.bytesToString(possibleLine);
          resultString += System.lineSeparator();
        }
        Helper.writeStringToFile(resultString, "bruteForceCaesarResult.txt");
      }

      // Avec caesarwithfrequencies
      else if (methodeDecode == 1) {
        byte key = Decrypt.caesarWithFrequencies(messageByte);
        byte[] decodedMessage = Encrypt.caesar(messageByte, key);
        String result = byteArrayToString(decodedMessage);
        System.out.println("The decoded message is : " + result);
      }

      // Avec xorbruteforce
      else if (methodeDecode == 2) {
        byte[][] result = Decrypt.xorBruteForce(messageByte);
        String resultString = "";
        for (byte[] possibleLine : result) {
          resultString += Helper.bytesToString(possibleLine);
          resultString += System.lineSeparator();
        }
        Helper.writeStringToFile(resultString, "bruteForceXorResult.txt");
      }

      // Avec vigenerewithfrequencies
      else if (methodeDecode == 3) {
        byte[] key = Decrypt.vigenereWithFrequencies(messageByte);
        byte[] decodedMessage = Encrypt.vigenere(messageByte, key);
        String result = byteArrayToString(decodedMessage);
        System.out.println("The decoded message is : " + result);
      }
      // Avec decryptcbc
      else if (methodeDecode == 4) {
        int keySize = keySize();
        byte tab[] = keyTab(keySize);
        byte[] decodedMessage = Decrypt.decryptCBC(messageByte, tab);
        String result = byteArrayToString(decodedMessage);
        System.out.println("The decoded message is : " + result);
      }
    }
  }

  public static int askSomething(String message, String[] options) {
    boolean correctAnswer = false;
    int answer = -1;
    while (!correctAnswer) {

      System.out.println(message);
      System.out.println("Write the number corresponding to your choice");
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

    int choice = askSomething("choose an option", new String[] { "load message from file", "insert message manually" });
    if (choice == 0) {
      System.out.println("In order to load some text from a file you have to put it inside /src/load.txt");
      int file = askSomething("is your text already there?", new String[] { "yes", "no" });
      if (file == 0) {
        text = Helper.readStringFromFile("load.txt");
        System.out.println("Text loaded correctly");
        System.out.println(text);
        return text;
      }
      if (file == 1) {
        System.out.println("put it there and retry then");
        return insertText(message);
      } else {
        return insertText(message);
      }

    } else if (choice == 1) {
      System.out.println(message);
      while (text.equals("")) {
        text = keyboard.nextLine();
      }
      return text;
    } else {
      return insertText(message);
    }
  }

  public static int keylengthOf1() {
    System.out.println("What is the key you want to use (Write a number) ? : ");
    int key = keyboard.nextInt();
    return key;
  }

  public static int keySize() {
    System.out.println("What is the size of your key ? : ");
    int size = keyboard.nextInt();
    return size;
  }

  public static byte[] keyTab(int keySize) {
    System.out.println("The size of your key is : " + keySize);
    System.out.println("You have to put " + keySize + " number(s) to fill up your key (write numbers) : ");

    byte tabKey[] = new byte[keySize];
    for (int i = 0; i < keySize; ++i) {
      System.out.println();
      tabKey[i] = (byte) keyboard.nextInt();
    }
    return tabKey;
  }

  public static byte[] keyOneTimePad(String message) {
    System.out.println("The size of your message is : " + message.length());
    System.out.println("You have to put one key number per letter of your message (write numbers) : ");
    System.out.println("Press enter after inserting every single number");

    byte tabKey[] = new byte[message.length()];
    for (int i = 0; i < message.length(); ++i) {
      System.out.println();
      tabKey[i] = (byte) keyboard.nextInt();
    }
    return tabKey;
  }

  public static void output(byte[] cipherText) {
    System.out.println("Your message in byte is : " + byteArrayToString(cipherText));
    String cipherTextString = Helper.bytesToString(cipherText);
    System.out.println("Your message is : " + cipherTextString);
    Helper.writeStringToFile(cipherTextString, "output.txt");
    System.out.println("The result was correctly stored into output.txt");
  }
}
