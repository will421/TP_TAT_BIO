import java.io.*;
import java.awt.*;

public class DoublePixmap extends Pixmap {
  
  public final double[] data;
  
  public DoublePixmap(int w, int h, double[] pixels) throws IllegalArgumentException {
    super(w, h);
    if ( pixels == null )
      pixels = new double[w*h];
    if ( pixels.length != w*h )
      throw new IllegalArgumentException("bad dimensions");
    data = pixels;
  }

  public DoublePixmap(int w, int h) {
    this(w, h, null);
  }

  public DoublePixmap(Pixmap p) {
    this(p.width, p.height, p.getDoubles());
  }

  public DoublePixmap(String fileName) throws IOException {
    super(fileName);
    data = getDoubles(readBytes());
    close();
  }

  // conversions
  public byte[] getBytes() {
    return getBytes(data);
  }

  public short[] getShorts() {
    return getShorts(data);
  }

  public double[] getDoubles() {
    return getDoubles(data);
  }

  // strings
  public String pixelType() {
    return "double";
  }
  
}
