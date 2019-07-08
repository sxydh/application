package bhe.net.cn.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum StreamUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(StreamUtils.class);

    public static String isToStr(InputStream is, Charset charset) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
        String line = "";
        while ((line = reader.readLine()) != null) {
            strBuilder.append(line);
        }
        return strBuilder.toString();
    }
}
