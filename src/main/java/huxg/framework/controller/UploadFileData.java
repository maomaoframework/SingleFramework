package huxg.framework.controller;

/**
 * 上传文件数据
 * 
 * @author huxg
 * 
 */
public class UploadFileData {
	String url;
	String locateUrl;
	String fid;
	String fname;
	String filename;
	int imageWidth;
	int imageHeight;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public String getLocateUrl() {
		return locateUrl;
	}

	public void setLocateUrl(String locateUrl) {
		this.locateUrl = locateUrl;
	}
	

}
