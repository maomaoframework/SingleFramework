package huxg.framework.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import huxg.framework.plugin.fileupload.TFile;
import huxg.framework.plugin.fileupload.UploadFileManager;
import huxg.framework.plugin.fileupload.alioos.AliOOSFileUploador;
import huxg.framework.util.FileUtils;
import huxg.framework.util.JsonUtils;
import huxg.framework.util.StringUtils;
import huxg.framework.util.cutimage.CutImage;
import huxg.framework.util.cutimage.ImageCut;
import huxg.framework.util.myutil.Base64;
import huxg.framework.util.uuid.Key;
import huxg.framework.vo.Message;
import huxg.framework.web.controller.BaseController;

/**
 * 文件上传视图类
 * 
 * @author apple
 * 
 */
@Controller("FileUploadController")
@RequestMapping("/upload")
public class FileUploadController extends BaseController {
	// 最大允许上传5M
	private final int MAX_FILE_LENGTH = 10;
	private final String MAX_IMAGE_SIZE = "800x800";

	/**
	 * 上传文件
	 * 
	 * @return
	 */
	@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String index() {
		String name = (String) getParameter("fn");
		String fid = (String) getParameter("fid");
		String type = (String) getParameter("type");

		// 最大允许上传文件的大小
		String maxlen = (String) getParameter("maxlen");

		String maxsize = (String) getParameter("maxsize");

		// 是否校验上传图片尺寸
		String strictsize = (String) getParameter("strictSize");

		// 是否校验上传文件大小
		String strictLen = (String) getParameter("strictLength");

		boolean verifySize = false;
		boolean verifyLen = false;

		try {
			verifySize = Boolean.parseBoolean(strictsize);
		} catch (Exception e) {
		}

		try {
			verifyLen = Boolean.parseBoolean(strictLen);
		} catch (Exception e) {
		}

		// 默认文件类型
		if (StringUtils.isEmpty(type))
			type = "image";

		if (!StringUtils.isEmpty(name)) {
			MultipartFile uploadfile = (MultipartFile) getUploadFiles(name);

			if (uploadfile == null || uploadfile.getSize() <= 0) {
				return Message.errorMessage("文件上传失败，不存在该文件");
			}

			// 检查上传文件大小
			if (verifyLen) {
				int max = MAX_FILE_LENGTH;
				try {
					max = Integer.parseInt(maxlen);
				} catch (Exception e) {
				}

				// 检查文件大小
				if (overflowMaxSize(max, uploadfile.getSize())) {
					// 错误，发送错误通知，同时删除文件
					return Message.errorMessage("文件大小超过限制，您上传的文件不能超过：" + max);
				}
			}

			if ("image".equals(type)) {
				if (verifySize) {
					// 验证图片尺寸
					String max = MAX_IMAGE_SIZE;
					if (!StringUtils.isEmpty(maxsize)) {
						max = maxsize;
					}

					String[] size = max.split("x");
					try {
						int width = Integer.parseInt(size[0]);
						int height = Integer.parseInt(size[1]);

						// 读取图片文件，并检查文件尺寸大小
						BufferedImage sourceImg = ImageIO.read(uploadfile.getInputStream());
						int realWidth = sourceImg.getWidth();
						int realHeight = sourceImg.getHeight();

						if (realWidth > width || realHeight > height) {
							return Message.errorMessage("图片上传失败，您上传的图片尺寸为" + realWidth + "x" + realHeight + "像素，超过了限定尺寸：" + max + "像素");
						}
					} catch (Exception e) {
						return Message.errorMessage("图片上传失败，图片尺寸不符：" + max);
					}
				}
			}

			try {
				// 读取文件，并判断文件尺寸
				TFile file = UploadFileManager.uploadLocal(uploadfile, AliOOSFileUploador.BUCKET_public);

				UploadFileData u = new UploadFileData();
				u.setUrl(file.getCWjljXd());
				// 2015-11-23 添加路径
				u.setLocateUrl(FileUtils.getWebFileAbstractPath(file.getCWjljXd()));
				u.setFid(fid);
				u.setFname(name);
				u.setFilename(uploadfile.getOriginalFilename());

				if ("image".equals(type)) {
					BufferedImage sourceImg = ImageIO.read(uploadfile.getInputStream());
					int realWidth = sourceImg.getWidth();
					int realHeight = sourceImg.getHeight();
					u.setImageWidth(realWidth);
					u.setImageHeight(realHeight);
				}
				return Message.okMessage(u);
			} catch (Exception e) {
				return Message.errorMessage("服务器繁忙，无法上传，请稍后再试");
			}
		}
		return Message.errorMessage("服务器繁忙，无法上传，请稍后再试");
	}

