package cn.net.bhe.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class JacksonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void main(String[] args) throws JsonProcessingException {

    }

    public static String objToJsonStr(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <X> X jsonStrToObj(String content, Class<X> valueType) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(content, valueType);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static JsonNode readTree(String jsonStr) throws IOException {
        return objectMapper.readTree(jsonStr);
    }

    public static String pretty(String str) {
        try {
            return objToJsonStr(readTree(str));
        } catch (Exception e) {
            return str;
        }
    }
}
