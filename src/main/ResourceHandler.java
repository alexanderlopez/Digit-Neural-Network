package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ResourceHandler {

	public static final int TEST_SET = 1;
	public static final int TRAIN_SET = 0;
	
	private int magicI;
	private int countI;
	private int imageRows;
	private int imageCols;
	
	private int magicL;
	private int countL;
	
	private byte[] imageData;
	private byte[] labelData;
	
	public void init(int set) throws IOException {
		byte[] buffer = new byte[16];
		InputStream is = null;
		if (set == TRAIN_SET) {
			is = ClassLoader.class.getResourceAsStream("/train-images-idx3-ubyte");
		}
		if (set == TEST_SET) {
			is = ClassLoader.class.getResourceAsStream("/t10k-images-idx3-ubyte");
		}
		is.read(buffer, 0, 16);
		
		ByteBuffer bb = ByteBuffer.wrap(buffer);
		bb.order(ByteOrder.BIG_ENDIAN);
		magicI = bb.getInt();
		countI = bb.getInt();
		imageRows = bb.getInt();
		imageCols = bb.getInt();
		bb.clear();
		
		buffer = new byte[countI*imageRows*imageCols];
		imageData = new byte[countI*imageRows*imageCols];
		is.read(buffer);
		is.close();
		
		System.arraycopy(buffer, 0, imageData, 0, countI*imageRows*imageCols);
		
		if (set == TRAIN_SET) {
			is = ClassLoader.class.getResourceAsStream("/train-labels-idx1-ubyte");
		}
		if (set == TEST_SET) {
			is = ClassLoader.class.getResourceAsStream("/t10k-labels-idx1-ubyte");
		}
		buffer = new byte[8];
		is.read(buffer, 0, 8);
		
		bb = ByteBuffer.wrap(buffer);
		bb.order(ByteOrder.BIG_ENDIAN);
		magicL = bb.getInt();
		countL = bb.getInt();
		bb.clear();
		
		buffer = new byte[countL];
		labelData = new byte[countL];
		is.read(buffer, 0, countL);
		is.close();
		
		System.arraycopy(buffer, 0, labelData, 0, countL);
		
		System.out.println(magicI + ", " + magicL);
	}
	
	public int[] getResource(int resourceNum) {
		int[] data = new int[imageRows*imageCols];
		
		int offset = resourceNum*imageRows*imageCols;
		
		for (int i = 0; i < data.length; i++) {
			data[i] = Byte.toUnsignedInt(imageData[offset+i]);
		}
		
		return data;
	}
	
	public Image getResourceAsImage(int resourceNum) {
		BufferedImage image = new BufferedImage(imageCols, imageRows, BufferedImage.TYPE_BYTE_GRAY);
		DataBuffer buffer = new DataBufferByte(imageData, imageCols*imageRows, resourceNum*imageCols*imageRows);
		image.setData(Raster.createRaster(image.getSampleModel(), buffer, null));
		
		int scaledW = image.getWidth()*4;
		int scaledH = image.getHeight()*4;
		
		BufferedImage resize = new BufferedImage(scaledW, scaledH, image.getType());		
		Graphics2D g = resize.createGraphics();
		g.drawImage(image, 0, 0, scaledW, scaledH, 0,0,image.getWidth(), image.getHeight(), null);
		g.dispose();
		
		return resize;
	}
	
	public int getLabel(int resourceNum) {
		return Byte.toUnsignedInt(labelData[resourceNum]);
	}
}
