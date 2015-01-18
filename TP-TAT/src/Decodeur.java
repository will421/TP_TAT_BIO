import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;


public class Decodeur {

	public static String hex(int n) {
	    // call toUpperCase() if that's required
	    return String.format("0x%8s", Integer.toHexString(n)).replace(' ', '0');
	}
	
	/*
	 * Lis un octet caché dans le buffer donné
	 * Attention : Prend en compte la position du curseur
	 */
	public static short readOne(ShortBuffer buffer)
	{
		short res = 0;
		for(int i =0;i<4;i++) //Pour lire 4 octets dans le buffer
		{
			res = (short) (res << 2);
			
			if(!buffer.hasRemaining()){ //Si il n'y a plus rien à lire, on rempli le reste de 0
				res = (byte) (res << 2*(3-i));
				break;
			}
			
			short b = buffer.get(); //Lecture de l'octet
			b =(short) (b & 0x3); //Recuperation des deux bits de poids faible
			res = (short) (res | b); //Ajout au resultat
		}
		return res;
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFile = "tatooed.pgm";
		String outputPref = "out_";
		String outfile ="";
		int beginData = 160;
		
		Pixmap tatou = new ShortPixmap(inputFile); //Image tatoué
		short[] bytes = tatou.getShorts(); 
		ShortBuffer buffer = ShortBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.position(0);
		
		//On lit la taille du fichier
		int sizeRes = 0;
		for(int i =0;i<4;i++){
			sizeRes = sizeRes <<8;
			short oct = readOne(buffer);
			sizeRes= sizeRes | oct;
		}
			
		///On lit le nom du fichier
		byte[] fileName = new byte[1];
		for(int i =0;i<16;i++){
			short oct = readOne(buffer);
			if(oct ==0)
				break;
			fileName[0]= (byte)oct;
			outfile+=new String(fileName);
		}
		
		//Fichier de sorti
		DataOutputStream out ;
		try{
			out = new DataOutputStream(new FileOutputStream(outputPref+outfile));
		}
		catch (FileNotFoundException ex){
			out = new DataOutputStream(new FileOutputStream(outputPref+"File_Error.pgm"));
		}
		
		
		//Saut au flux de donnée (-16 car le lecteur de pgm saute l'entête)
		buffer.position(beginData-16);
		
		//On recuperer les octets caché un par un pour les ecrire dans le fichier de sorti
		for(int i =0;i<sizeRes;i++)
		{	
			short oct = readOne(buffer);
			out.writeByte((byte)oct);
		}
		out.close();
	}
	
}
