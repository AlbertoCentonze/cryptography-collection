import crypto.Helper;

public class test {
	 public static void main(String[] args) {
		 				 
		 String plainText = "bonne journée";
		 System.out.println(plainText);
		  byte[] plainBytes = Helper.stringToBytes(plainText);
		  
		  
		 byte Key = 4;
		 byte[] cipherText = caesar(plainBytes , Key);
		 
		 for(int i=0; i<plainText.length(); i++) {
			 		
		 System.out.print("  "+cipherText[i]);
		  }
		 
	 }
	 public static byte[] caesar(byte [] plainText, byte key) {
		 
		 //Création tableau qui va changer les byte
		 byte tab [] = new byte[plainText.length];
		 	
		 //Remplissage du tableau 
		 for(int i=0; i<plainText.length; i++) {
			 			 			 
			 if(plainText[i] != 32) {
				 tab[i]= (byte) (plainText[i] + (byte)key);
			 } else { 
				 tab[i]=32;
			 }
		 }
		 
		 //Création tableau qui retransforme les valeurs de tab en String
		 byte [] tabReturn = new byte [plainText.length];
		 
		 //Remplissage du Tableau
		 for(int j=0;j<plainText.length;j++) {
			 		 
		 tabReturn [j] = Helper.bytesToString(tab[j]);
		 }		 			 		 
		
		 		 	
		 //Valeur attendue pour le cipherText: {-101, 32, -87, -109, -96, -90}
			
		 assert(plainText != null);
			// TODO: COMPLETE THIS METHOD
			
			return tab; // TODO: to be modified
		}
		
}
