package huxg.framework.util.encrypt;

import java.security.MessageDigest;

/**
 * <p>
 * 
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class Encryption {
	public static void main(String[] args) throws Exception {
	}

	/**
	 * MD5
	 * 
	 * @param sString
	 *            String
	 * 
	 * @throws Exception
	 *             
	 * 
	 * @return String
	 */
	public static String encodeMD5(String sString) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");

		md5.update(sString.getBytes());
		byte[] abResult = md5.digest();

		return TypeConvertUtils.convertByte2Hex(abResult);
	}

	/**
	 * DES
	 * 
	 * @param sString
	 *            String
	 * 
	 * @throws Exception
	 *             
	 * 
	 * @return String
	 */
	public static String encode_DES(String astr, String rawkey) throws Exception {
		return DES.encrypt(astr, rawkey);
	}

	public static String decode_DES(String astr, String rawkey) throws Exception {
		return DES.decrypt(astr, rawkey);
	}

	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs.append("0").append(stmp);
			} else {
				hs.append(stmp);
			}
		}
		return hs.toString();
	}

	public static byte[] hex2byte(String hex) throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}
}
