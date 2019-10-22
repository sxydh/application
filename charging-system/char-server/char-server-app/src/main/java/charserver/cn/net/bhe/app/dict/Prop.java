package charserver.cn.net.bhe.app.dict;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import charserver.cn.net.bhe.app.service.UserService;
import charserver.cn.net.bhe.common.dict.Const;
import charserver.cn.net.bhe.common.exception.BusinessException;

/**
 * Note, public field cannot use primitive type.
 */
@Component
public class Prop {

    static Logger LOGGER = LoggerFactory.getLogger(Prop.class);

    private static final String[] props = { "res.properties" };

    private static Map<String, Boolean> checkUrl = loadPropFile(Boolean.class, "url_check.properties");
    private static Map<String, Boolean> encryptUrl = loadPropFile(Boolean.class, "url_encrypt.properties");

    public static Boolean ENCRYPT_API_ENABLE;

    public static Boolean INTERCEPTOR_AUTH_CHECK;

    public static final long ORDER_UNPAID_TIMER = 30 * 60 * 1000;

    public static String REDIS_HOST;
    public static Integer REDIS_PORT;
    public static String REDIS_PASSWORD;
    public static Integer REDIS_MAX_TOTAL = 200;
    public static Integer REDIS_MAX_IDLE = 50;
    public static Integer REDIS_MAX_WAIT = 1500;
    public static Boolean REDIS_TEST_ON_BORROW = true;

    public static Boolean TIMER_ENABLED;

    private static Throwable boote = null;

    static {
        /** public field init */
        initPubField();
    }

    private static Map<String, Object> beans = new HashMap<>();

    private static void initPubField() {
        try {
            Map<String, String> map = new HashMap<>();
            for (String path : props) {
                Map<String, String> temp = loadPropFile(String.class, path);
                map.putAll(temp);
            }

            Map<String, String> res = new HashMap<>();
            for (String key : map.keySet()) {
                res.put(key.replace(".", "_").toUpperCase(), map.get(key));
            }

            for (Field field : listStatPubField()) {
                String fName = field.getName();

                if (res.get(fName) != null) {
                    field.set(null, ConvertUtils.convert(res.get(fName), field.getType()));
                }

                if (field.get(null) == null) {
                    throw new BusinessException(Prop.class.getName() + "." + fName + " can not be null, neither initialized its value nor defined in " + Arrays.toString(props));
                }
            }
        } catch (Exception e) {
            LOGGER.error(Const.LOGGER_TAG_ERR_BUSINESS, e);
            boote = e;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <X> Map<String, X> loadPropFile(Class<X> clazz, String fileName) {
        Map<String, X> result = new HashMap<>();

        Map<String, String> init = new HashMap();

        // inner
        try {
            Properties prop = new Properties();
            prop.load(Prop.class.getClassLoader().getResourceAsStream(fileName));
            init.putAll(new HashMap(prop));
        } catch (Exception e) {
            LOGGER.warn(e.getLocalizedMessage());
        }

        for (String key : init.keySet()) {
            result.put(key, (X) ConvertUtils.convert(init.get(key), clazz));
        }
        return result;
    }

    public Prop(ApplicationContext context) {
        if (boote != null) {
            LOGGER.error(Const.LOGGER_TAG_ERR_BUSINESS, boote);
            System.exit(0);
        }
    }
    
    @Autowired
    private UserService userService;

    @PostConstruct
    private void initUser() {
        userService.init();
    }

    /** Use with caution. */
    @SuppressWarnings("unchecked")
    public static <X> X getBean(Class<X> clazz) {
        return (X) beans.get(clazz.getSimpleName());
    }

    public static boolean needCheck(String url) {
        Boolean b = checkUrl.get(url.replaceAll("\\/+", "/"));
        if (b == null) {
            return true;
        } else {
            return b;
        }
    }

    public static boolean needEncrypt(String url) {
        Boolean b = encryptUrl.get(url.replaceAll("\\/+", "/"));
        if (b == null) {
            return true;
        } else {
            return b;
        }
    }

    private static List<Field> listStatPubField() {
        Field[] array = Prop.class.getFields();
        List<Field> fields = new ArrayList<>();
        for (Field field : array) {
            if (Modifier.isStatic(field.getModifiers())) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
        for (Field f : listStatPubField()) {
            String n = f.getName();
            Object v = f.get(null);
            System.out.println(n + ", " + v);
        }
    }

}
