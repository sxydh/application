package bhe.net.cn.api.greenyep;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import bhe.net.cn.exception.BusinessException;
import bhe.net.cn.utils.HttpClientUtils;
import bhe.net.cn.utils.JacksonUtils;

public class GreenYepPay {

    static final Logger LOGGER = LoggerFactory.getLogger(GreenYepPay.class);

    public static GYPResponse execute(Config config) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(config.getGoodname())) {
            map.put("goodname", config.getGoodname());
        }
        if (config.getType() != null) {
            map.put("type", config.getType());
        }
        if (StringUtils.isNotEmpty(config.getAccount())) {
            map.put("account", config.getAccount());
        }
        if (StringUtils.isNotEmpty(config.getNotifyurl())) {
            map.put("notifyurl", config.getNotifyurl());
        }
        if (StringUtils.isNotEmpty(config.getReturnurl())) {
            map.put("returnurl", config.getReturnurl());
        }
        if (StringUtils.isNotEmpty(config.getOrdernum())) {
            map.put("ordernum", config.getOrdernum());
        }
        if (StringUtils.isNotEmpty(config.getOrderuid())) {
            map.put("orderuid", config.getOrderuid());
        }
        if (config.getAmount() != null) {
            map.put("amount", config.getAmount());
        }
        if (config.getMode() != null) {
            map.put("mode", config.getMode());
        }
        if (StringUtils.isNotEmpty(config.getUid())) {
            map.put("uid", config.getUid());
        }
        if (StringUtils.isNotEmpty(config.getKey())) {
            map.put("key", config.getKey());
        }
        String jsonStr = HttpClientUtils.postWithForm(config.getUrl(), map);
        LOGGER.info(jsonStr);
        JsonNode node = JacksonUtils.readTree(jsonStr);
        if (Arrays.asList(new String[] { "400" }).contains(node.findValue("code").toString())) {
            throw new BusinessException("Payment amount is too low !");
        }
        GYPResponse gypresponse = JacksonUtils.jsonStrToObj(node.findValue("content").toString(), GYPResponse.class);
        return gypresponse;
    }

    public static void main(String[] args) throws Exception {
    }
}
