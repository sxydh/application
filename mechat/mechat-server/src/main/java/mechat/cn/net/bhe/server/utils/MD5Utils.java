package mechat.cn.net.bhe.server.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum MD5Utils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(MD5Utils.class);

    public static String toLowerStr(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytes = toBytes(data);
        char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v / 16];
            hexChars[j * 2 + 1] = hexArray[v % 16];
        }
        return new String(hexChars);
    }

    public static String toUpperStr(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return toLowerStr(data).toUpperCase();
    }

    public static byte[] toBytes(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] src = data.getBytes(StandardCharsets.UTF_8);
        md5.update(src);
        return md5.digest();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String data = "data that needs to be encrypted ";
        System.out.println("data: " + data);
        String result = toLowerStr(data);
        System.out.println("result: " + result);
    }
}
