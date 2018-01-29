package display;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//MNIST STRUCTURE http://yann.lecun.com/exdb/mnist/

public class ImageHandler {

	byte[] endData;
	byte[] labelData;
	
	int magic;
	int lmagic;
	int lcount;
	int count;
	int rows;
	int cols;
	
	public void loadData() throws IOException {
		InputStream is = ClassLoader.class.getResourceAsStream("/train-images-idx3-ubyte");
		byte[] firstData = new byte[16];
		is.read(firstData, 0, 16);
		
		ByteBuffer bb = ByteBuffer.wrap(firstData);
		bb.order(ByteOrder.BIG_ENDIAN);
		
		magic = bb.getInt();
		count = bb.getInt();
		rows = bb.getInt();
		cols = bb.getInt();
		
		System.out.println(magic);
		
		endData = new byte[rows*cols*count];
		is.read(endData);
		is.close();
		
		InputStream lis = ClassLoader.class.getResourceAsStream("/train-labels-idx1-ubyte");
		byte[] lData = new byte[8];
		lis.read(lData, 0, 8);
		
		bb = ByteBuffer.wrap(lData);
		bb.order(ByteOrder.BIG_ENDIAN);
		
		lmagic = bb.getInt();
		lcount = bb.getInt();
		
		System.out.println(lmagic);
		
		labelData = new byte[count];
		lis.read(labelData);
		lis.close();
	}
	
	public int getLabel(int imagenum) {
		
		byte data = labelData[imagenum];
		return Byte.toUnsignedInt(data);
	}
	
	public Image getImage(int imagenum){
		BufferedImage image = new BufferedImage(cols,rows,BufferedImage.TYPE_BYTE_GRAY);
		DataBuffer imageData = new DataBufferByte(endData,rows*cols,imagenum*rows*cols);
		image.setData(Raster.createRaster(image.getSampleModel(), imageData, new Point()));
		
		int scaledW = image.getWidth()*4;
		int scaledH = image.getHeight()*4;
		
		BufferedImage resize = new BufferedImage(scaledW, scaledH, image.getType());		
		Graphics2D g = resize.createGraphics();
		g.drawImage(image, 0, 0, scaledW, scaledH, 0,0,image.getWidth(), image.getHeight(), null);
		g.dispose();
		
		return resize;
	}
	
	public BufferedImage getRawImage(int imagenum) {
		BufferedImage image = new BufferedImage(cols,rows,BufferedImage.TYPE_BYTE_GRAY);
		DataBuffer imageData = new DataBufferByte(endData,rows*cols,imagenum*rows*cols);
		image.setData(Raster.createRaster(image.getSampleModel(), imageData, new Point()));
		
		return image;
	}
}
