package huxg.framework.plugin.fileupload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jcifs.smb.SmbFile;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.model.OSSObject;

/**
 * MultiPart 本地文件
 * 
 * @author huxg
 * 
 */
public class MyMultiPartFile implements MultipartFile {
	private File file = null;
	private SmbFile smbFile = null;
	private OSSObject ossObject = null;

	public MyMultiPartFile(OSSObject ossObject) {
		this.ossObject = ossObject;
	}

	public MyMultiPartFile(File file) {
		this.file = file;
	}

	public MyMultiPartFile(SmbFile smbFile) {
		this.smbFile = smbFile;
	}

	@Override
	public String getName() {
		if (smbFile != null)
			return smbFile.getName();
		if (file != null)
			return file.getName();
		if (ossObject != null)
			return ossObject.getBucketName();
		return "";
	}

	@Override
	public String getOriginalFilename() {
		if (smbFile != null)
			return smbFile.getName();
		if (file != null)
			return file.getName();
		if (ossObject != null)
			return ossObject.getBucketName();
		return "";
	}

	@Override
	public String getContentType() {
		return "application/octet-stream";
	}

	@Override
	public boolean isEmpty() {
		if (smbFile == null)
			return !this.file.exists() || file.length() == 0;
		else
			try {
				return !this.smbFile.exists() || smbFile.length() == 0;
			} catch (Exception e) {
				return true;
			}
	}

	@Override
	public long getSize() {
		if (file != null) {
			return file.length();
		} else {
			try {
				return smbFile.length();
			} catch (Exception e) {
				return -1;
			}
		}
	}

	@Override
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream out = null;
		InputStream in = null;
		try {
			out = new ByteArrayOutputStream();
			in = getInputStream();
			int count = -1;
			byte[] data = new byte[1024];

			while ((count = in.read(data, 0, 1024)) != -1) {
				out.write(data, 0, count);
			}
			return out.toByteArray();
		} catch (Exception e) {

		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (this.file != null)
			return new FileInputStream(this.file);

		if (smbFile != null)
			return smbFile.getInputStream();

		if (ossObject != null)
			return ossObject.getObjectContent();
		return null;
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
	}
}