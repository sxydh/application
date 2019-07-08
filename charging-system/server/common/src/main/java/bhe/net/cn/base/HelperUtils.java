package bhe.net.cn.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bhe.net.cn.utils.MD5Utils;

public enum HelperUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(HelperUtils.class);

    public static Map<String, Object> lowercaseKey(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        for (String key : map.keySet()) {
            result.put(key.toLowerCase(), map.get(key));
        }
        return result;
    }

    public static String genToken() {
        try {
            return MD5Utils.toLowerStr(UUID.randomUUID().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int creditGet(int value) {
        return (int) Math.round(value * 0.01);
    }

    public static String walletLogRemarkGen(int type, int amount) {
        if (Arrays.asList(new Integer[] { 1, 2 }).contains(type)) {
            return "Self-service recharge, mode of payment: " + (type == 1 ? "alipay" : "wechat") + ", amount: " + amount / 100;
        } else if (type == 3) {
            return "Business hall recharge, mode of payment: cash, amount: " + amount / 100;
        } else {
            return "";
        }
    }

    public static String buildOrderId(int userId) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String orderId = df.format(new Date()) + userId;
        return "0000000".substring(orderId.length() - 17) + orderId;
    }
}
