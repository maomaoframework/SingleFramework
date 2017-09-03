package huxg.framework.plugin.fileupload.local;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import huxg.framework.config.SysConfiguration;
import huxg.framework.plugin.fileupload.IFileUploador;
import huxg.framework.plugin.fileupload.MyMultiPartFile;
import huxg.framework.plugin.fileupload.TFile;
import huxg.framework.util.StringUtils;
import huxg.framework.util.uuid.Key;
import huxg.framework.web.servlet.SysLoaderServlet;

/**
 * 本地的上传管理器
 * 
 * @author huxg
 * 
 */
public class LocaldFileUploador implements IFileUploador {
	/**
	 * 上传文件到文件服务器，这里的文件服务器是本地
	 */
	@Override
	public TFile uploadFile(MultipartFile file) {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		// 从缓存服务器中读取配置信息
		try {
			String outputFolder = null;
			String cfspath = SysConfiguration.getInstance().getProperty("upload.folder");
			if (!StringUtils.isEmpty(cfspath)) {
				outputFolder = cfspath;
			} else {
				outputFolder = SysLoaderServlet.getRealPath();
			}

			if (outputFolder.endsWith("/"))
				outputFolder = outputFolder.substring(0, outputFolder.length() - 1);

			// 按照日期来创建文件夹
			SimpleDateFormat sf = new SimpleDateFormat("yyy" + File.separatorChar + "MM" + File.separatorChar + "dd");
			String current = sf.format(new Date());
			outputFolder = outputFolder + File.separatorChar + current;

			int idx = file.getOriginalFilename().lastIndexOf(".");
			String ext = "";
			if (idx >= 0) {
				ext = file.getOriginalFilename().substring(idx + 1);
			} 
			
			if (StringUtils.isEmpty(ext)){
				ext = "jpg";
			}

			String fileid = Key.key();
			String filename = fileid + "." + ext;
			String outputFilePath = outputFolder + File.separatorChar + filename;
			File outputFile = new File(outputFilePath);

			File dir = new File(outputFolder);
			if (!dir.exists())
				dir.mkdirs();

			in = new BufferedInputStream(file.getInputStream());
			out = new BufferedOutputStream(new FileOutputStream(outputFile));

			byte[] data = new byte[1024];
			int count = 0;
			while ((count = in.read(data, 0, 1024)) != -1) {
				out.write(data, 0, count);
			}

			TFile l = new TFile();
			l.setCYwjm(file.getOriginalFilename());
			l.setCWjljXd(SysConfiguration.getInstance().getProperty("system.static_server") + "/" + current + "/" + filename);
			l.setCLx(ext);
			l.setCBh(fileid);
			l.setNYwjdx(file.getSize());
			l.setCKzm(ext);

			// 当前所属分类编号，默认分类编号
			l.setCFlbh("-1");
			l.setDScsj(new Date());
			l.setNDjcs(0L);
			l.setNSccs(0L);
			l.setNPlcs(0L);
			l.setNFz(0);
			l.setNSfkf(0);
			l.setNXzcs(0L);
			l.setNFxcs(0L);

			return l;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (Exception e) {

			}
		}
	}

	// @Override
	// public TFile uploadFile(MultipartFile file, UPLOAD_TYPE upType, int ly,
	// boolean converted) {
	// BufferedInputStream in = null;
	// BufferedOutputStream out = null;
	//
	// // 从缓存服务器中读取配置信息
	// try {
	// String outputFolder = null;
	// String cfspath =
	// SysConfiguration.getInstance().getProperty("upload.folder");
	// if (!StringUtils.isEmpty(cfspath)) {
	// outputFolder = cfspath;
	// } else {
	// outputFolder = SysLoaderServlet.getRealPath();
	// }
	//
	// if (outputFolder.endsWith("/"))
	// outputFolder = outputFolder.substring(0, outputFolder.length() - 1);
	//
	// // 按照日期来创建文件夹
	// SimpleDateFormat sf = new SimpleDateFormat("yyy" + File.separatorChar +
	// "MM" + File.separatorChar + "dd");
	// String current = sf.format(new Date());
	// outputFolder = outputFolder + File.separatorChar + current;
	//
	// int idx = file.getOriginalFilename().lastIndexOf(".");
	// String ext = "";
	// if (idx >= 0) {
	// ext = file.getOriginalFilename().substring(idx + 1);
	// }
	//
	// String fileid = Key.key();
	// String filename = fileid + "." + ext;
	// String outputFilePath = outputFolder + File.separatorChar + filename;
	// File outputFile = new File(outputFilePath);
	//
	// File dir = new File(outputFolder);
	// if (!dir.exists())
	// dir.mkdirs();
	//
	// in = new BufferedInputStream(file.getInputStream());
	// out = new BufferedOutputStream(new FileOutputStream(outputFile));
	//
	// byte[] data = new byte[1024];
	// int count = 0;
	// while ((count = in.read(data, 0, 1024)) != -1) {
	// out.write(data, 0, count);
	// }
	//
	// TFile l = new TFile();
	// l.setCYwjm(file.getOriginalFilename());
	// l.setCWjljXd(SysConfiguration.getInstance().getProperty("system.static_server")
	// + "/" + current + "/" + filename);
	// // l.setCWjljJd(outputFilePath);
	// l.setCLx(FILE_TYPE.getFileType(ext).name());
	// l.setCBh(fileid);
	// l.setNYwjdx(file.getSize());
	// l.setCKzm(ext);
	// if (l.getCLx().equals(FILE_TYPE.PIC.name())) {
	// l.setCSltUrl(l.getCSltUrl());
	// } else if (l.getCLx().equals(FILE_TYPE.VIDEO.name())) {
	// l.setCSltUrl(UploadFileManager.URL_VIDEO_NOIMAGE);
	// } else if (l.getCLx().equals(FILE_TYPE.DOC.name())) {
	// l.setCSltUrl(UploadFileManager.URL_DOC_NOIMAGE);
	// }
	//
	// // 当前所属分类编号，默认分类编号
	// l.setCFlbh("-1");
	// l.setDScsj(new Date());
	// l.setNDjcs(0L);
	// l.setNSccs(0L);
	// l.setNPlcs(0L);
	// l.setNFz(0);
	// l.setNSfkf(0);
	// l.setNXzcs(0L);
	// l.setNFxcs(0L);
	//
	// return l;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// } finally {
	// try {
	// if (out != null)
	// out.close();
	// if (in != null)
	// in.close();
	// } catch (Exception e) {
	//
	// }
	// }
	//
	// }

	@Override
	public MultipartFile getFileFromUrl(String url) {
		String relativePath = url;
		if (url.startsWith("http")) {
			int idx = url.indexOf(":");
			relativePath = url.substring(idx + 3);
			idx = relativePath.indexOf("/");
			relativePath = relativePath.substring(idx);
		}

		String filepath = SysConfiguration.getInstance().getProperty("upload.folder") + relativePath;
		MultipartFile mf = new MyMultiPartFile(new File(filepath));
		return mf;
	}

	/**
	 * 根据URL获取到某一个文件在文件服务器中的位置
	 */
	@Override
	public InputStream getFileInputStreamFromUrl(String url) throws Exception {
		MultipartFile mf = getFileFromUrl(url);
		return mf.getInputStream();
	}

}
