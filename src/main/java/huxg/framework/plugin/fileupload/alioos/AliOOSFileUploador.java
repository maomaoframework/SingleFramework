package huxg.framework.plugin.fileupload.alioos;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import huxg.framework.config.SysConfiguration;
import huxg.framework.plugin.fileupload.MyMultiPartFile;
import huxg.framework.plugin.fileupload.TFile;
import huxg.framework.util.uuid.Key;

/**
 * 本地的上传管理器
 * 
 * @author huxg
 * 
 */
public class AliOOSFileUploador {
	// 存储名称
	// 健康百科
	public static final String BUCKET_jkbk = "jkbk";

	// 公共空间
	public static final String BUCKET_public = "cnd-public";

	/**
	 * 上传文件到文件服务器，这里的文件服务器是本地
	 */
	public TFile uploadFile(MultipartFile file, String bucketName) {
		String accessKeyId = SysConfiguration.getProperty("upload.aliyun.accessKeyId");
		String accessKeySecret = SysConfiguration.getProperty("upload.aliyun.accessKeySecret");
		String endpoint = SysConfiguration.getProperty("upload.aliyun.endpoint");

		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		try {
			// 判断文件类型，如果是图片文件，则返回图片文件的路径
			String filename = file.getOriginalFilename();
			int idx = filename.lastIndexOf(".");
			String ext = "";
			if (idx >= 0) {
				ext = filename.substring(idx + 1);
			}

			String key = Key.key();
			key = key + "." + ext;
			client.putObject(bucketName, key, file.getInputStream());
			client.getObject(new GetObjectRequest(bucketName, key));

			TFile l = new TFile();

			if ("jpg".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext) || "png".equalsIgnoreCase(ext) || "bmp".equalsIgnoreCase(ext)
					|| "jpeg".equalsIgnoreCase(ext)) {
				// 图片类型
				l.setCWjljXd("http://" + bucketName + ".img-cn-beijing.aliyuncs.com/" + key);
			} else {
				// 文件
				l.setCWjljXd("http://" + bucketName + ".oss-cn-beijing.aliyuncs.com/" + key);
			}

			l.setCLx(ext);
			l.setCBh(key);
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
			if (client != null) {
				client.shutdown();
			}
		}
	}

	public MultipartFile getFileFromUrl(String url) {

		String accessKeyId = SysConfiguration.getProperty("upload.aliyun.accessKeyId");
		String accessKeySecret = SysConfiguration.getProperty("upload.aliyun.accessKeySecret");
		String endpoint = SysConfiguration.getProperty("upload.aliyun.endpoint");

		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		try {
			int idx = url.lastIndexOf("/");
			String prefix = url.substring(idx + 1);
			if (prefix.contains("@")) {
				prefix = prefix.substring(0, prefix.indexOf("@"));
			}

			// 取得bucketname
			String bucketname = url.substring(7);
			bucketname = bucketname.substring(0, bucketname.indexOf("."));

			OSSObject object = client.getObject(bucketname, prefix);
			MultipartFile mf = new MyMultiPartFile(object);
			return mf;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}

	}
}
