package charserver.cn.net.bhe.common.api.greenyep;

import org.apache.commons.lang3.StringUtils;

import cn.net.bhe.utils.main.MD5Utils;

public class Config {

    /*greenyep url*/
    private final String url = "https://www.greenyep.com/api/index";

    /*token*/
    // required
    private static final String token = "1nlxhtzmy1wfdam05nrjif1urmh6jdvb";

    /*merchant id, the unique identifier of the merchant, which can be viewed in the personal center*/
    // required
    private final String uid = "6807486433564987";

    /*payment mode, 1: standard mode; 2: custom mode*/
    // required
    private Integer mode;

    /*price, unit: RMB, accurate to two decimal*/
    // required
    private Float amount;

    /*payment method, 1: alipay; 2-wechat pay*/
    // required
    private Integer type;

    /*account name*/
    // optional
    private String account;

    /*notification callback URL, after the user successfully pays, GreenYep will send a post message to this url*/
    // required
    private final String notifyurl = "******";

    /*jump URL, the target url after user successfully paid*/
    // required when mode=1, optional when mode=2
    private String returnurl = "";

    /*custom order number*/
    // required
    private String ordernum;

    /*custom user number*/
    // optional
    private String orderuid;

    /*goods name*/
    // optional
    private String goodname;

    /*MD5 string of goodname + type + account + notifyurl + returnurl + ordernum + orderuid + amount + mode + token + uid*/
    // required
    @SuppressWarnings("unused")
    private String key;

    /**
     * Custom mode, the interface will return the corresponding QR code
     * information, and the merchant can customize the payment page, and in
     * conjunction with the collection result query interface, query whether the
     * user pays successfully, mode=2.
     *
     * @param type
     *            <ul>
     *            <li>1: alipay
     *            <li>2: wechat
     *            </ul>
     * @param amount
     *            price, unit: RMB, accurate to two decimal
     * @param ordernum
     *            custom order number
     */
    public Config(Integer type, Float amount, String ordernum) {
        this.mode = 2;
        this.type = type;
        this.amount = amount;
        this.ordernum = ordernum;
    }

    /**
     * Standard mode, jump to the official payment page, mode=1.
     *
     * @param type
     *            payment method
     *            <ul>
     *            <li>1: alipay;
     *            <li>2: wechat pay
     *            </ul>
     * @param amount
     *            price, unit: RMB, accurate to two decimal
     * @param returnurl
     *            jump URL, the target url after user successfully paid
     * @param ordernum
     *            custom order number
     */
    public Config(Integer type, Float amount, String returnurl, String ordernum) {
        this.mode = 1;
        this.type = type;
        this.amount = amount;
        this.returnurl = returnurl;
        this.ordernum = ordernum;
    }

    public String getKey() {
        /*goodname + type + account + notifyurl + returnurl + ordernum + orderuid + amount + mode + token + uid*/
        String key = "";
        try {
            if (StringUtils.isNotEmpty(goodname)) {
                key += goodname;
            }
            if (type != null) {
                key += type;
            }
            if (StringUtils.isNotEmpty(account)) {
                key += account;
            }
            if (StringUtils.isNotEmpty(notifyurl)) {
                key += notifyurl;
            }
            if (StringUtils.isNotEmpty(returnurl)) {
                key += returnurl;
            }
            if (StringUtils.isNotEmpty(ordernum)) {
                key += ordernum;
            }
            if (StringUtils.isNotEmpty(orderuid)) {
                key += orderuid;
            }
            if (amount != null) {
                key += amount;
            }
            if (mode != null) {
                key += mode;
            }
            if (StringUtils.isNotEmpty(token)) {
                key += token;
            }
            if (StringUtils.isNotEmpty(uid)) {
                key += uid;
            }
            key = MD5Utils.toLowerStr(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public String getUrl() {
        return url;
    }

    public static String getToken() {
        return token;
    }

    public String getUid() {
        return uid;
    }

    public Integer getMode() {
        return mode;
    }

    public Float getAmount() {
        return amount;
    }

    public Integer getType() {
        return type;
    }

    public String getAccount() {
        return account;
    }

    public String getNotifyurl() {
        return notifyurl;
    }

    public String getReturnurl() {
        return returnurl;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public String getOrderuid() {
        return orderuid;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setReturnurl(String returnurl) {
        this.returnurl = returnurl;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public void setOrderuid(String orderuid) {
        this.orderuid = orderuid;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
