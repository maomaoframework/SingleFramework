package huxg.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.web.multipart.MultipartFile;

import huxg.framework.config.SysConfiguration;
import huxg.framework.plugin.fileupload.MyMultiPartFile;
import huxg.framework.web.servlet.SysLoaderServlet;

public class FileUtils {
	/**
	 * 去的web文件的绝对路径
	 * 
	 * @return
	 */
	public static String getWebFileAbstractPath(String url) {
		String prefix = SysConfiguration.getInstance().getProperty("system.static_server");
		String sourcePath = url;
		if (url.startsWith(prefix)) {
			sourcePath = sourcePath.substring(prefix.length());
		}
		sourcePath = SysConfiguration.getInstance().getProperty("upload.folder") + sourcePath;
		return sourcePath;
	}

	public static boolean saveString2File(String s, String filename) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
			writer.write(s);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public static void copyFile(File srcFile, File detFolder) {
		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try {
			String fileName = srcFile.getName();
			String targetFileName = detFolder.getAbsolutePath() + File.separatorChar + fileName;
			bin = new BufferedInputStream(new FileInputStream(srcFile));
			bout = new BufferedOutputStream(new FileOutputStream(new File(targetFileName)));
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = bin.read(buffer, 0, 1024)) != -1) {
				bout.write(buffer, 0, count);
			}
			bout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bin != null)
					bin.close();
				if (bout != null)
					bout.close();
			} catch (Exception e) {

			}
		}
	}

	public static void copyFiles2(String srcFolderPath, String targetFolderPath) {
		File srcFolder = new File(srcFolderPath);
		File targetFolder = new File(targetFolderPath);
		iteratorCopyFile(srcFolder, targetFolder);
	}

	private static void iteratorCopyFile(File folder, File targetFolder) {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				String folderName = file.getName();
				String targetFolderName = targetFolder.getAbsolutePath() + File.separatorChar + folderName;
				File f = new File(targetFolderName);
				if (!f.exists())
					f.mkdirs();
				iteratorCopyFile(file, f);
			} else {
				// 将源文件拷贝到目标目录中
				copyFile(file, targetFolder);
			}
		}
	}

	/**
	 * 取得临时文件夹
	 * 
	 * @return
	 */
	public static File getTempFolder() {
		String root = SysLoaderServlet.getRealPath();
		String temp = "temp";
		File rootFolder = new File(root, temp);
		if (!rootFolder.exists()) {
			rootFolder.mkdirs();
		}
		return rootFolder;
	}

	/**
	 * 将文件对象转换为MultiPart对象
	 * 
	 * @param file
	 * @return
	 */
	public static MultipartFile convert2MultiPart(File file) {
		return new MyMultiPartFile(file);
	}

}
