package huxg.framework.util.encrypt;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p> MD5 </p>
 * 
 * @author 
 * @version 1.0
 */
public class MD5 implements Serializable {
    private static final long serialVersionUID = 0;

    private MessageDigest md;

    public MD5() {
        try {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsae) {
        }
    }

    /**
     * MD5
     * 
     * @param source
     *            String
     * 
     * @throws Exception
     *             
     * 
     * @return byte[]
     */
    public byte[] getDigest(String source) throws Exception {
        if ( source == null ) {
            return null;
        }

        md.update(source.getBytes());
        return md.digest();
    }

    /**
     * 
     * 
     * @param bytes
     *            byte[]
     * @return String
     */
    public String byteToHex(byte[] bytes) {
        if ( bytes == null ) {
            return null;
        }

        String hex = "", temp = "";

        for ( int i = 0; i < bytes.length; i++ ) {
            temp = Integer.toHexString(bytes[i] & 0XFF);
            if ( 1 == temp.length() ) {
                hex += "0" + temp;
            }
            else {
                hex += temp;
            }
        }

        return hex;
    }
}
