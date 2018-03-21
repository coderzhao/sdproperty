package com.quadrant.fr;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;

public class ImageUtil {
	
	
	public static void drawStringOnImage(int x , int y, String name, BufferedImage bgImage)
			throws IOException
	{
		Graphics graph = bgImage.getGraphics();
		graph.setColor(Color.RED);	
		graph.drawString(name, x, y);
		
		graph.dispose();
	}
	
	public static void drawBoxesOnImage(int x , int y , int w , int h , String name, BufferedImage bgImage)
			throws IOException
	{
		Graphics graph = bgImage.getGraphics();
		graph.setColor(Color.RED);
		
			graph.drawRect(x, y, w, h);
			
			graph.drawString(name, x, y - 10);
		
		graph.dispose();
	}
	
	public static BufferedImage convertByte2BufferedImage(byte[] jpg) {
		BufferedImage jpgImage = null;
		try {
			jpgImage = ImageIO.read(new ByteArrayInputStream(jpg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jpgImage;
	}

	public static byte[] convertBufferedImage2Byte(BufferedImage jpgImage) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(jpgImage, "jpeg", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] loadImageFile(String file) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File(file));
			BufferedImage jpgImage = ImageIO.read(fileInputStream);
			ImageIO.write(jpgImage, "jpeg", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (fileInputStream!=null){
					fileInputStream.close();
				}
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}
	
	public static String loadImageFile2Base64Text(String file) throws UnsupportedEncodingException {
		byte[] img = loadImageFile(file);
		return new String(Base64.encodeBase64(img) , "UTF-8");
	}
	
	public static void main(String[] args) throws IOException {
		String file = "/home/quadrant/snapshot/sdfasdaf/dsfdsafd.jpg";
		int idx = file.indexOf("snapshot");
		System.out.println(file.substring(idx+9));
		
		System.out.println(Paths.get("aaa","bbb"));
		
//		String prjFolder = System.getProperty("user.dir");
//		byte[] imgByte= loadImageFile(prjFolder +"/src/main/java/com/quadrant/fr/f5-1.jpg");
////		BufferedImage img = convertByte2BufferedImage(imgByte);
////		int w = img.getWidth();
////		System.out.println(w);
//		for(int i =0 ; i < 20;i++) {
//			BufferedImage img = convertByte2BufferedImage(imgByte);
//			drawStringOnImage(i * 48, img.getHeight()/2, "Bean Liao", img);
//			ImageIO.write(img, "jpg", new File(prjFolder +"/src/main/java/com/quadrant/fr/L5-" + i +".jpg"));
//		}
//		System.out.println("done");
		
	}
	
	
	
	
	
	
}
