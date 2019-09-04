package cn.net.bhe.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils {

    public static void main(String[] args) {
        String result = null;
        Rt rt = HttpUtils.post(
                Host.MAIN,
                "/user/login",
                "{"
                        + "\"phone\": \"15186942525\","
                        + "\"password\": \"670b14728ad9902aecba32e22fa4f6bd\","
                        + "\"type\": 2"
                        + "}");
        Map<String, Object> data = (Map<String, Object>) rt.getData();
        Map<String, Object> user = (Map<String, Object>) data.get("user");
        Credential.setCookie(Host.MAIN, "SESSION=" + user.get("session_id"));
        result = JacksonUtils.objToJsonStr(HttpUtils.get(Host.MAIN, "/wallet/get", null));

        System.out.println(result);
    }

    // HTTP GET request
    public static Rt get(String host, String path, Map<String, String> query) {
        Rt rt;

        String params = "";
        if (query != null && !query.isEmpty()) {
            for (String key : query.keySet()) {
                params += "&" + key + "=" + query.get(key);
            }
            params = "?" + params.substring(1);
        }

        BufferedReader reader = null;
        try {
            URL obj = new URL(host + path + params);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            conn.setRequestMethod("GET");
            conn.setRequestProperty("cookie", Credential.getCookie(host));
            conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_UTF8);
            conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);

            reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer body = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            rt = JacksonUtils.jsonStrToObj(body.toString(), Rt.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rt;
    }

    // HTTP POST request
    public static Rt post(String host, String path, String jsonStr) {
        Rt rt;

        BufferedReader reader = null;
        try {
            URL obj = new URL(host + path);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("cookie", Credential.getCookie(host));
            conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_UTF8);
            conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);

            /* setDoOutput(true) is used for POST and PUT requests. If it is false then it is for using GET requests */
            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            if (jsonStr != null && !jsonStr.isEmpty()) {
                out.writeBytes(jsonStr);
            }

            out.flush();
            out.close();

            reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer body = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            rt = JacksonUtils.jsonStrToObj(body.toString(), Rt.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rt;
    }

    public static class MediaType {

        public static final String ALL = "*/*";
        public static final String APPLICATION_ATOM_XML = "application/atom+xml";
        public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
        public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
        public static final String APPLICATION_PDF = "application/pdf";
        public static final String APPLICATION_PROBLEM_JSON = "application/problem+json";
        public static final String APPLICATION_PROBLEM_JSON_UTF8 = "application/problem+json;charset=UTF-8";
        public static final String APPLICATION_PROBLEM_XML = "application/problem+xml";
        public static final String APPLICATION_RSS_XML = "application/rss+xml";
        public static final String APPLICATION_STREAM_JSON = "application/stream+json";
        public static final String APPLICATION_XHTML_XML = "application/xhtml+xml";
        public static final String APPLICATION_XML = "application/xml";
        public static final String IMAGE_GIF = "image/gif";
        public static final String IMAGE_JPEG = "image/jpeg";
        public static final String IMAGE_PNG = "image/png";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
        public static final String TEXT_EVENT_STREAM = "text/event-stream";
        public static final String TEXT_HTML = "text/html";
        public static final String TEXT_MARKDOWN = "text/markdown";
        public static final String TEXT_PLAIN = "text/plain";
        public static final String TEXT_XML = "text/xml";

    }

    public static class Rt implements Serializable {

        private Integer sc;
        private String msg;
        private Object data;

        public Rt() {
        }

        public Integer getSc() {
            return sc;
        }

        public void setSc(Integer sc) {
            this.sc = sc;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