	private boolean overflowMaxSize(long maxsize, long realsize) {
		long mb = realsize / 1024 / 1024;
		return mb > maxsize;
	}

	/**
	 * 上传照片
	 * 
	 * @return
	 */
	@RequestMapping(value = "/uploadphoto")
	public @ResponseBody
	String uploadphoto() {
		try {
			MultipartFile logo = (MultipartFile) getUploadFiles("photo");
			TFile file = null;
			if (logo != null && logo.getSize() > 0) {
				// 处理保存文件
				file = UploadFileManager.uploadAliyun(logo, AliOOSFileUploador.BUCKET_public);
			}

			return Message.okMessage(file);
		} catch (Exception e) {
			return Message.error();
		}
	}

	/**
	 * 裁剪头像
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cutimg")
	public @ResponseBody
	String cutimg() {

		String jsonIms = (String) getParameter("ims");

		// 进行两次解码
		try {
			jsonIms = URLDecoder.decode(jsonIms, "UTF-8");
			jsonIms = URLDecoder.decode(jsonIms, "UTF-8");
		} catch (Exception e) {

		}

		Collection<CutImage> ims = JsonUtils.String2Collection(jsonIms, CutImage.class);

		try {
			if (ims != null && ims.size() > 0) {
				CutImage image = ims.iterator().next();

				String newpath = ImageCut.abscut(image.getSrc(), image.getWidthInt(), image.getHeightInt(), image.getLeftInt(), image.getTopInt(),
						image.getStandardWidthInt(), image.getStandardHeightInt());
				return Message.okMessage(newpath);
			}
			return Message.errorMessage("图片尺寸过小或超大无法裁剪，请上传适合的图像进行裁剪");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.errorMessage(e.getMessage());
		}
	}

	@RequestMapping(value = "/forkindeditor", produces = "text/html;charset=UTF-8")
	public @ResponseBody
	String forkindeditor() {
		String name = (String) getParameter("fn");
		if (!StringUtils.isEmpty(name)) {
			MultipartFile uploadfile = (MultipartFile) getUploadFiles(name);

			try {
				TFile file = null;
				if (uploadfile != null && uploadfile.getSize() > 0) {
					// 处理保存文件
					file = UploadFileManager.uploadAliyun(uploadfile, AliOOSFileUploador.BUCKET_public);

					JSONObject json = new JSONObject();
					json.put("error", 0);
					json.put("url", file.getCWjljXd());
					return json.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 上传文件数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/data")
	public @ResponseBody
	String data() throws Exception {
		String htmlContent = (String) getParameter("editor");
		Pattern pattern = Pattern.compile("\\<img[^>]*src=\"data:image/[^>]*>");
		Matcher matcher = pattern.matcher(htmlContent);
		while (matcher.find()) { // 找出base64图片元素
			String str = matcher.group();
			String ext = StringUtils.defaultIfEmpty(StringUtils.substringBetween(str, "data:image/", ";"), "jpg");// 图片后缀
			String base64ImgData = StringUtils.substringBetween(str, "base64,", "\"");// 图片数据
			if (!StringUtils.isEmpty(base64ImgData)) {
				MultipartFile file = decodeBase64ToImage(base64ImgData, ext);

				if (file != null) {
					// 保存文件
					TFile upfile = UploadFileManager.uploadAliyun(file, AliOOSFileUploador.BUCKET_public);
					UploadFileData u = new UploadFileData();
					u.setUrl(upfile.getCWjljXd());
					return Message.okMessage(u);
				}

			}
		}
		return Message.error();
	}

	public static MultipartFile decodeBase64ToImage(String base64, String ext) {
		Base64 decoder = new Base64();
		byte[] decoderBytes = decoder.decode(base64);
		MultipartFile file = new ImageBase64MultipartFile(decoderBytes, ext);
		return file;
	}
}

class ImageBase64MultipartFile implements MultipartFile {
	byte[] data;
	String filename;

	public ImageBase64MultipartFile(byte[] data, String ext) {
		this.data = data;
		this.filename = Key.key() + "." + ext;
	}

	@Override
	public String getName() {
		return this.filename;
	}

	@Override
	public String getOriginalFilename() {
		return this.filename;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return data == null || data.length == 0;
	}

	@Override
	public long getSize() {
		return data.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return data;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new BufferedInputStream(new ByteArrayInputStream(data));
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		// TODO Auto-generated method stub
	}
}