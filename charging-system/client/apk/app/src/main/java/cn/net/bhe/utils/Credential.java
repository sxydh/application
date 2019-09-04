package cn.net.bhe.utils;

import java.util.HashMap;
import java.util.Map;

public class Credential {

    private static final Map<String, String> cookies = new HashMap<>();

    public static String getCookie(String host) {
        return cookies.get(host);
    }

    public static void setCookie(String host, String value) {
        cookies.put(host, value);
    }

}
