package huxg.framework.util.cutimage;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageUtil {

	public static void main(String arg[]) {
		String filePath = "http://10.10.25.185:8080/b/uploadfiles/signName/2012/7/31/EE5D0FF386FE6D5CFF97AC2B4AF53810.jpg"; // 图片的位置

		int height = 200;
		int width = 200;
		Icon icon = null;
		try {
			icon = getFixedIcon(filePath, width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 按宽的比例更改图片的大小
	 * 
	 * @param filePath
	 *            图片路径
	 * @param width
	 *            需要改变图片的宽度
	 * @return
	 * @throws Exception
	 */
	public static Icon getRatioWidth(String filePath, int width)
			throws Exception {

		File f = new File(filePath);

		BufferedImage bi = ImageIO.read(f);

		double wRatio = (new Integer(width)).doubleValue() / bi.getWidth(); // 宽度的比例

		int height = (int) (wRatio * bi.getHeight()); // 图片转换后的高度

		Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 设置图像的缩放大小

		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getScaleInstance(wRatio, wRatio), null); // 设置图像的缩放比例

		image = op.filter(bi, null);

		int lastLength = filePath.lastIndexOf(".");
		String subFilePath = filePath.substring(0, lastLength); // 得到图片输出路径
		String fileType = filePath.substring(lastLength); // 图片类型
		File zoomFile = new File(subFilePath + "_" + width + "_" + height
				+ fileType);

		Icon ret = null;
		try {
			ImageIO.write((BufferedImage) image, "jpg", zoomFile);
			ret = new ImageIcon(zoomFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 按高的比例更改图片大小
	 * 
	 * @param filePath
	 *            图片路径
	 * @param height
	 *            需要改变图片的高度
	 * @return
	 * @throws Exception
	 */
	public static Icon getRatioHeight(String filePath, int height)
			throws Exception {
		File f = new File(filePath);

		BufferedImage bi = ImageIO.read(f);

		double hRatio = (new Integer(height)).doubleValue() / bi.getHeight(); // 高度的比例

		int width = (int) (hRatio * bi.getWidth()); // 图片转换后的高度

		Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 设置图像的缩放大小

		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getScaleInstance(hRatio, hRatio), null); // 设置图像的缩放比例

		image = op.filter(bi, null);

		int lastLength = filePath.lastIndexOf(".");
		String subFilePath = filePath.substring(0, lastLength); // 得到图片输出路径
		String fileType = filePath.substring(lastLength); // 图片类型
		File zoomFile = new File(subFilePath + "_" + width + "_" + height
				+ fileType);

		Icon ret = null;
		try {
			ImageIO.write((BufferedImage) image, "jpg", zoomFile);
			ret = new ImageIcon(zoomFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 按输入的任意宽高改变图片的大小
	 * 
	 * @param filePath
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public static Icon getFixedIcon(String filePath, int width, int height)
			throws Exception {
		File f = new File(filePath);

		BufferedImage bi = ImageIO.read(f);

		double wRatio = (new Integer(width)).doubleValue() / bi.getWidth(); // 宽度的比例

		double hRatio = (new Integer(height)).doubleValue() / bi.getHeight(); // 高度的比例

		Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 设置图像的缩放大小

		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getScaleInstance(wRatio, hRatio), null); // 设置图像的缩放比例

		image = op.filter(bi, null);

		int lastLength = filePath.lastIndexOf("\\");

		String subFilePath = filePath.substring(0, lastLength); // 得到图片输出路径

		String fileName = filePath.substring(lastLength + 1, filePath.length());// 得到文件名称

		createtemp(subFilePath);

		File zoomFile = new File(subFilePath + "\\temp\\" + fileName);

		Icon ret = null;
		try {
			ImageIO.write((BufferedImage) image, "jpg", zoomFile);
			ret = new ImageIcon(zoomFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static void createtemp(String path) {
		File file = new File(path);
		if (file.exists()) {
			File filetemp = new File(path + "/temp");
			if (!filetemp.exists()) {
				filetemp.mkdir();
			}
		}
	}

}
