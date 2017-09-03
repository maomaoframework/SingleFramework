package huxg.framework.util;

public class UnSupportedExcelFormatException extends Exception {
	private String message;

	public UnSupportedExcelFormatException(String message) {
		// 文件路径
		this.message = message;
	}
}
