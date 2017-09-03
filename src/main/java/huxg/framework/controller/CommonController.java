package huxg.framework.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import huxg.framework.config.SysConfiguration;
import huxg.framework.dao.redis.JedisTemplate;
import huxg.framework.dao.redis.JedisTemplate.JedisAction;
import huxg.framework.dao.redis.JedisTemplate.JedisActionNoResult;
import huxg.framework.dao.redis.RedisDaoSupportImpl;
import huxg.framework.filter.parameter.ParameterWrapper;
import huxg.framework.plugin.fileupload.UploadFileManager;
import huxg.framework.plugin.sms.ISMSProvider;
import huxg.framework.util.AddressUtils;
import huxg.framework.util.JsonUtils;
import huxg.framework.util.SpringUtils;
import huxg.framework.util.StringUtils;
import huxg.framework.util.myutil.RandomUtil;
import huxg.framework.vo.Message;
import huxg.framework.web.controller.BaseController;

/**
 * 公共服务
 * 
 * @author huxg
 * 
 */
@Controller("CommonController")
@RequestMapping("/wbs")
public class CommonController extends BaseController {
	@Resource(name = "jedisTemplate")
	private JedisTemplate jedisTemplate;

	/**
	 * 为当前用户定位所在省
	 * 
	 * @return
	 */
	@RequestMapping(value = "/locateProvince")
	public @ResponseBody
	String locateProvince() {
		try {
			String[] region = AddressUtils.getProvince(getRequest());
			if (region == null) {
				region = new String[] { "110000", "北京" };
			}
			return Message.okMessage(new String[] { "region_id", "region_name" }, new String[] { region[0], region[1] });
		} catch (Exception e) {
			return Message.error();
		}
	}

	/**
	 * 用于判断是否是正确来源，本服务类只接受我们网站自己的服务请求
	 * 
	 * @return
	 */
	private boolean isValidFrom() {
		HttpServletRequest request = ParameterWrapper.getWrapper().getRequest();
		String domainName = request.getServerName();

		if (domainName.startsWith("192.168.") || domainName.startsWith("10.")) {
			return true;
		}

		if (StringUtils.isEmpty(domainName))
			return false;

		if (domainName.contains("localhost"))
			return true;

		if (!domainName.contains(".com"))
			return false;

		String dm = SysConfiguration.getInstance().getProperty("document_domain");

		if (StringUtils.isEmpty(dm) || StringUtils.isEmpty(domainName))
			return false;

		if (dm.length() > domainName.length() && dm.startsWith(domainName))
			return true;

		if (domainName.length() > dm.length() && (domainName.startsWith(dm) || domainName.endsWith(dm)))
			return true;

		return false;
	}

