package bhe.net.cn.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum RSAUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);

    private static RSAPrivateKey privateKey;
    private static RSAPublicKey publicKey;
    static {
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            privateKey = (RSAPrivateKey) keyPair.getPrivate();
            publicKey = (RSAPublicKey) keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    public static String encrypt(String data) throws Exception {
        return Base64.getEncoder().encodeToString(doRSA(Cipher.ENCRYPT_MODE, data.getBytes()));
    }

    public static String decrypt(String data) throws Exception {
        return new String(doRSA(Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data)));
    }

    public static byte[] doRSA(int mode, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) throws Exception {
        String data = "data that needs to be encrypted";
        System.out.printf("%-10s%s%n", "data:", data);
        String encrypt = encrypt(data);
        System.out.printf("%-10s%s%n", "encrypt:", encrypt);
        String decrypt = decrypt(encrypt);
        System.out.printf("%-10s%s%n", "decrypt:", decrypt);
    }
}
