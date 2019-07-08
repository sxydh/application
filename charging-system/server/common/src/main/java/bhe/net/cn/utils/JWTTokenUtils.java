package bhe.net.cn.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public enum JWTTokenUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(JWTTokenUtils.class);

    public static String createToken(int minute, String password, Map<String, Object> claims) {
        // issuance time
        Date isDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        Date expireTime = calendar.getTime();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        Builder builder = JWT.create().withHeader(map);
        for (String key : claims.keySet()) {
            Object temp = claims.get(key);
            if (temp instanceof Boolean) {
                Boolean value = (Boolean) temp;
                builder.withClaim(key, value);
            }
            if (temp instanceof Date) {
                Date value = (Date) temp;
                builder.withClaim(key, value);
            }
            if (temp instanceof Double) {
                Double value = (Double) temp;
                builder.withClaim(key, value);
            }
            if (temp instanceof Integer) {
                Integer value = (Integer) temp;
                builder.withClaim(key, value);
            }
            if (temp instanceof Long) {
                Integer value = (Integer) temp;
                builder.withClaim(key, value);
            }
            if (temp instanceof String) {
                String value = (String) temp;
                builder.withClaim(key, value);
            }
        }
        return builder.withExpiresAt(expireTime).withIssuedAt(isDate).sign(Algorithm.HMAC256(password));
    }

    public static Boolean getBoolean(String token, String key) {
        return JWT.decode(token).getClaim(key).asBoolean();
    }

    public static Date getDate(String token, String key) {
        return JWT.decode(token).getClaim(key).asDate();
    }

    public static Double getDouble(String token, String key) {
        return JWT.decode(token).getClaim(key).asDouble();
    }

    public static Integer getInteger(String token, String key) {
        return JWT.decode(token).getClaim(key).asInt();
    }

    public static Long getLong(String token, String key) {
        return JWT.decode(token).getClaim(key).asLong();
    }

    public static String getString(String token, String key) {
        return JWT.decode(token).getClaim(key).asString();
    }

    public static boolean verify(String token, String password) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password)).build();
        try {
            @SuppressWarnings("unused")
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("name", "Bruce Wayne");
        payload.put("vocation", "Hero");
        payload.put("status", 1);
        String token = JWTTokenUtils.createToken(1, "123456", payload);

        System.out.printf("%-10s%s%n", "token:", token);
        //token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2b2NhdGlvbiI6Ikhlcm8iLCJuYW1lIjoiQnJ1Y2UgV2F5bmUiLCJleHAiOjE1NDIwOTAwNjYsImlhdCI6MTU0MjA5MDAwNiwic3RhdHVzIjoxfQ.6xWRBMtHKcF-ZQ9wVY8MC6gV8JJms9rdTc3CLeJYHDE";
        System.out.printf("%-10s%s%n", "verify:", verify(token, "123456"));
        System.out.printf("%-10s%s%n", "name:", getString(token, "name"));
    }
}
