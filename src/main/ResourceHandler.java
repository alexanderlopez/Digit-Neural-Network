package main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ResourceHandler {

	private int magicI;
	private int countI;
	private int imageRows;
	private int imageCols;
	
	private int magicL;
	private int countL;
	
	private byte[] imageData;
	private byte[] labelData;
	
	public void init() throws IOException {
		byte[] buffer = new byte[16];
		InputStream is = ClassLoader.class.getResourceAsStream("/train-images-idx3-ubyte");
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
		
		is = ClassLoader.class.getResourceAsStream("/train-labels-idx1-ubyte");
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
	
	public int getLabel(int resourceNum) {
		return Byte.toUnsignedInt(labelData[resourceNum]);
	}
}
