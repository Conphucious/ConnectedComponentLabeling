package labbookpages;

//Otsu Thresholder Demo
// http://www.labbookpages.co.uk/software/imgProc/otsuThreshold.html

// I DO NOT OWN THIS. I AM USING THIS CODE IN REFERENCE TO SUBSTITUTE PART OF MY FUNCTION

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

//A.Greensted
//http://www.labbookpages.co.uk
//Please use however you like. I'd be happy to hear any feedback or comments.
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

public class Otsu {
	public static File threshold(String filename) {
		// Load Source image
		BufferedImage srcImage = null;

		try {
			File imgFile = new File(filename);
			srcImage = javax.imageio.ImageIO.read(imgFile);
		} catch (IOException ioE) {
			System.err.println(ioE);
			System.exit(1);
		}

		int width = srcImage.getWidth();
		int height = srcImage.getHeight();

		// Get raw image data
		Raster raster = srcImage.getData();
		DataBuffer buffer = raster.getDataBuffer();

		int type = buffer.getDataType();
		if (type != DataBuffer.TYPE_BYTE) {
			System.err.println("Wrong image data type");
			System.exit(1);
		}
		if (buffer.getNumBanks() != 1) {
			System.err.println("Wrong image data format");
			System.exit(1);
		}

		DataBufferByte byteBuffer = (DataBufferByte) buffer;
		byte[] srcData = byteBuffer.getData(0);

		// Sanity check image
		if (width * height != srcData.length) {
			System.err.println("Unexpected image data size. Should be greyscale image");
			System.exit(1);
		}

		// Output Image info
		System.out.printf("Loaded image: '%s', width: %d, height: %d, num bytes: %d\n", filename, width, height,
				srcData.length);

		byte[] dstData = new byte[srcData.length];

		// Create Otsu Thresholder
		OtsuThresholder thresholder = new OtsuThresholder();
		int threshold = thresholder.doThreshold(srcData, dstData);

		System.out.printf("Threshold: %d\n", threshold);

		// Create GUI
		GreyFrame dstFrame = new GreyFrame(width, height, dstData);

		// Save Images
		try {
			int dotPos = filename.lastIndexOf(".");
			String basename = filename.substring(0, filename.lastIndexOf("."));
			
			File theFile = new File(filename);
			
			File file = new File("images/binary/bin_" + theFile.getName() + ".png");

			javax.imageio.ImageIO.write(dstFrame.getBufferImage(), "PNG", file);
			return file;
		} catch (IOException ioE) {
			System.err.println("Could not write image " + filename);
		}
		return null;
	}

}
