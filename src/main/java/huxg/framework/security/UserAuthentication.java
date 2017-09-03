package huxg.framework.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

//import javax.servlet.http.HttpSession;

public class UserAuthentication {
	public static boolean isLogin(HttpSession session) {
		return session.getAttribute("SESSION_User") != null;
	}

	public static JSONObject getLoginUserInfo(HttpServletRequest request) {
		// 从缓存中取得用户信息
		HttpSession session = request.getSession();
		JSONObject user = (JSONObject) session.getAttribute("SESSION_User");

		if (user == null)
			return null;
		return user;
	}

}
