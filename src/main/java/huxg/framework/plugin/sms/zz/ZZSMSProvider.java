package huxg.framework.plugin.sms.zz;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import huxg.framework.config.SysConfiguration;
import huxg.framework.plugin.sms.ISMSProvider;
import huxg.framework.util.HttpUtils;
import huxg.framework.util.StringUtils;

/**
 * 短信通版本的短信收发类
 * 
 * @author huxg
 * 
 */
public class ZZSMSProvider implements ISMSProvider {
	@Override
	public boolean sendMessage(String mobile, String message) {
		String enabledSms = SysConfiguration.getInstance().getProperty("sms.interface.enable");
		String url = SysConfiguration.getInstance().getProperty("sms.duanxintong.interface.url");
		String username = SysConfiguration.getInstance().getProperty("sms.duanxintong.username");
		String password = SysConfiguration.getInstance().getProperty("sms.duanxintong.password");
		String prefix = SysConfiguration.getInstance().getProperty("sms.duanxintong.message.prefix");

		boolean smsEnabled = StringUtils.isEmpty(enabledSms) ? false : Boolean.parseBoolean(enabledSms);
		if (smsEnabled) {
			try {
				// 针对message做处理
				String content = URLEncoder.encode(message, "UTF-8");

				Map<String, String> params = new HashMap<String, String>();
				params.put("UserName", username);
				params.put("UserPass", password);
				params.put("Content", content);
				params.put("Mobile", mobile);
				params.put("Subid", "");

				String zt = HttpUtils.get4String(url, params);
				if ("00".equals(zt) || "03".equals(zt)) {
					System.out.println("发送成功");
				} else {
					System.out.println("发送失败，返回值：" + zt);
				}
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
