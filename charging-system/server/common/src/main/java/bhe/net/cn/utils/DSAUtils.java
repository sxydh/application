package bhe.net.cn.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DSAUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(DESUtils.class);

    private static final String KEY_ALGORITHM = "DSA";
    private static final String SIGNATURE_ALGORITHM = "DSA";
    private static final String DEFAULT_SEED = "seed";

    private static final Decoder DECODER = Base64.getDecoder();
    private static final Encoder ENCODER = Base64.getEncoder();
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    static {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(DEFAULT_SEED.getBytes());
            keygen.initialize(640, secureRandom);
            KeyPair keys = keygen.genKeyPair();
            privateKey = keys.getPrivate();
            publicKey = keys.getPublic();
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        LOGGER.info(getPrivateKey());
        LOGGER.info(getPublicKey());
        String data = "data that needs to be encrypted";
        String sign = sign(data);
        boolean verifyFake = verify("fake data that needs to be encrypted", sign);
        LOGGER.info(String.valueOf(verifyFake));
        boolean verify = verify(data, sign);
        LOGGER.info(String.valueOf(verify));
    }

    public static String getPrivateKey() {
        return new String(ENCODER.encode(privateKey.getEncoded()));
    }

    public static String getPublicKey() {
        return new String(ENCODER.encode(publicKey.getEncoded()));
    }

    public static String sign(String data) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = factory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data.getBytes());
        return new String(ENCODER.encode(signature.sign()));
    }

    public static boolean verify(String data, String sign) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data.getBytes());
        return signature.verify(DECODER.decode(sign));
    }
}
