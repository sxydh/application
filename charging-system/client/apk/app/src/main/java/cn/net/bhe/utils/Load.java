package cn.net.bhe.utils;

import java.util.HashMap;
import java.util.Map;

public class Load {

    private final String path;
    private final Map<String, Object> map = new HashMap<>();

    private Load(String path) {
        this.path = path;
    }

    public static Load instance(String url) {
        return new Load(url);
    }

    public String getPath() {
        return path;
    }

    public Object get(String key){
        return map.get(key);
    }

    public Load put(String key, Object value){
        map.put(key, value);
        return this;
    }

    public Map<String ,Object> getMap(){
        return this.map;
    }
}
