package mechat.cn.net.bhe.server.utils;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public enum HelperUtils {

    ;
    public static String keyGen(InetSocketAddress inetSocketAddress) {
        String address = inetSocketAddress.getHostString();
        int port = inetSocketAddress.getPort();

        String str = address + port;
        try {
            return MD5Utils.toLowerStr(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String keyGen(String address, int port) {

        String str = address + port;
        try {
            return MD5Utils.toLowerStr(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> parse(String message) {
        Map<String, Object> result = new HashMap<>();

        int methodBeginIndex = message.indexOf("Method: ");
        int typeBeginIndex = message.indexOf("Type: ");
        int addressBeginIndex = message.indexOf("Address: ");
        int portBeginIndex = message.indexOf("Port: ");
        int contentBeginIndex = message.indexOf("Content: ");

        result.put("Method", message.substring(methodBeginIndex, typeBeginIndex));
        result.put("Type", message.substring(typeBeginIndex, addressBeginIndex));
        result.put("Address", message.substring(addressBeginIndex, portBeginIndex));
        result.put("Port", Integer.parseInt(message.substring(portBeginIndex, contentBeginIndex)));
        result.put("Content", message.substring(contentBeginIndex));

        return result;
    }

}
