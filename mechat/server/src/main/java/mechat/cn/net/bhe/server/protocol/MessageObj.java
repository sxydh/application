package mechat.cn.net.bhe.server.protocol;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

public class MessageObj {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String ACK = "ACK";

    public MessageObj() {
    }

    private String Method_;
    private String Type_;
    private String SAddress_;
    private int SPort_;
    private String TAddress_;
    private int Tport_;
    private String Content_;

    public static MessageObj parse(String message) {
        MessageObj messageObj = new MessageObj();

        Field[] fields = messageObj.getClass().getDeclaredFields();
        int length = fields.length;

        String preFname = "";
        String curFname = "";
        Method preMethod = null;

        for (int i = 0; i <= length; i++) {
            try {
                if (i < length) {
                    curFname = fields[i].getName();
                    if (StringUtils.isNotEmpty(preFname) && !preFname.equals(curFname) && preMethod != null) {
                        int begin = message.indexOf(preFname);
                        int end = message.indexOf(curFname);
                        String value = message.substring(begin, end);
                        preMethod.invoke(messageObj, value);
                    }

                    preFname = curFname;
                    preMethod = messageObj.getClass().getMethod("set" + curFname.substring(0, 1).toUpperCase() + curFname.substring(1));
                } else {
                    int begin = message.indexOf(preFname);
                    String value = message.substring(begin);
                    preMethod.invoke(messageObj, value);
                }
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        }

        return messageObj;
    }

    public static String wrap(MessageObj messageObj) {
        String result = "";

        Field[] fields = messageObj.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                String fName = field.getName();
                Method method;
                method = messageObj.getClass().getMethod("get" + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                Object value = method.invoke(messageObj);
                result += fName + value;
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public String getMethod_() {
        return Method_;
    }

    public String getType_() {
        return Type_;
    }

    public String getSAddress_() {
        return SAddress_;
    }

    public int getSPort_() {
        return SPort_;
    }

    public String getTAddress_() {
        return TAddress_;
    }

    public int getTport_() {
        return Tport_;
    }

    public String getContent_() {
        return Content_;
    }

    public void setMethod_(String method_) {
        Method_ = method_;
    }

    public void setType_(String type_) {
        Type_ = type_;
    }

    public void setSAddress_(String sAddress_) {
        SAddress_ = sAddress_;
    }

    public void setSPort_(int sPort_) {
        SPort_ = sPort_;
    }

    public void setTAddress_(String tAddress_) {
        TAddress_ = tAddress_;
    }

    public void setTport_(int tport_) {
        Tport_ = tport_;
    }

    public void setContent_(String content_) {
        Content_ = content_;
    }

}
