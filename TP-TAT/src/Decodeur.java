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
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFile = "tatooed.pgm";
		String outputPref = "out_";
		String outfile ="";
		int beginData = 160;
		
		Pixmap tatou = new ShortPixmap(inputFile);
		short[] bytes = tatou.getShorts();
		ShortBuffer buffer = ShortBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.position(0);
		
		//On lit la taille du fichier
		int sizeRes = 0;
		System.out.println(hex(sizeRes));
		for(int i =0;i<4;i++)
		{
			sizeRes = sizeRes <<8;
			short oct = readOne(buffer);
			System.out.println(hex(oct));
			System.out.println("-");
			sizeRes= sizeRes | oct;
		}
		System.out.println(sizeRes);
		System.out.println(hex(sizeRes));
		
		
		///On lit le nom du fichier
		byte[] fileName = new byte[1];
		for(int i =0;i<16;i++)
		{
			short oct = readOne(buffer);
			if(oct ==0)
				break;
			fileName[0]= (byte)oct;
			outfile+=new String(fileName);
		}
		
		DataOutputStream out ;
		try{
			out = new DataOutputStream(new FileOutputStream(outputPref+outfile));
		}
		catch (FileNotFoundException ex)
		{
			out = new DataOutputStream(new FileOutputStream(outputPref+"File_Error.pgm"));
		}
		
		
		//Saut au flux de donnée (-16 car le lecteur de pgm saute l'entête)
		buffer.position(beginData-16);
		
		//On recuperer les deux premier bits de chaque octet
		for(int i =0;i<sizeRes;i++)
		{	
			short oct = readOne(buffer);
			out.writeByte((byte)oct);
		}
		out.close();
	}

	public static short readOne(ShortBuffer buffer)
	{
		short oct = 0;
		for(int i =0;i<4;i++)
		{
			oct = (short) (oct << 2);
			
			if(!buffer.hasRemaining()){
				oct = (byte) (oct << 2*(3-i));
				break;
			}
			
			
			short b = buffer.get();
			//System.out.println(hex(b));
			b =(short) (b & 0x3);
			
			
			oct = (short) (oct | b);
		}
		
		return oct;
	}
	
}
