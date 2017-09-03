package huxg.framework.filter.parameter;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadFileWrapper extends CommonsMultipartFile implements MultipartFile {

	private static final long serialVersionUID = -1776360587741789086L;

	public UploadFileWrapper(FileItem fileItem) {
		super(fileItem);
	}

	// ===== implements methods for FormFile

	public void destroy() {
		super.getFileItem().delete();
	}

	public byte[] getFileData() throws FileNotFoundException, IOException {
		return super.getBytes();
	}

	public String getFileName() {
		return super.getOriginalFilename();
	}

	public int getFileSize() {
		return (int) super.getSize();
	}

	/**
	 * <p>
	 * Sets the content type for this file.
	 * <p>
	 * NOTE: This method is not supported in this implementation.
	 * </p>
	 * 
	 * @param contentType
	 *            A string representing the content type.
	 */
	public void setContentType(String contentType) {
		throw new UnsupportedOperationException("The setContentType() method is not supported.");
	}

	/**
	 * <p>
	 * Sets the (client-side) file name for this file.
	 * <p>
	 * NOTE: This method is not supported in this implementation.
	 * </p>
	 * 
	 * @param fileName
	 *            The client-side name for the file.
	 */
	public void setFileName(String fileName) {
		throw new UnsupportedOperationException("The setFileName() method is not supported.");
	}

	/**
	 * <p>
	 * Sets the size, in bytes, for this file.
	 * <p>
	 * NOTE: This method is not supported in this implementation.
	 * </p>
	 * 
	 * @param filesize
	 *            The size of the file, in bytes.
	 */
	public void setFileSize(int filesize) {
		throw new UnsupportedOperationException("The setFileSize() method is not supported.");
	}

	/**
	 * <p>
	 * Returns the (client-side) file name for this file.
	 * </p>
	 * 
	 * @return The client-size file name.
	 */
	public String toString() {
		return getFileName();
	}

}
