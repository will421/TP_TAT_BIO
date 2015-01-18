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
	
	public static void testUnitaire()
	{
		byte data = (byte) 0x01;
		byte[] bytes = {15,15,15,15,15,15};
		writeOne(data, bytes, 1);
		System.out.println(bytes.toString());
	}
	
	
	public static void main(String[] args) throws IOException {
		
		//On recupère les paramètres
		String contentFile = args[0];
		String tatooFile = args[1];
		String codedFile ="tatooed.pgm";
		int beginData = Integer.parseInt(args[2]);
		
		Pixmap contentFilePIX = new BytePixmap(contentFile);
		
		DataInputStream MyTatoo = new DataInputStream(new FileInputStream(tatooFile));
		byte[] bytes = contentFilePIX.getBytes();
		byte[] tatooBytes = tatooFile.getBytes();
		
		
		//Ecriture du nom de fichier
		for (int i = 0 ; i < 16; i++){
			
			if(i>=tatooBytes.length)
			{
				writeOne((byte)0,bytes, 16+i*4);
			}
			else
			{
				writeOne(tatooBytes[i],bytes, 16+i*4);
			}
		}
		

		//ecriture du reste du flux
		boolean stop = false;
		int n =0;
		while(!stop){
			int r = MyTatoo.read();
			if(r==-1){
				stop = true;
			}
			else{
				byte data = (byte)r;
				writeOne(data,bytes,beginData-16+n*4);
				n++;
			}
			
		}
		System.out.println("Ecrit:"+n);
		
		
		// Nous avons pu recupere la taille grace à la variable N, nous l'inscrivons maitnenant dans notr fichier
		for(int i=0;i<4;i++)
		{
			writeOne((byte)(n>>8*(3-i)),bytes,0+i*4);
		}

		
		Pixmap outputPIX = new BytePixmap(contentFilePIX.width,contentFilePIX.height,bytes);
		
		outputPIX.write(codedFile);
	}
	
	
	
	
	// writeOne : data -> octet de data que je veux écrire
	// Bytes possède déjà les données de l'image à tatouté ( image intialie )
	public static void writeOne(byte data, byte[] bytes, int pos){
		for(int i=0; i < 4; i++ ){
			
			byte tmp = (byte) (data >> 2*(3-i)); 
			byte dataLast = (byte) (tmp & 0x3);
			
			byte orig = bytes[pos+i];
			orig  = (byte) ( orig & ~0x3);
			 
			bytes[pos+i] =  (byte) (orig |dataLast);
			//System.out.println(orig);
		}
		
		
		
	}
	
	
}
