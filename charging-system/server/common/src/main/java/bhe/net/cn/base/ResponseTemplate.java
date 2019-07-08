package bhe.net.cn.base;

public class ResponseTemplate {

    private Integer sc;
    private String msg;
    private Object data;

    public Integer getSc() {
        return sc;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void setSc(Integer sc) {
        this.sc = sc;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
