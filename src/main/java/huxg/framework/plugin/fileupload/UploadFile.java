package huxg.framework.plugin.fileupload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import huxg.framework.config.SysConfiguration;
import huxg.framework.util.uuid.RandomGUID;
import huxg.framework.web.servlet.SysLoaderServlet;

/**
 * 上传文件工具类
 * 
 * @author huxg
 * 
 */
public class UploadFile {
	private static Logger log = Logger.getLogger(UploadFile.class);

	public static final int UPLOAD_TYPE_INTE = 1;

	public static final int UPLOAD_TYPE_ALONE = 2;

	public static int UploadType = UPLOAD_TYPE_INTE;

	public static String UploadFileContext;

	private static String m_base;

	static {
		try {
			UploadType = Integer.parseInt(SysConfiguration.getInstance().getProperty("web.upload.type"));
		} catch (Exception e) {
			UploadType = UPLOAD_TYPE_INTE;
		}

		if (UploadType == UPLOAD_TYPE_INTE) {

			UploadFileContext = SysLoaderServlet.getRealPath();

			UploadFileContext += File.separatorChar + "uploadfiles";

			File f = new File(UploadFileContext);

			if (!f.exists()) {
				f.mkdir();
			}
			m_base = UploadFileContext;
		} else {
			UploadFileContext = SysConfiguration.getInstance().getProperty("web.upload.fileserver");
			m_base = SysConfiguration.getInstance().getProperty("web.upload.dir");
		}
	}

	public static void uploadFile(String filePath, byte[] data) throws Exception {
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			File file = new File(m_base + filePath);
			File folder = file.getParentFile();
			if (!folder.exists())
				folder.mkdirs();
			if (!file.exists())
				file.createNewFile();
			out = new BufferedOutputStream(new FileOutputStream(file));
			in = new BufferedInputStream(new ByteArrayInputStream(data));
			int count = 0;
			byte[] read = new byte[1024];
			while ((count = in.read(read, 0, 1024)) > 0) {
				out.write(read, 0, count);
			}
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (Exception e) {
			}
		}
	}

	public static byte[] getUploadFileData(String filePath) {
		ByteArrayOutputStream out = null;
		BufferedInputStream bin = null;
		try {
			File file = new File(m_base + filePath);
			bin = new BufferedInputStream(new FileInputStream(file));
			out = new ByteArrayOutputStream();
			byte data[] = new byte[1024];
			int count = 0;
			while ((count = bin.read(data, 0, 1024)) > 0) {
				out.write(data, 0, count);
			}
			return out.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (out != null)
					out.close();
				if (bin != null)
					bin.close();
			} catch (Exception e) {
			}
		}
	}

	public static String getFileBase() {
		return m_base;
	}

	public static String getIwebfilesPath() {
		int i = m_base.lastIndexOf("/");
		return m_base.substring(i);
	}

	public void uploadFile(MultipartFile file, String floder, String guid) throws FileNotFoundException, IOException {
		InputStream in = null;
		in = file.getInputStream();
		File fileName = new File(m_base + floder);
		if (!fileName.exists()) {
			fileName.mkdirs();
		}
		OutputStream out = null;

		try {
			out = new FileOutputStream(m_base + floder + guid);
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = in.read(b)) > 0) {
				out.write(b, 0, i);
			}
			out.flush();
		} catch (FileNotFoundException ex) {
			log.error("Error:", ex);
			throw ex;
		} catch (IOException ex) {
			log.error("Error:", ex);
			throw ex;
		} finally {
			out.close();
			in.close();
		}
	}

	public String uploadFile(MultipartFile file, String type) throws FileNotFoundException, IOException {
		String floadName = makeFloder(type);
		String typeName = file.getOriginalFilename();
		int index = typeName.lastIndexOf(".");
		typeName = typeName.substring(index);
		String fileName = makeFileName() + typeName;
		uploadFile(file, floadName, fileName);
		return floadName + fileName;
	}

	public static String generatorNewUploadFileName(String type, String fileType) {
		String floadName = makeFloder(type);
		String fileName = makeFileName() + fileType;
		return floadName + fileName;
	}

	public static void deleteFile(String path) {
		File file = new File(m_base + path);
		file.delete();
	}

	public static String makeFloder(String type) {
		Calendar cal = Calendar.getInstance();
		StringBuffer buf = new StringBuffer();
		buf.append(File.separator).append(type);
		buf.append(File.separator).append(cal.get(Calendar.YEAR));
		buf.append(File.separator).append(cal.get(Calendar.MONTH) + 1);
		buf.append(File.separator).append(cal.get(Calendar.DAY_OF_MONTH));
		buf.append(File.separator);
		return buf.toString();
	}

	public static String makeFileName() {
		return new RandomGUID().toString();
	}
}
