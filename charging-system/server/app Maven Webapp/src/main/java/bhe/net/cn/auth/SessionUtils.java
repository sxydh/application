package bhe.net.cn.auth;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bhe.net.cn.dict.Constant;
import bhe.net.cn.dict.RedisKey;
import bhe.net.cn.filter.PreprocessFilter;
import bhe.net.cn.utils.MD5Utils;
import bhe.net.cn.utils.MathUtils;

public enum SessionUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(SessionUtils.class);

    @SuppressWarnings("unchecked")
    public static <X> X get(HttpServletRequest request, SessionKey key) {
        return (X) request.getSession().getAttribute(key.toString());
    }

    public static void set(HttpServletRequest request, SessionKey key, Object value) {
        request.getSession().setAttribute(key.toString(), value);
    }

    public static String getIp(HttpServletRequest request) {
        return PreprocessFilter.getClientIpAddr(request);
    }

    public static void invalidate(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static void dySms(String phone) throws Exception {
        String dycode = MathUtils.getRandomNum(6);
        LOGGER.info(dycode);

        // AlicomDysms.sendSms(phone, dycode);

        String key = MD5Utils.toLowerStr(RedisKey.CS_APP_DYCODE.toString());
        String field = MD5Utils.toLowerStr(phone);
        String value = dycode + "," + System.currentTimeMillis();
        RedisUtils.hset(key, field, value, null);
    }

    public static int validCode(String phone, String code) throws Exception {
        String key = MD5Utils.toLowerStr(RedisKey.CS_APP_DYCODE.toString());
        String field = MD5Utils.toLowerStr(phone);

        String value = RedisUtils.hget(key, field);
        if (value == null) {
            return -1;
        }

        String[] data = value.split(",");
        String cachedCode = data[0];
        long startTime = Long.valueOf(data[1]);

        if (!code.equals(cachedCode)) {
            return 0;
        }

        long interval = (System.currentTimeMillis() - startTime) / 1000;
        if (interval > Constant.DYCODE_KEEP_TIME) {
            return 2;
        }

        return 1;
    }

}
