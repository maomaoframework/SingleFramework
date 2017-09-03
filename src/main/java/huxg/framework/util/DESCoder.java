package huxg.framework.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESCoder {

	private static final String DES_ALGORITHM = "DES";  
	
	/**
	 * DES加密
	 * @param datasource
	 * @param secretKey
	 * @return
	 * @throws Exception
	 */
	public static String encryption(String datasource, String secretKey) throws Exception{
		if(datasource!=null&&!datasource.equals("")&&secretKey!=null&&!secretKey.equals("")){
			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);  
	        cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey));  
	        
	        byte[] buf = cipher.doFinal(datasource.getBytes());
	        
	        return BASE64.encryptBASE64(buf);
		}
		return "";
	}
	
	/**
	 * DES解密
	 * @param secretData
	 * @param secretKey
	 * @return
	 * @throws Exception
	 */
	public static String decryption(String secretData, String secretKey) throws Exception { 
		if(secretData!=null&&!secretData.equals("")&&secretKey!=null&&!secretKey.equals("")){
			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);  
			cipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey)); 
	         
			byte[] buf = cipher.doFinal(BASE64.decryptBASE64(secretData));
	         
			return new String(buf);  
		}
		return "";
	}
	
	/**
	 * 获取密钥
	 * @param secretKey
	 * @return
	 * @throws Exception
	 */
	private static SecretKey generateKey(String secretKey) throws Exception{
		// 创建一个DESKeySpec对象  
        DESKeySpec desKey = new DESKeySpec(secretKey.getBytes()); 
    	// 创建一个密匙工厂  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        // 将DESKeySpec对象转换成SecretKey对象  
        return keyFactory.generateSecret(desKey); 
	}
	
	static class BASE64 {
		/**
		 * 解密
		 * @param key
		 * @return
		 * @throws Exception
		 */
		public static byte[] decryptBASE64(String key) throws Exception {
    		return (new BASE64Decoder()).decodeBuffer(key);
    	}
        
		/**
		 * 加密
		 * @param key
		 * @return
		 * @throws Exception
		 */
        public static String encryptBASE64(byte[] key) throws Exception {
        	return (new BASE64Encoder()).encodeBuffer(key);
    	}
	}
	
}
