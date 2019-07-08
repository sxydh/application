package bhe.net.cn.utils;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AESUtils {
    ;
    static final String SALT = "salt";
    static final Logger LOGGER = LoggerFactory.getLogger(AESUtils.class);

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        String data = "data that need to be encrypt";
        LOGGER.info(data);
        String encrypt = AESUtils.encrypt(data, "1234562");
        LOGGER.info(encrypt);
        String decrypt = AESUtils.decrypt(encrypt, "1234562");
        LOGGER.info(decrypt);
        long end = System.currentTimeMillis();
        LOGGER.info(String.valueOf(end - start));
    }

    private static String doAES(int mode, String data, String secret) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), SALT.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String encrypt(String strToEncrypt, String secret) throws Exception {
        return doAES(Cipher.ENCRYPT_MODE, strToEncrypt, secret);
    }

    public static String decrypt(String strToDecrypt, String secret) throws Exception {
        return doAES(Cipher.DECRYPT_MODE, strToDecrypt, secret);
    }

}