package bhe.net.cn.dict;

public class Note {

    /*the server has successfully processed the request*/
    public static final Integer SC_OK = 200;
    public static final String MSG_OK = "the server has successfully processed the request";
    /*wrong request*/
    public static final Integer SC_BADREQUEST = 400;
    public static final String MSG_BADREQUEST = "wrong request";
    /*request for authentication*/
    public static final Integer SC_UNAUTHORIZED = 401;
    public static final String MSG_UNAUTHORIZED = "request not authorized";
    /*server internal error*/
    public static final Integer SC_INNERERROR = 500;
    public static final String MSG_INNERERROR = "server internal error";

    /*session*/
    public static final String NEED_RELOGIN = "need relogin";
}
