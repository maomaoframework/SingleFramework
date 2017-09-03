package huxg.framework.plugin.sms;

/**
 * 短信服务者
 * @author huxg
 *
 */
public interface ISMSProvider {
	boolean sendMessage(String mobile, String message);
}
