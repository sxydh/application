package cn.net.bhe.chargingsystem.app.cache;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.net.bhe.chargingsystem.app.filter.AttrFilter;
import cn.net.bhe.chargingsystem.common.dict.Const;
import cn.net.bhe.chargingsystem.common.dict.K;
import cn.net.bhe.utils.main.MD5Utils;
import cn.net.bhe.utils.main.MathUtils;

public enum SessionUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(SessionUtils.class);

    @SuppressWarnings("unchecked")
    public static <X> X get(HttpServletRequest request, K key) {
        return (X) request.getSession().getAttribute(key.toString());
    }

    public static void set(HttpServletRequest request, K key, Object value) {
        request.getSession().setAttribute(key.toString(), value);
    }

    public static String getIp(HttpServletRequest request) {
        return AttrFilter.getClientIpAddr(request);
    }

    public static void invalidate(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static void dySms(String phone) throws Exception {
        String dycode = MathUtils.getRandomNum(6);
        LOGGER.info(dycode);

        // AlicomDysms.sendSms(phone, dycode);

        String key = MD5Utils.toLowerStr(K.DYCODE_APP.toString());
        String field = MD5Utils.toLowerStr(phone);
        String value = dycode + "," + System.currentTimeMillis();
        RedisUtils.hset(key, field, value, null);
    }

    public static int validCode(String phone, String code) throws Exception {
        String key = MD5Utils.toLowerStr(K.DYCODE_APP.toString());
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
        if (interval > Const.DYCODE_KEEP_TIME) {
            return 2;
        }

        return 1;
    }

}
