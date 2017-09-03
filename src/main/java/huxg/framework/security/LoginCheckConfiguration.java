package huxg.framework.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Element;

import huxg.framework.util.dom4j.XmlUtils;
import huxg.framework.web.servlet.SysLoaderServlet;

/**
 * 登录检查配置类
 * 
 * @author apple
 * 
 */
public class LoginCheckConfiguration {
	Map<String, String> includePath = new HashMap<String, String>();
	Map<String, String> excludePath = new HashMap<String, String>();

	private static LoginCheckConfiguration instance = null;

	public static LoginCheckConfiguration getInstance() {
		if (null == instance) {
			instance = new LoginCheckConfiguration();
			instance.init();
		}
		return instance;
	}

	private LoginCheckConfiguration() {

	}

	private void init() {
		// 读取配置文件
		String rpath = SysLoaderServlet.getRealPath();
		String webxmlPath = rpath + "/WEB-INF/loginchecker.xml";
		FileInputStream in = null;
		XmlUtils xml = new XmlUtils();
		try {
			in = new FileInputStream(webxmlPath);
			Document document = xml.load(in);
			Element root = document.getRootElement();

			// 读取include
			Element elIn = root.element("include");
			List<Element> logincheckElements = elIn.elements("item");

			// 读取exclude
			Element elEx = root.element("exclude");
			List<Element> nochecker = elEx.elements("item");

			// 分别将路径加入到Map中去
			for (Element e : logincheckElements) {
				includePath.put(e.attributeValue("path"), e.attributeValue("path"));
			}
			for (Element e : nochecker) {
				excludePath.put(e.attributeValue("path"), e.attributeValue("path"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void include() {

	}

	/**
	 * 判断是否是排除的路径
	 * 
	 * @param path
	 * @return
	 */
	public boolean exclude(String path) {
		Iterator<String> iter = excludePath.keySet().iterator();
		String p;
		while (iter.hasNext()) {
			p = iter.next();
			if (path.startsWith(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是包含的路径
	 * 
	 * @param path
	 * @return
	 */
	public boolean include(String path) {
		Iterator<String> iter = includePath.keySet().iterator();
		String p;
		while (iter.hasNext()) {
			p = iter.next();
			if (path.startsWith(p)) {
				return true;
			}

			if (Pattern.matches(p, path)) {
				return true;
			}
		}
		return false;
	}
}
