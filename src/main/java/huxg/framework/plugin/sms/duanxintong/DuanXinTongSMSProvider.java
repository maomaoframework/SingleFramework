package huxg.framework.plugin.sms.duanxintong;

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
public class DuanXinTongSMSProvider implements ISMSProvider {
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
				if (!message.startsWith("【")) {
					message = "【" + prefix + "】" + message;
				}
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", username);
				params.put("password", password);
				params.put("message", message);
				params.put("mobile", mobile);

				String zt = HttpUtils.post4String(url, params);
				if (!StringUtils.isEmpty(zt) &&zt.length() == 20) {
					return true;
				}
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
