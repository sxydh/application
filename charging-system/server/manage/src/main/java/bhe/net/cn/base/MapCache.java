package bhe.net.cn.base;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapCache {

    static final Logger LOGGER = LoggerFactory.getLogger(MapCache.class);
    public static Map<String, String> verify_url;

    static {
        try {
            InputStream is = MapCache.class.getClassLoader().getResourceAsStream("verify_url.properties");
            Properties verify_url_p = new Properties();
            verify_url_p.load(is);
            verify_url = new HashMap(verify_url_p);
        } catch (Exception e) {
            LOGGER.info(e.getLocalizedMessage());
        }
    }
}
