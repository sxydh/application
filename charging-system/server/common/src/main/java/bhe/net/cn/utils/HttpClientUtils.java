package bhe.net.cn.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum HttpClientUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    public static void main(String[] args) throws Exception {

    }

    public static String get(String url, Map<String, Object> paramMap) throws Exception {
        String response = "";
        CloseableHttpClient client = HttpClients.createDefault();
        String params = "";
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                params += "&" + key + "=" + paramMap.get(key);
            }
            params = "?" + params.substring(1);
        }
        HttpGet get = new HttpGet(url + params);
        HttpResponse rs = client.execute(get);
        int status = rs.getStatusLine().getStatusCode();
        LOGGER.info("response status: " + status);
        if (status == HttpStatus.SC_OK) {
            response = EntityUtils.toString(rs.getEntity());
        } else {
            throw new ClientProtocolException(String.valueOf(status));
        }
        client.close();
        return response;
    }

    public static String postWithJson(String url, String jsonStr) throws Exception {
        String response = "";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        if (StringUtils.isNotEmpty(jsonStr)) {
            StringEntity se = new StringEntity(jsonStr, Consts.UTF_8);
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            post.setEntity(se);
        }
        HttpResponse rs = client.execute(post);
        int status = rs.getStatusLine().getStatusCode();
        LOGGER.info("response status: " + status);
        if (status == HttpStatus.SC_OK) {
            response = EntityUtils.toString(rs.getEntity());
        } else {
            throw new ClientProtocolException(String.valueOf(status));
        }
        client.close();
        return response;
    }

    public static String postWithForm(String url, Map<String, Object> formMap) throws Exception {
        String response = "";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        if (formMap != null) {
            List<NameValuePair> form = new ArrayList<>();
            for (String key : formMap.keySet()) {
                form.add(new BasicNameValuePair(key, formMap.get(key).toString()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
            post.setEntity(entity);
        }
        ResponseHandler<String> responseHandler = handler -> {
            int status = handler.getStatusLine().getStatusCode();
            LOGGER.info("response status: " + status);
            if (status == HttpStatus.SC_OK) {
                HttpEntity responseEntity = handler.getEntity();
                return responseEntity != null ? EntityUtils.toString(responseEntity) : null;
            } else {
                throw new ClientProtocolException(String.valueOf(status));
            }
        };
        response = client.execute(post, responseHandler);
        return response;
    }
}
