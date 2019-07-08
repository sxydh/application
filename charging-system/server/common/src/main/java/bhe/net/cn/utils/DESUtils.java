package bhe.net.cn.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DESUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(DESUtils.class);

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        String data = "data that need to be encrypt";
        LOGGER.info(data);
        String encrypt = DESUtils.encrypt(data, "0123456789abcdef");
        LOGGER.info(encrypt);
        String decrypt = DESUtils.decrypt(encrypt, "01234567");
        LOGGER.info(decrypt);
        long end = System.currentTimeMillis();
        LOGGER.info(String.valueOf(end - start));
    }

    public static String encrypt(String data, String key) throws Exception {
        return Base64.getEncoder().encodeToString(doDES(Cipher.ENCRYPT_MODE, data.getBytes(), key.getBytes()));
    }

    public static String decrypt(String data, String key) throws Exception {
        return new String(doDES(Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data), key.getBytes()));
    }

    public static byte[] doDES(int mode, byte[] data, byte[] key) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        SecretKey secret = factory.generateSecret(new DESKeySpec(key));
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        if (mode == Cipher.ENCRYPT_MODE) {
            desCipher.init(Cipher.ENCRYPT_MODE, secret);
            return desCipher.doFinal(data);
        } else if (mode == Cipher.DECRYPT_MODE) {
            desCipher.init(Cipher.DECRYPT_MODE, secret);
            return desCipher.doFinal(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
