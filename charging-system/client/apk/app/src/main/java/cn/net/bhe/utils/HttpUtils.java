package cn.net.bhe.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    public static void main(String[] args) throws Exception {

        System.out.println(HttpUtils.get("https://www.baidu.com/", null));

        /*
        String jsonStr = "{\"phone\":\"15186942525\",\"password\":\"670b14728ad9902aecba32e22fa4f6bd\"}";
        System.out.println(HttpUtils.post("http://localhost:8080/user/login", jsonStr));
        */

    }

    // HTTP GET request
    public static Map<String, Object> get(String url, Map<String, String> paramMap) throws Exception {
        Map<String, Object> response = new HashMap<>();

        String params = "";
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                params += "&" + key + "=" + paramMap.get(key);
            }
            params = "?" + params.substring(1);
        }

        URL obj = new URL(url + params);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        conn.setRequestMethod("GET");

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
        Map<String, Object> response = new HashMap<>();

        URL obj = new URL(url);
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
