package cn.net.bhe.chargingsystem.common.api.greenyep;

import java.io.Serializable;

public class GYPResponse implements Serializable {

    private static final long serialVersionUID = 8582194697511891230L;
    private String payurl;
    private String payurl_online;
    private String action;
    private Double amount;
    private Double realamount;
    private String greenpay_id;
    private String ordernum;
    private String payimg;
    private String queryurl;
    private Integer type;
    private String key;
    private Integer timeout;

    public GYPResponse() {

    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getPayurl() {
        return payurl;
    }

    public String getPayurl_online() {
        return payurl_online;
    }

    public String getAction() {
        return action;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getRealamount() {
        return realamount;
    }

    public String getGreenpay_id() {
        return greenpay_id;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public String getPayimg() {
        return payimg;
    }

    public String getQueryurl() {
        return queryurl;
    }

    public Integer getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setPayurl(String payurl) {
        this.payurl = payurl;
    }

    public void setPayurl_online(String payurl_online) {
        this.payurl_online = payurl_online;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setRealamount(Double realamount) {
        this.realamount = realamount;
    }

    public void setGreenpay_id(String greenpay_id) {
        this.greenpay_id = greenpay_id;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public void setPayimg(String payimg) {
        this.payimg = payimg;
    }

    public void setQueryurl(String queryurl) {
        this.queryurl = queryurl;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

}
