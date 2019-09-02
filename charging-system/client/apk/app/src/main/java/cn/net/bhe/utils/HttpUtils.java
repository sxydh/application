package cn.net.bhe.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    private static final String HOST = "http://192.168.18.239:8080/app";

    public static void main(String[] args) {
        String result = null;
        /*
        try {
            Map<String, Object> map = HttpUtils.post(
                    "/user/login",
                    "{"
                            + "\"phone\": \"15186942525\","
                            + "\"password\": \"670b14728ad9902aecba32e22fa4f6bd\","
                            + "\"type\": 2"
                            + "}");
            result = JacksonUtils.objToJsonStr(map.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        try {
            result = JacksonUtils.objToJsonStr(HttpUtils.get("/wallet/get", new HashMap<>()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    // HTTP GET request
    public static Map<String, Object> get(String url, Map<String, String> paramMap) throws Exception {
        System.out.println(HOST + url + ", " + paramMap.toString());

        Map<String, Object> response = new HashMap<>();

        String params = "";
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                params += "&" + key + "=" + paramMap.get(key);
            }
            if (params.length() > 0) {
                params = "?" + params.substring(1);
            }
        }

        URL obj = new URL(HOST + url + params);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        conn.setRequestMethod("GET");
        conn.setRequestProperty("cookie", "SESSION=114eb502-a083-4123-b2a5-e47df5970e61");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");

        int code = conn.getResponseCode();
        response.put("code", code);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuffer body = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        response.put("body", body);
        reader.close();

        return response;

    }

    // HTTP POST request
    public static Map<String, Object> post(String url, String jsonStr) throws Exception {
        System.out.println(HOST + url + ", " + jsonStr);

        Map<String, Object> response = new HashMap<>();

        URL obj = new URL(HOST + url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");

        conn.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());

        if (jsonStr != null && !jsonStr.isEmpty()) {
            out.writeBytes(jsonStr);
        }

        out.flush();
        out.close();

        int code = conn.getResponseCode();
        response.put("code", code);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuffer body = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        response.put("body", body);
        reader.close();

        return response;

    }
}