	/**
	 * 校验验证码是否有效
	 * 
	 * @return
	 */
	@RequestMapping(value = "/is_verify_code_valid", produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String is_verify_code_valid() throws Exception {
		String mobile = (String) getParameter("mobile");
		String verifyCode = (String) getParameter("verifyCode");
		try {
			checkMobileVerifyCode(mobile, verifyCode);
			return Message.okMessage();
		} catch (Exception e) {
			return Message.errorMessage(e.getMessage());
		}
	}

	/**
	 * 校验验证码是否有效
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void checkMobileVerifyCode(final String phone, final String verifyCode) throws Exception {
		boolean EnablePhoneVerifyCode = Boolean.parseBoolean(SysConfiguration.getInstance().getProperty("sms.interface.enable"));
		if (!EnablePhoneVerifyCode) {
			return;
		}

		String realCode = (String) jedisTemplate.execute(new JedisAction() {
			@Override
			public Object action(Jedis jedis) {
				String realCode = jedis.hget(RedisDaoSupportImpl.POOL + "MobileVerifyCode", phone);
				return realCode;
			}
		});

		if (realCode.contains(",")) {
			String[] str = realCode.split(",");
			long time = Long.parseLong(str[1]);
			long now = System.currentTimeMillis();
			long minute = (now - time) / 1000 / 60;
			if (minute < 10 && verifyCode.equals(str[0])) {
				return;
			}
		}
		throw new Exception("手机验证码无效");
	}

	/**
	 * 发送手机验证码
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sendMobileVerifyCode")
	public @ResponseBody
	String sendMobileVerifyCode() {
		boolean EnablePhoneVerifyCode = Boolean.parseBoolean(SysConfiguration.getInstance().getProperty("sms.interface.enable"));
		if (!EnablePhoneVerifyCode) {
			return Message.okMessage();
		}

		String msg = (String) getParameter("msg");
		if (!isValidFrom()) {
			return Message.error();
		}

		final String phone = (String) getParameter("phone");

		final String code = RandomUtil.randomVerifyCode();
		try {
			if (StringUtils.isEmpty(msg)) {
				msg = "您的验证码是：" + code;
			} else if (msg.contains("?")) {
				msg = msg.replace("?", code);
			} else {
				msg = msg + "：" + code;
			}

			ISMSProvider sms = (ISMSProvider) SpringUtils.getBean("SMSProvider");
			sms.sendMessage(phone, msg);

			// 将验证码信息保存到redis缓存服务器上，并设置定期清除
			// 从cookie中提取用户进入本网站的唯一标示，将标示与验证码组织后存储到缓存服务器上
			// 在分布式部署环境下，是没有办法从session中获取信息的，因为session是不做同步的。

			jedisTemplate.execute(new JedisActionNoResult() {
				@Override
				public void action(Jedis jedis) {
					long time = System.currentTimeMillis();
					jedis.hset(RedisDaoSupportImpl.POOL + "MobileVerifyCode", phone, code + "," + String.valueOf(time));
				}
			});

			return Message.okMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return Message.errorMessage("验证码发送失败，请检查您的手机号码输入是否有误。");
		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @return
	 */
	@RequestMapping(value = "/imgcut")
	public String imgcut() {
		String preview = (String) getParameter("preview");
		String widthLabel = "120";
		String heightLabel = "120";

		StringBuffer sbOut = new StringBuffer();
		if (preview != null) {
			try {
				preview = URLDecoder.decode(preview, "UTF-8");
				preview = URLDecoder.decode(preview, "UTF-8");
				JSONArray ja = JsonUtils.String2JSONArray(preview);
				int size = ja.size();

				int parentHeight = 0;
				int top = 15;
				int left = 430;
				int maxWidth = 0;
				StringBuffer styleHtml = new StringBuffer();
				for (int i = 0; i < size; i++) {
					JSONObject obj = ja.getJSONObject(i);
					int marginLeft = Integer.parseInt(obj.get("width").toString()) - 120;
					widthLabel = obj.get("width").toString();
					heightLabel = obj.get("height").toString();

					if (i == 0) {
						maxWidth = Integer.parseInt(widthLabel);
					}

					if (i > 0) {
						left = 430 + (maxWidth / 2 - Integer.parseInt(widthLabel) / 2);
					}

					styleHtml.append(".jcrop-holder #preview-pane" + i + " {width: " + obj.get("width") + "px;height: " + obj.get("height")
							+ "px;position: absolute;left: " + left + "px;top:" + String.valueOf(top) + "px;}");
					sbOut.append("<div id=\"preview-pane" + i + "\" class=\"preview-pane\" style=\"width:" + obj.get("width") + "px;height:"
							+ obj.get("height") + "px;margin:0 auto;margin-bottom:30px;\">");
					sbOut.append("<div class=\"border1px no-pic preview-container\" style=\"width:" + obj.get("width") + "px; height:" + obj.get("height")
							+ "px\">");
					sbOut.append("     	 <img src=\"/themes/images/no_pic.jpg\" width=\"" + obj.get("width") + "\" height=\"" + obj.get("height")
							+ "\" alt=\"\" class=\"jcrop-preview\" data-width=\"" + obj.get("width") + "\" data-height=\"" + obj.get("height") + "\"/>");
					sbOut.append("</div>");
					sbOut.append("     <div style=\"text-align: center; margin-top: 4px;\" class=\"tips_img_cut\">" + widthLabel + " x " + heightLabel
							+ " </div>");
					sbOut.append(" </div>");
					top = top + Integer.parseInt(heightLabel) + 30;
				}
				sbOut.append("<style>" + styleHtml.toString() + "</style>");
			} catch (Exception e) {

			}
		} else {
			sbOut.append("<div id=\"preview-pane\" class=\"preview-pane\">");
			sbOut.append("<div class=\"border1px no-pic preview-container\" style=\"width:150px; height:150px\">");
			sbOut.append(" <img src=\"/themes/images/no_pic.jpg\" width=\"150\" height=\"150\" alt=\"\" class=\"jcrop-preview\" data-width=\"150\" data-height=\"150\"/></div>");
			sbOut.append("<div style=\"padding-left : 10px;\">" + widthLabel + " x " + heightLabel + " 像素</div>");
			sbOut.append("</div>");
		}
		setParameter("sbhtml", sbOut.toString());
		return "/public/imgcut";
	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/download", produces = "application/octet-stream")
	public String download(HttpServletRequest request, HttpServletResponse response) {
		// 生成提示信息，
		response.setContentType("application/octet-stream");

		String codedFileName = (String) getParameter("filename");
		if (StringUtils.isEmpty(codedFileName)) {
			try {
				codedFileName = new String(codedFileName.getBytes("ISO8859_1"), "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		OutputStream fOut = null;
		InputStream in = null;
		try {
			String fileUrl = (String) getParameter("file");

			// 进行转码，使其支持中文文件名
			MultipartFile mf = UploadFileManager.getMultipartFileFromUrl(fileUrl);
			in = mf.getInputStream();

			String srcFileName = mf.getName();

			if (StringUtils.isEmpty(codedFileName))
				codedFileName = java.net.URLEncoder.encode(srcFileName, "UTF-8");
			else {
				int spliter = srcFileName.lastIndexOf(".");
				if (spliter != -1) {
					String ext = srcFileName.substring(spliter);
					codedFileName += ext;
				}
			}
			response.setHeader("content-disposition", "attachment;filename=" + codedFileName);

			fOut = response.getOutputStream();

			byte[] data = new byte[1024];
			int count = -1;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fOut.write(data, 0, count);
			}
			fOut.flush();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fOut)
				try {
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
				}
		}
		return null;
	}
}
