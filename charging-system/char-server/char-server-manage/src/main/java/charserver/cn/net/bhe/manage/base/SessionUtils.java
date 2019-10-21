package charserver.cn.net.bhe.manage.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import charserver.cn.net.bhe.common.dict.Const;
import charserver.cn.net.bhe.common.dict.K;
import charserver.cn.net.bhe.manage.filter.AttrFilter;
import cn.net.bhe.utils.main.MD5Utils;
import cn.net.bhe.utils.main.MathUtils;
import cn.net.bhe.utils.main.SerializeUtils;

@Component
public class SessionUtils {

    static Logger LOGGER = LoggerFactory.getLogger(SessionUtils.class);

    @Autowired
    private RedisTemplateUtils redis;

    @Autowired
    private HttpServletRequest request;

    @Value("${dispatch.tokenHeader}")
    private String tokenHeader;

    @Value("${dispatch.session.time}")
    private long time;

    public int validCode(String phone, String code) throws Exception {
        String key = MD5Utils.toLowerStr(K.DYCODE_MG.toString());
        String field = MD5Utils.toLowerStr(phone);

        String value = (String) redis.get(key, field);
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

    public void dySms(String phone) throws Exception {
        String dycode = MathUtils.randomNum(6);
        LOGGER.info(dycode);

        // AlicomDysms.sendSms(phone, dycode);

        String key = MD5Utils.toLowerStr(K.DYCODE_MG.toString());
        String field = MD5Utils.toLowerStr(phone);
        String value = dycode + "," + System.currentTimeMillis();
        redis.put(key, field, value);
    }

    public void delCurUser() {
        try {
            String token = getToken();
            if (StringUtils.isEmpty(token)) {
                return;
            }
            redis.del(SerializeUtils.serialize(token));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Object value) {
        try {
            redis.set(SerializeUtils.serialize(key), SerializeUtils.serialize(value), time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurUser(String token, Map<String, Object> user) {
        try {
            redis.set(SerializeUtils.serialize(token), SerializeUtils.serialize(user), time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getCurUser() {
        try {
            String token = getToken();
            if (StringUtils.isNotEmpty(token)) {
                byte[] bytes = redis.get(SerializeUtils.serialize(token));
                if (bytes != null && bytes.length > 0) {
                    Map<String, Object> curUser = (Map<String, Object>) SerializeUtils.deserialize(bytes);
                    setCurUser(token, curUser);
                    return curUser;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getToken() {
        String token = request.getHeader(tokenHeader);
        LOGGER.info(token);
        return token;
    }

    public String getIp() {
        return AttrFilter.getClientIpAddr(request);
    }

}
