package huxg.framework.plugin.fileupload;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploador {

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param upType
	 * @param ly
	 * @param converted
	 * @return
	 */
	public TFile uploadFile(MultipartFile file);

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param upType
	 * @return
	 */
	// public TFile uploadFile(MultipartFile file, UPLOAD_TYPE upType, int ly,
	// boolean converted);

	// public TFile uploadFile2Local(MultipartFile file);

	// public TFile resaveFile2Personal(String loc, String uid, int ly, boolean
	// converted);

	// public TFile uploadPersonalVideo(MultipartFile file, String userid, int
	// ly, boolean converted);

	/**
	 * 取得文件服务器上某一个文件的输入流
	 * 
	 * @param url
	 *            含有http:// 开头的上传文件url
	 * @return
	 * @throws Exception
	 */
	public InputStream getFileInputStreamFromUrl(String url) throws Exception;

	/**
	 * 取得文件对象
	 * 
	 * @param url
	 * @return
	 */
	public MultipartFile getFileFromUrl(String url);
}
