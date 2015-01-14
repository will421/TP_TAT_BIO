import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ShortBuffer;


public class Encodeur {

	
	public static String hex(int n) {
	    // call toUpperCase() if that's required
	    return String.format("0x%8s", Integer.toHexString(n)).replace(' ', '0');
	}
	
	
	public static void main(String[] args) throws IOException {
		String contentFile = args[0];
		String tatooFile = args[1];
		String codedFile ="tatooed.pgm";
		int beginData = Integer.parseInt(args[2]);
		
		Pixmap contentFilePIX = new BytePixmap(contentFile);
		
		DataInputStream MyTatoo = new DataInputStream(new FileInputStream(tatooFile));
		byte[] bytes = contentFilePIX.getBytes();
		byte[] tatooBytes = tatooFile.getBytes();
		
		
		// ecriture du nom de fichier
		for (int i = 0 ; i < tatooBytes.length; i++){
			
			writeOne(tatooBytes[i],bytes, 16+i*4);
			
		}
		

		//ecriture du reste
		boolean stop = false;
		int i =0;
		while(!stop){
			int r = MyTatoo.read();
			if(r==-1){
				stop = true;
			}
			else{
				byte data = (byte)r;
				writeOne(data,bytes,beginData+i*4);
				
			}
			i++;
		}
		
		//Maintenon à la taille;
		
		writeOne((byte)i,bytes,0);
		
		Pixmap outputPIX = new BytePixmap(contentFilePIX.width,contentFilePIX.height,bytes);
		
		outputPIX.write(codedFile);
	}
	
	
	
	
	// writeOne : data -> octet de data que je veux écrire
	// Bytes possède déjà les données de l'image à tatouté ( image intiali )
	public static void writeOne(byte data, byte[] bytes, int pos){
		

		for(int i=0; i < 4; i++ ){
			
			byte orig = bytes[pos+i];
			byte dataLast = (byte) (data & 0x3);
			orig  = (byte) ( orig & ~0x3);
			
			bytes[pos+i] =  (byte) (orig |dataLast);
			
			data = (byte) (data >> 2); 
		}
		
		
		
	}
	
	
}
