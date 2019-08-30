package cn.net.bhe.utils;

public class Load {

    private String url;
    private String jsonStr;

    private Load() {

    }

    public static Load instance() {
        return new Load();
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
