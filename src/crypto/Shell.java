package crypto;

import java.util.Scanner;

public class Shell {
  private static Scanner keyboard = new Scanner(System.in);

  public static void main(String args[]) {

    // Demande à l'utilisateur s'il veut coder ou décoder un message
    String answer = asking();

    // Demande le message à l'utilisateur, demande tant qu'il n'est pas null.
    String message = message();

    String s1 = "code";
    String s2 = "decode";
    String s3 = "Help";
    int i = 0;

    
    if (answer.equals(s3)) {
      System.out.println(
          " At the start of the program we will let you choose whether you want to encode a message or decode a message. Then we will ask you to write this message (in letters). You will be able to choose between several different techniques." + System.lineSeparator()
              + System.lineSeparator() + "If you code: " + System.lineSeparator() + "- Caesar" + System.lineSeparator() + "- Vigenere "System.lineSeparator() + "- XorSystem.lineSeparator()
              + "- One-Time Pad (OTP)" + System.lineSeparator() + "- Cipher Block Chaining (CBC)System.lineSeparator() + System.lineSeparator()
              + "If you decode:"System.lineSeparator() + "- Caesar Brute ForceSystem.lineSeparator() + "- XorSystem.lineSeparator() + "- CBCSystem.lineSeparator()
              + "- Clever deciphering of the CaesarSystem.lineSeparator() + "- Deciphering by VigenèreSystem.lineSeparator() + System.lineSeparator()
              + "Dans la partie codage, vous pourrez pour certaine technique de chiffrement, choisir si oui ou non 0vous voulez coder les espaces présent dans votre message.System.lineSeparator()
              + "In the case of message encoding, you will have to give the key, the size of which varies depending on the selected encryption technique.System.lineSeparator()
              + "For the decryption of the CBC, it will be required to enter the first pad used to encode the message.System.lineSeparator()
              + "The result will then be displayed. ");
    }

    // Si l'on code
    else if (answer.equals(s1)) {

      // Savoir avec quelle methode, demande tant que la méthode est incorrecte
      String methode = methodeCode();

      String m1 = "CAESAR";
      String m2 = "VIGENERE";
      String m3 = "XOR";
      String m4 = "ONETIME";
      String m5 = "CBC";

      // Avec CAESAR
      if (methode.equals(m1)) {
        int key = keylengthOf1();
        boolean spaces = WithOrWithoutSpace();
        if (spaces) {

        } else {

        }
      }
      // Avec VIGENERE
      else if (methode.equals(m2)) {

        int keySize = keySize();
        int tab[] = keyTab(keySize);
        boolean spaces = WithOrWithoutSpace();
        if (spaces) {

        } else {

        }
      }
      // AvecXOR
      else if (methode.equals(m3)) {

        int key = keylengthOf1();
        boolean spaces = WithOrWithoutSpace();
        if (spaces) {

        } else {

        }
      }
      // Avec ONETIME
      else if (methode.equals(m4)) {

        int tab[] = keyOneTimePad(message);
      }
      // Avec CBC
      else if (methode.equals(m5)) {

        int keySize = keySize();
        int tab[] = keyTab(keySize);
      }
    }

    // Si l'on decode
    else if (answer.equals(s2)) {

      String methode = methodeDecode();

      // Savoir avec quelle methode, demande tant que la méthode est incorrecte
      String m1 = "caesarbruteforce";
      String m2 = "caesarwithfrequencies";
      String m3 = "xorbruteforce";
      String m4 = "vigenerewithfrequencies";
      String m5 = "decryptcbc";

      // Avec caesarbruteforce
      if (methode.equals(m1)) {

      }
      // Avec caesarwithfrequencies
      else if (methode.equals(m2)) {

      }
      // Avec xorbruteforce
      else if (methode.equals(m3)) {

      }
      // Avec vigenerewithfrequencies
      else if (methode.equals(m4)) {

      }
      // Avec decryptcbc
      else if (methode.equals(m5)) {

        int keySize = keySize();

        int tab[] = keyTab(keySize);

      }

    }
  }

