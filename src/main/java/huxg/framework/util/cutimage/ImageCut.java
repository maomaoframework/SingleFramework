package huxg.framework.util.cutimage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import huxg.framework.config.SysConfiguration;
import huxg.framework.util.uuid.Key;

public class ImageCut {
	public static IImageCut getImageCut() {
		String fileServerUrl = SysConfiguration.getInstance().getProperty("upload.folder");
		if (fileServerUrl.startsWith("smb:")) {
			return new SambaImageCut();
		} else {
			// 本地文件模式
			return new LocalImageCut();
		}
	}

	public static void stretchImage(String sourceFilePath, String extention, int width, int height) throws Exception {
		// 截取文件路径，并转换为实际路径
		// 首先进行图片拉伸处理
		BufferedImage reader = ImageIO.read(new File(sourceFilePath));
		Image sourceImage = reader.getScaledInstance(width, height, Image.SCALE_DEFAULT);

		BufferedImage writer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = writer.getGraphics();
		g.drawImage(sourceImage, 0, 0, null);
		g.dispose();

		ImageIO.write(writer, extention, new File("/Users/huxg//abc.png"));
	}

	public static void cutImage(String src, String dest, String extention, int x, int y, int w, int h) throws IOException {
		Iterator iterator = ImageIO.getImageReadersByFormatName(extention);
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		Rectangle rect = new Rectangle(x, y, w, h);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, extention, new File(dest));

	}

	public static void main(String[] args) {
		// 截取文件路径，并转换为实际路径
		String filepath = "/Users/huxg/Pictures/Chrysanthemum.jpg";
		int width = 280;
		int height = 224;
		int left = Math.abs(-72);
		int top = Math.abs(-83);

		try {
			String extention = filepath.substring(filepath.lastIndexOf(".") + 1);
			stretchImage(filepath, extention, width, height);

			cutImage("/Users/huxg/abc.png", "/Users/huxg/def.png", extention, left, top, 120, 120);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param srcImageFile
	 * @param x
	 * @param y
	 * @param destWidth
	 * @param destHeight
	 */
	public static String abscut(String webpath, int stretchWidth, int stretchHeight, int x, int y, int destWidth, int destHeight) throws Exception {
		// 截取文件路径，并转换为实际路径
		String sourceFilePath = getSourceFilePath(webpath);
		String extention = sourceFilePath.substring(sourceFilePath.lastIndexOf(".") + 1);
		
		String tempFilePath = getTempFilePath(sourceFilePath, extention);
		
		String targetFilePath = getTargetFilePath(sourceFilePath, extention, destWidth, destHeight);
		String targetWebPath = getTargetWebPath(webpath, extention, destWidth, destHeight);
		try {

			// 拉伸
			Graphics g = null;
			BufferedImage sourceBuffer = null;
			BufferedImage stretchdBuffer = null;
			Image sourceImage = null;
			try {
				sourceBuffer = ImageIO.read(new File(sourceFilePath));
				sourceImage = sourceBuffer.getScaledInstance(stretchWidth, stretchHeight, Image.SCALE_DEFAULT);

				stretchdBuffer = new BufferedImage(stretchWidth, stretchHeight, BufferedImage.TYPE_INT_RGB);
				g = stretchdBuffer.getGraphics();
				g.drawImage(sourceImage, 0, 0, null);
				ImageIO.write(stretchdBuffer, extention, new File(tempFilePath));
			} finally {
				if (g != null)
					g.dispose();
				sourceBuffer = null;
				stretchdBuffer = null;
				sourceImage = null;
			}
			
			// 裁剪
			ImageInputStream iis = null;
			BufferedImage bi = null;
			ImageReader reader = null;
			try {
				Iterator iterator = ImageIO.getImageReadersByFormatName(extention);
				reader = (ImageReader) iterator.next();
				iis = ImageIO.createImageInputStream(new FileInputStream(tempFilePath));
				reader.setInput(iis, true);

				ImageReadParam param = reader.getDefaultReadParam();
				Rectangle rect = new Rectangle(x, y, destWidth, destHeight);
				param.setSourceRegion(rect);

				bi = reader.read(0, param);
				ImageIO.write(bi, extention, new File(targetFilePath));
			} finally {
				if (iis != null) {
					iis.close();
				}
				bi = null;
				reader = null;
			}

			return targetWebPath;
		} catch (Exception e) {
			throw new Exception("图像尺寸有误，清上传其他图片");
		}
	}

	public static String getSourceFilePath(String webpath) {
		String prefix = SysConfiguration.getInstance().getProperty("system.static_server");
		String sourcePath = webpath;
		if (webpath.startsWith(prefix)) {
			sourcePath = sourcePath.substring(prefix.length());
		}
		sourcePath = SysConfiguration.getInstance().getProperty("upload.folder") + sourcePath;
		return sourcePath;
	}
	
	private static String getTempFilePath (String sourceFilePath, String ext){
		int dv = sourceFilePath.lastIndexOf(".");
		String prefix = sourceFilePath.substring(0, dv);
		String target = prefix + Key.key() + "." + ext;
		return target;
	}
	
	private static String getTargetFilePath(String sourceFilePath, String ext, int width, int height) {
		int dv = sourceFilePath.lastIndexOf(".");
		String prefix = sourceFilePath.substring(0, dv);
		String target = prefix + "_" + width + "_" + height + "." + ext;
		return target;
	}

	private static String getTargetWebPath(String webpath, String ext, int width, int height) {
		int idx = webpath.lastIndexOf(".");

		// web路径
		String targetWebPath = webpath.substring(0, idx) + "_" + width + "_" + height + "." + ext;
		return targetWebPath;
	}
}
