package huxg.framework.util.uuid;

import java.util.Properties;

public final class Key {
	public static final String key() {
		Properties props = new Properties();
		props.setProperty("separator", "");
		HexUUIDGenerator gen = new HexUUIDGenerator();
		gen.configure(props);

		return (String) gen.generate();
	}

	/**
	 * 生成6位数字验证码
	 * 
	 * @return
	 */
	public static final String generate6Number() {
		String s = "";
		while (s.length() < 6)
			s += (int) (Math.random() * 10);
		return s;
	}
	
	/**
	 * 随机码生成工具
	 * 
	 * @return
	 */
	public static String generate8bitCode() {
		String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
		int MaxPos = chars.length();
		String code = "";
		for (int i = 0; i < 8; i++) {
			code += chars.charAt((int) Math.floor(Math.random() * MaxPos));
		}
		return code;

	}
}