  public static String asking() {

    String answer = "";
    String s1 = "code";
    String s2 = "decode";
    String s3 = "Help";
    boolean correctAnswer = false;
    do {
      System.out.println("If you need help understanding how the shell works (Write help) " + System.lineSeparator()
          + "Do you want to code a message or to decode a message ? (Write code or decode) : ");
      answer += keyboard.nextLine();
      if (answer.equals(s1) || answer.equals(s2) || answer.equals(s3)) {
        correctAnswer = true;
      }
    } while (!correctAnswer);
    return answer;
  }

  public static String message() {

    String message;
    do {
      System.out.println("Write down the message you want to code : ");
      message = keyboard.nextLine();
    } while (message != null);
    return message;
  }

  public static String methodeCode() {
    // Savoir avec quelle methode, demande tant que la méthode est incorrecte

    int i = 0;
    String methode;
    String m1 = "CAESAR";
    String m2 = "VIGENERE";
    String m3 = "XOR";
    String m4 = "ONETIME";
    String m5 = "CBC";
    do {
      System.out.println(
          "Your message can be coded with Caesar, Vigenere, Xor, Onetime or CBC. Which one do you want ? (Write it in capital letters, ex : Xor => XOR) : ");
      methode = keyboard.nextLine();
      if (methode.equals(m1) || methode.equals(m2) || methode.equals(m3) || methode.equals(m4) || methode.equals(m5)) {
        ++i;
      }
    } while (i == 0);
    return methode;
  }

  public static String methodeDecode() {
    // Savoir avec quelle methode, demande tant que la méthode est incorrecte

    int i = 0;
    String methode;
    String m1 = "caesarbruteforce";
    String m2 = "caesarwithfrequencies";
    String m3 = "xorbruteforce";
    String m4 = "vigenerewithfrequencies";
    String m5 = "decryptcbc";
    do {
      System.out.println(
          "Your message can be decoded with CaesarBruteForce, CaesarWithFrequencies, XorBruteForce, VigenereWithFrequencies or DecryptCBC. Which one do you want ? (Write it in lowercase letter, ex : DecryptCBC => decryptcbc) : ");
      methode = keyboard.nextLine();
      if (methode.equals(m1) || methode.equals(m2) || methode.equals(m3) || methode.equals(m4) || methode.equals(m5)) {
        ++i;
      }
    } while (i == 0);
    return methode;
  }

  public static int keylengthOf1() {

    // Savoir la clé, tant qu'elle n'est pas nulle
    System.out.println("Which key do you want to use (Write a number) ? : ");
    int key = keyboard.nextInt();
    return key;
  }

  public static int keySize() {

    // Savoir la clé, (tant qu'elle n'est pas nulle ??)
    System.out.println("What is the size of your key ? : ");
    int size = keyboard.nextInt();
    return size;
  }

  public static int[] keyTab(int keySize) {

    // Creation d'untableau qui condiendra les clés
    System.out.println("The size of your message is : " + keySize);
    System.out.println("You have to put one key number per letter of your message (write numbers) : ");

    int tabKey[] = new int[keySize];
    for (int i = 0; i < keySize; ++i) {
      System.out.println();
      tabKey[i] = keyboard.nextInt();
    }

    return tabKey;
  }

  public static int[] keyOneTimePad(String message) {

    // Une clé pour chque lettre (transformée en byte) du message
    System.out.println("The size of your message is : " + message.length());
    System.out.println("You have to put one key number per letter of your message (write numbers) : ");

    int tabKey[] = new int[message.length()];
    for (int i = 0; i < message.length(); ++i) {
      System.out.println();
      tabKey[i] = keyboard.nextInt();
    }

    return tabKey;
  }

  public static boolean WithOrWithoutSpace() {
    boolean spaces;
    int i = 0;
    do {
      System.out.println("Do you want to skip spaces or not ? ( Write yes or not) ");
      String message = keyboard.nextLine();
      String s1 = "yes";
      String s2 = "not";
      if (message.equals(s1)) {
        return true;
        ++i;
      } else if (message.equals(s1)) {
        return false;
        ++i;
      }
    } while (i > 0);

  }
}
