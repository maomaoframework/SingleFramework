package huxg.framework.util.cutimage;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class SambaImageCut implements IImageCut {
	/**
	 * 裁剪头像
	 */
	public String cutImage(int x, int y, int w, int h, String imgLocation, String sufix, int constaint) throws Exception {
		// 切割图片
		// 生成新文件的名字
		int idx = imgLocation.lastIndexOf(".");
		String ext = imgLocation.substring(idx + 1);
		String newFileName = imgLocation.substring(0, idx) + "_" + sufix + "." + ext;

		cutImage(imgLocation, newFileName, x, y, w, h, ext, constaint);

		return newFileName;
	}

	// 图片裁剪
	public void cutImage(String imagePath, String targetPath, int x, int y, int w, int h, String ext, int constraint) throws Exception {
		BufferedImage image = ImageIO.read(new SmbFileInputStream(new SmbFile(imagePath)));

		int iSrcWidth = image.getWidth();// 得到源图宽
		int iSrcHeight = image.getHeight(); // 得到源图长

		// 计算缩放因子，取得最大的尺寸
		double scale = 1;
		int maxEdge = iSrcWidth > iSrcHeight ? iSrcWidth : iSrcHeight;
		if (maxEdge > constraint) {
			scale = maxEdge * 1.0 / constraint;
		}

		// 计算实际偏移量
		double realX = scale * x;
		double realY = scale * y;

		// 计算实际高度宽度
		double realWith = scale * w;
		double realHeight = scale * h;

		// 剪裁
		BufferedImage newImage = image.getSubimage((int) realX, (int) realY, (int) realWith, (int) realHeight);
		ImageIO.write(newImage, ext, new SmbFileOutputStream(new SmbFile(targetPath)));
	}
}
