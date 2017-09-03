package huxg.framework.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import huxg.framework.config.SysConfiguration;
import huxg.framework.plugin.fileupload.TFile;
import huxg.framework.plugin.fileupload.UploadFileManager;
import huxg.framework.plugin.fileupload.alioos.AliOOSFileUploador;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码生成工具
 * 
 * @author huxg
 * 
 */
public class QRUtils {
	/**
	 * 产生商品URL
	 * 
	 * @return
	 */
	public static TFile generateQRImageGoodsBuyForMobile(String goodsId) throws Exception {
		String url = SysConfiguration.getProperty("site.url") + "/shopping/buy/" + goodsId;
		String filename = "qr_code_" + goodsId + "for_mobile_buy.png";
		TFile f = generateQRImage(filename, url);
		return f;
	}

	/**
	 * 生成图像
	 * 
	 * @throws WriterException
	 * @throws IOException
	 */
	public static TFile generateQRImage(String filename, String url) throws WriterException, IOException {
		String content = url;// 内容
		int width = 240; // 图像宽度
		int height = 240; // 图像高度

		String format = "png";// 图像类型
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 2);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵

		File tempFolder = FileUtils.getTempFolder();

		Path path = FileSystems.getDefault().getPath(tempFolder.getCanonicalPath(), filename);

		File tempFile = path.toFile();

		MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像

		// 转存到文件服务器上
		TFile f = UploadFileManager.uploadAliyun(FileUtils.convert2MultiPart(tempFile), AliOOSFileUploador.BUCKET_public);

		// 删除临时文件
		tempFile.deleteOnExit();
		return f;
	}
}
