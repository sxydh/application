package bhe.net.cn.utils;

import java.security.MessageDigest;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SHAUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(SHAUtils.class);

    public static String encrypt(String data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("SHA");
        byte[] src = data.getBytes();
        md5.update(src);
        byte[] encrypt = md5.digest();
        return Base64.getEncoder().encodeToString(encrypt);
    }

    public static void main(String[] args) throws Exception {
        String data = "data that needs to be encrypted ";
        System.out.printf("%-10s%s%n", "data:", data);
        System.out.printf("%-10s%s%n", "encrypt:", encrypt(data));
    }
}
