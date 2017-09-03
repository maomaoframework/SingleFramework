package huxg.framework.filter.parameter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class MyMultipartResolver extends CommonsMultipartResolver {

	/**
	 * <p>
	 * The default value for the maximum allowable size, in bytes, of an
	 * uploaded file. The value is equivalent to 250MB.
	 * </p>
	 */
	public static final long DEFAULT_SIZE_MAX = 250 * 1024 * 1024;

	/**
	 * <p>
	 * The default value for the threshold which determines whether an uploaded
	 * file will be written to disk or cached in memory. The value is equivalent
	 * to 250KB.
	 * </p>
	 */
	public static final int DEFAULT_SIZE_THRESHOLD = 256 * 1024;

	/**
	 * set default
	 * 
	 * @see org.springframework.web.multipart.commons.CommonsFileUploadSupport#newFileItemFactory()
	 */
	protected DiskFileItemFactory newFileItemFactory() {
		DiskFileItemFactory fif = super.newFileItemFactory();
		fif.setSizeThreshold(DEFAULT_SIZE_THRESHOLD);
		return fif;
	}

	/**
	 * set default
	 * 
	 * @see org.springframework.web.multipart.commons.CommonsMultipartResolver#newFileUpload(org.apache.commons.fileupload.FileItemFactory)
	 */
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
		FileUpload fu = super.newFileUpload(fileItemFactory);
		fu.setFileSizeMax(DEFAULT_SIZE_MAX);
		return fu;
	}

	public void setMaxInMemorySizeWithUnit(String maxInMemorySizeWithUnit) {
		int maxInMemorySize = (int) convertSizeToBytes(maxInMemorySizeWithUnit, DEFAULT_SIZE_THRESHOLD);
		super.setMaxInMemorySize(maxInMemorySize);
	}

	/**
	 * @param maxUploadSize
	 *            ����ϴ��ļ���С(��λ:K,M,G)
	 * @see #setMaxUploadSize(long)
	 */
	public void setMaxUploadSizeWithUnit(String maxUploadSizeWithUnit) {
		super.setMaxUploadSize(convertSizeToBytes(maxUploadSizeWithUnit, DEFAULT_SIZE_MAX));
	}

	/**
	 * <p>
	 * Converts a size value from a string representation to its numeric value.
	 * The string must be of the form nnnm, where nnn is an arbitrary decimal
	 * value, and m is a multiplier. The multiplier must be one of 'K', 'M' and
	 * 'G', representing kilobytes, megabytes and gigabytes respectively.
	 * </p>
	 * <p>
	 * If the size value cannot be converted, for example due to invalid syntax,
	 * the supplied default is returned instead.
	 * </p>
	 * 
	 * @param sizeString
	 *            The string representation of the size to be converted.
	 * @param defaultSize
	 *            The value to be returned if the string is invalid.
	 * @return The actual size in bytes.
	 */
	protected long convertSizeToBytes(String sizeString, long defaultSize) {
		int multiplier = 1;
		if (sizeString.endsWith("K")) {
			multiplier = 1024;
		} else if (sizeString.endsWith("M")) {
			multiplier = 1024 * 1024;
		} else if (sizeString.endsWith("G")) {
			multiplier = 1024 * 1024 * 1024;
		}

		if (multiplier != 1) {
			sizeString = sizeString.substring(0, sizeString.length() - 1);
		}

		long size = 0;
		try {
			size = Long.parseLong(sizeString);
		} catch (NumberFormatException nfe) {
			logger.warn("Invalid format for file size ('" + sizeString + "'). Using default.");
			size = defaultSize;
			multiplier = 1;
		}

		return (size * multiplier);
	}

	public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request) throws MultipartException {
		Assert.notNull(request, "Request must not be null");
		// resolve lazily
		return new MyMultipartHttpServletRequest(request) {

			protected void initializeMultipart() {
				MultipartParsingResult parsingResult = parseRequest(request);
				setMultipartFiles(parsingResult.getMultipartFiles());
				setMultipartParameters(parsingResult.getMultipartParameters());
			}
		};
	}

	/**
	 * Parse the given List of Commons FileItems into a Spring
	 * MultipartParsingResult, containing Spring MultipartFile instances and a
	 * Map of multipart parameter.
	 * 
	 * @param fileItems
	 *            the Commons FileIterms to parse
	 * @param encoding
	 *            the encoding to use for form fields
	 * @return the Spring MultipartParsingResult
	 * @see CommonsMultipartFile#CommonsMultipartFile(org.apache.commons.fileupload.FileItem)
	 */
	protected MultipartParsingResult parseFileItems(List fileItems, String encoding) {
		MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap();
		Map<String, String[]> multipartParameters = new HashMap();
		Map<String, String> multipartParameterContentTypes = new HashMap<String, String>();

		// Extract multipart files and multipart parameters.
		for (Iterator it = fileItems.iterator(); it.hasNext();) {
			FileItem fileItem = (FileItem) it.next();
			if (fileItem.isFormField()) {
				addTextParameter(multipartParameters, fileItem, encoding);
				multipartParameterContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
			} else {
				addFileParameter(multipartFiles, fileItem);

			}
		}
		return new MultipartParsingResult(multipartFiles, multipartParameters, multipartParameterContentTypes);
	}

	/**
	 * <p>
	 * Adds a regular text parameter to the set of text parameters for this
	 * request. Handles the case of multiple values for the same parameter by
	 * using an array for the parameter value.
	 * </p>
	 * 
	 * @param multipartParameters
	 *            The set of text parameters for this request
	 * @param formField
	 *            The form field for the parameter to add
	 * @param encoding
	 *            The encoding to use for form fields
	 */
	protected void addTextParameter(Map multipartParameters, FileItem formField, String encoding) {
		String value = null;
		if (encoding != null) {
			try {
				value = formField.getString(encoding);
			} catch (UnsupportedEncodingException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Could not decode multipart item '" + formField.getFieldName() + "' with encoding '" + encoding + "': using platform default");
				}
				value = formField.getString();
			}
		} else {
			value = formField.getString();
		}

		String[] oldArray = (String[]) multipartParameters.get(formField.getFieldName());
		String[] newArray;

		if (oldArray != null) {
			// array of simple form fields
			newArray = new String[oldArray.length + 1];
			System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
			newArray[oldArray.length] = value;
		} else {
			// simple form field
			newArray = new String[] { value };
		}

		multipartParameters.put(formField.getFieldName(), newArray);

	}

	/**
	 * <p>
	 * Adds a file parameter to the set of file parameters for this request.
	 * </p>
	 * 
	 * @param multipartFiles
	 *            The set of file parameters for this request
	 * @param fileItem
	 *            The file item for the parameter to add
	 */
	protected void addFileParameter(Map multipartFiles, FileItem fileItem) {
		// multipart file field
		UploadFileWrapper file = new UploadFileWrapper(fileItem);
		// add
		String name = fileItem.getFieldName();
		if (multipartFiles.containsKey(name)) {
			Object o = multipartFiles.get(name);
			if (o instanceof List) {
				((List) o).add(file);
			} else {
				List list = new ArrayList();
				list.add((MultipartFile) o);
				list.add(file);
				multipartFiles.put(name, list);
			}
		} else {
			List list = new ArrayList();
			list.add(file);
			multipartFiles.put(name, list);
		}

		// debug
		if (logger.isDebugEnabled()) {
			logger.debug("Found multipart file [" + file.getOriginalFilename() + "] of size " + file.getSize() + " bytes with original filename ["
					+ file.getOriginalFilename() + "], stored " + file.getStorageDescription());
		}
	}

	/**
	 * Cleanup the Spring MultipartFiles created during multipart parsing,
	 * potentially holding temporary data on disk.
	 * <p>
	 * Deletes the underlying Commons FileItem instances.
	 * 
	 * @param multipartFiles
	 *            Collection of MultipartFile instances
	 * @see org.apache.commons.fileupload.FileItem#delete()
	 */
	protected void cleanupFileItems(Collection multipartFiles) {
		if (multipartFiles.isEmpty()) {
			return;
		}
		for (Iterator it = multipartFiles.iterator(); it.hasNext();) {
			Object files = it.next();
			if (files instanceof List) {
				this.cleanupFileItems((List) files);
				continue;
			}
			UploadFileWrapper file = (UploadFileWrapper) files;
			if (logger.isDebugEnabled()) {
				logger.debug("Cleaning up multipart file [" + file.getOriginalFilename() + "] with original filename [" + file.getOriginalFilename() + "], stored "
						+ file.getStorageDescription());
			}
			file.destroy();
		}
	}
}
