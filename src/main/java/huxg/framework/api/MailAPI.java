package huxg.framework.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import huxg.framework.config.SysConfiguration;
import huxg.framework.util.StringUtils;
import huxg.framework.util.mail.MailSenderInfo;
import huxg.framework.util.mail.SimpleMailSender;
import huxg.framework.vo.Message;

/**
 * 邮件发送API类
 * 
 * @author apple
 * 
 */
public class MailAPI {
	private static final Log logger = LogFactory.getLog(MailAPI.class);

	public static String sendMail(String targetMailAddress, String title, String content, boolean isHtml) {
		String strEnable = SysConfiguration.getInstance().getProperty("mailserver.enable");
		boolean enableMailServer = StringUtils.isEmpty(strEnable) ? false : Boolean.parseBoolean(strEnable);
		if (!enableMailServer) 
			return Message.okMessage();
		
		if (StringUtils.isEmpty(targetMailAddress)) {
			if (logger.isDebugEnabled())
				logger.debug("企图向" + targetMailAddress + "发送邮件标题为空的邮件，已被过滤。");
			return Message.errorMessage("企图向" + targetMailAddress + "发送邮件标题为空的邮件，已被过滤。");
		}

		if (StringUtils.isEmpty(content)) {
			if (logger.isDebugEnabled())
				logger.debug("企图向" + targetMailAddress + "发送邮件内容为空的邮件，已被过滤。");
			return Message.errorMessage("企图向" + targetMailAddress + "发送邮件内容为空的邮件，已被过滤。");
		}

		try {
			SysConfiguration sys = SysConfiguration.getInstance();
			String port = sys.getProperty("mailserver.port");
			String host = sys.getProperty("mailserver.host");
			String user = sys.getProperty("mailserver.user");
			String from = sys.getProperty("mailserver.from");
			String pwd = sys.getProperty("mailserver.pwd");
			String validate = sys.getProperty("mailserver.validate");

			// 设置邮件服务器信息
			MailSenderInfo mailInfo = new MailSenderInfo();
			mailInfo.setMailServerHost(host);
			mailInfo.setMailServerPort(port);
			try {
				mailInfo.setValidate(Boolean.parseBoolean(validate));
			} catch (Exception e) {
			}
			mailInfo.setUserName(user);
			mailInfo.setPassword(pwd);
			mailInfo.setFromAddress(from);
			mailInfo.setToAddress(targetMailAddress);
			mailInfo.setSubject(title);
			mailInfo.setContent(content);

			// 发送邮件
			SimpleMailSender sms = new SimpleMailSender();
			if (isHtml)
				SimpleMailSender.sendHtmlMail(mailInfo);
			else
				sms.sendMailNews(mailInfo);

			if (logger.isDebugEnabled()) {
				logger.debug("向" + targetMailAddress + "发送了一封邮件：" + title);
				return Message.okMessage();
			}

			return Message.errorMessage("信息不完整，邮件发送失败");
		} catch (Exception e) {
			if (logger.isDebugEnabled())
				logger.debug("向" + targetMailAddress + "发送一封邮件时系统出错", e);
			return Message.errorMessage("向" + targetMailAddress + "发送一封邮件时系统出错：" + e.getMessage());
		}
	}

}
