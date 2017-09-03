package huxg.framework.plugin.sms.rest;

import java.net.URLEncoder;

import huxg.framework.config.SysConfiguration;
import huxg.framework.plugin.sms.ISMSProvider;
import huxg.framework.util.HttpUtils;
import huxg.framework.util.StringUtils;

/**
 * Restful鐗堟湰鐨勫疄鐜�
 * 
 * @author huxg
 * 
 */
public class RestfulSMSProvider implements ISMSProvider {
	String serviceUrl = ".....";
	String user = "test";
	String password = "test";

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean sendMessage(String mobile, String message) {
		String enabledSms = SysConfiguration.getInstance().getProperty("sms.interface.enable");
		boolean smsEnabled = StringUtils.isEmpty(enabledSms) ? false : Boolean.parseBoolean(enabledSms);
		if (smsEnabled) {
			try {
				message = URLEncoder.encode(message, "GBK");
				String params = "?username=" + user + "&password=" + password + "&mobile=" + mobile + "&message=" + message;
				String zt = HttpUtils.get4String(serviceUrl + params, null);
				if (!StringUtils.isEmpty(zt)) {
					int i = Integer.parseInt(zt);
					if (i < 0) {
						return false;
					} else {
						return true;
					}
				}
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
