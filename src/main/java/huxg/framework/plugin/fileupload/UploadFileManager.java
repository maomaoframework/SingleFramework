package huxg.framework.plugin.fileupload;

import org.springframework.web.multipart.MultipartFile;

import huxg.framework.plugin.fileupload.alioos.AliOOSFileUploador;
import huxg.framework.plugin.fileupload.local.LocaldFileUploador;

/**
 * 文件上传客户端类 <b> 该类可以将文件上传到文件服务器上去</b>
 * 
 * @author huxg
 * 
 */
public abstract class UploadFileManager {
	// public static final String S_VIDEO_TYPE =
	// "avi|mpg|wmv|3gp|mov|mp4|asf|asx|flv|wmv9|rm|rmvb";
	// public static final String S_PIC_TYPE = "jpg|jpeg|png|gif";
	// public static final String S_DOC_TYPE =
	// "txt|doc|docx|ppt|pptx|pdf|xls|xlsx";
	// public static final String S_WAR_TYPE = "war";
	// public static final int LY_YX = 2;
	// public static final int LY_WB = 1;
	// public static final int LY_WK = 3;
	// public static final int LY_PX = 4;
	// public static final String URL_VIDEO_NOIMAGE =
	// SysConfiguration.getInstance().getProperty("CFG_URL_O_FS") +
	// "/public/images/no_image.jpg";
	// public static final String URL_DOC_NOIMAGE =
	// SysConfiguration.getInstance().getProperty("CFG_URL_O_FS") +
	// "/public/images/no_doc_image.jpg";
	//
	// public enum FILE_TYPE {
	// VIDEO, DOC, PIC, BIN, WAR;
	// public static FILE_TYPE getFileType(String extention) {
	// String s = extention.toLowerCase();
	// if (S_VIDEO_TYPE.contains(s))
	// return FILE_TYPE.VIDEO;
	// else if (S_PIC_TYPE.contains(s)) {
	// return FILE_TYPE.PIC;
	// } else if (S_DOC_TYPE.contains(s)) {
	// return FILE_TYPE.DOC;
	// } else if (S_WAR_TYPE.contains(s)) {
	// return FILE_TYPE.WAR;
	// } else
	// return FILE_TYPE.BIN;
	// }
	// };
	//
	// // 正在转换
	// public static int STATUS_CONVERT = 50;
	// public static int STATUS_OK = 10; // 正常，
	// public static int STATUS_ERR = 20; // 存在错误
	// public static int STATUS_UNKNOWN = 30; // 未知状态
	// public static int STATUS_CDN_SYING = 40; // 正在同步CDn
	// public static int STATUS_CDN_OK = 60; // 完成CDN同步
	//
	// public enum UPLOAD_TYPE {
	// publicResource, project, temp, personalVideo, war;
	// public static String getName(UPLOAD_TYPE ut) {
	// return ut.toString();
	// }
	// }
	//
	// public static TFile uploadFile(MultipartFile file) {
	// String fileServerUrl =
	// SysConfiguration.getInstance().getProperty("upload.folder");
	// if (fileServerUrl.startsWith("smb:")) {
	// return new SambFileUploador().uploadFile(file);
	// } else {
	// // 本地文件模式
	// return new LocaldFileUploador().uploadFile(file);
	// }
	// }
	//
	// /**
	// * 根据URL获取文件流
	// */
	// public static InputStream getFileInputStreamFromUrl(String url) {
	// String fileServerUrl =
	// SysConfiguration.getInstance().getProperty("upload.folder");
	// try {
	// if (fileServerUrl.startsWith("smb:")) {
	// return new SambFileUploador().getFileInputStreamFromUrl(url);
	// } else {
	// // 本地文件模式
	// return new LocaldFileUploador().getFileInputStreamFromUrl(url);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
	//
	// /**
	// * 根据URL取得MultipartFile
	// *
	// * @param url
	// * @return
	// */
	// public static MultipartFile getMultipartFileFromUrl(String url) {
	// String fileServerUrl =
	// SysConfiguration.getInstance().getProperty("upload.folder");
	// try {
	// if (fileServerUrl.startsWith("smb:")) {
	// return new SambFileUploador().getFileFromUrl(url);
	// } else {
	// // 本地文件模式
	// return new LocaldFileUploador().getFileFromUrl(url);
	// }
	// } catch (Exception e) {
	// return null;
	// }
	// }
	//
	// public static String getRealPath(TFile file) {
	// String relativePath = file.getCWjljXd();
	// String prefix = SysConfiguration.getProperty("system.static_server");
	// String sub = relativePath.substring(prefix.length());
	//
	// String realPath = SysLoaderServlet.getRealPath() + sub;
	// return realPath;
	// }
	//
	// public static String copyNewFileName(String src) {
	// File file = new File(src);
	// File folder = file.getParentFile();
	// String filename = file.getName();
	// String ext = filename.substring(filename.lastIndexOf("."));
	// String newFileName = Key.key() + ext;
	// File newFile = new File(folder, newFileName);
	// return newFile.getPath();
	// }
	//
	// public static String convert2RelativePath(String absolutePath) {
	// String prefix = SysConfiguration.getProperty("system.static_server");
	// String realPrefix = SysLoaderServlet.getRealPath();
	//
	// String sufix = absolutePath.substring(realPrefix.length());
	// if (!sufix.startsWith("/")) {
	// sufix = "/" + sufix;
	// }
	// return prefix + sufix;
	// }

	/**
	 * 上传图片，文件上传至阿里云服务器
	 * 
	 * @param file
	 * @return
	 */
	public static TFile uploadAliyun(MultipartFile file, String bucketName) {
		return new AliOOSFileUploador().uploadFile(file, bucketName);

		// String fileServerUrl =
		// SysConfiguration.getInstance().getProperty("upload.folder");
		// if (fileServerUrl.startsWith("smb:")) {
		// return new SambFileUploador().uploadFile(file);
		// } else {
		// // 本地文件模式
		// return new LocaldFileUploador().uploadFile(file);
		// }
	}

	public static TFile uploadLocal(MultipartFile file, String bucketName) {
		return new LocaldFileUploador().uploadFile(file);

	}

	public static MultipartFile getMultipartFileFromUrl(String url) {
		return new AliOOSFileUploador().getFileFromUrl(url);
	}
}
