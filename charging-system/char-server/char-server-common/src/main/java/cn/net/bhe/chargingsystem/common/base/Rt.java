package cn.net.bhe.chargingsystem.common.base;

public class Rt {

    public static final Integer SC_OK = 200;
    public static final Integer SC_ERR_BS = 300;
    public static final Integer SC_ERR_SYS = 400;
    public static final Integer SC_ERR_EXP = 401;

    private Integer sc;
    private String msg;
    private Object data;

    public static Rt ins() {
        return new Rt();
    }

    public static Rt suc() {
        return new Rt(SC_OK);
    }

    private Rt() {

    }

    public Rt(Integer sc) {
        this.sc = sc;
    }

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
