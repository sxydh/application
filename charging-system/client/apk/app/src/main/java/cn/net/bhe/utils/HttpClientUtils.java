package cn.net.bhe.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

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
