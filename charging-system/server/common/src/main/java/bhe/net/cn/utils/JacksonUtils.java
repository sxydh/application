package bhe.net.cn.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum JacksonUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static void main(String[] args) throws JsonProcessingException {
        LOGGER.info(JacksonUtils.objToJsonStr(new Bean()));
    }

    public static String objToJsonStr(Object obj) {
        String str = "";
        try {
            str = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            str = e.getLocalizedMessage();
        }
        LOGGER.info(str);
        return str;
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
}

class Bean {

    private Integer id;
    private String data;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}