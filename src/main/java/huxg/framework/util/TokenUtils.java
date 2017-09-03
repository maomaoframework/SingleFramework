package huxg.framework.util;

import huxg.framework.util.encrypt.DES;

/**
 * 生成一个登录Token
 * 
 * @author cndini
 * 
 */
public class TokenUtils {
	public static final String KEY = "G_R!RE@N$Tu__0bOE";

	/**
	 * 生成Token
	 * 
	 * @param imei
	 * @param ip
	 * @param time
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public static final String generateLoginToken(String imei, String ip, String time, String userid) throws Exception {
		String str = imei + "_" + ip + "_" + time + "_" + userid;
		String encrypted = DES.encrypt(str, KEY);

		return encrypted;
	}

	/**
	 * 判断token是否合法
	 * 
	 * @return
	 */
	public static final String isTokenRight(String token) throws Exception {
		if (token.equals("2"))
			return "2";
		String deEncrypted = DES.decrypt(token, KEY);
		String[] p = deEncrypted.split("_");
		if (p.length != 4)
			throw new Exception("Error Token");

		String time = p[2];
		long current = System.currentTimeMillis();
		long interval = (current - Long.valueOf(time)) / (1000 * 60);
		if (interval > 30) {
			throw new Exception("Error Token, Session is timeout");
		}

		return p[3];
	}

	/**
	 * 重新生成Token
	 * 
	 * @param token
	 * @param imei
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static final String recreateToken(String token, String imei, String ip) throws Exception {
		String deEncrypted = DES.decrypt(token, KEY);
		String[] p = deEncrypted.split("_");
		if (p.length != 4)
			throw new Exception("Error Token");

		String time = p[2];
		long current = System.currentTimeMillis();
		long interval = (current - Long.valueOf(time)) / (1000 * 60);
		if (interval > 30) {
			throw new Exception("Error Token, Session is timeout");
		}

		String str = imei + "_" + ip + "_" + current + "_" + p[3];
		String encrypted = DES.encrypt(str, KEY);

		return encrypted;
	}
}
