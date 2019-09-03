package cn.net.bhe.utils;

import java.util.HashMap;
import java.util.Map;

public class M {

    private final Map<K, Object> map = new HashMap<>();

    public static M g() {
        return new M();
    }

    public M put(K key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public Map<K, Object> getMap() {
        return this.map;
    }

}
