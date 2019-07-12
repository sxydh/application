package mechat.cn.net.bhe.server.utils;

import java.net.InetSocketAddress;

public enum HelperUtils {

    ;
    public static String keyGen(InetSocketAddress inetSocketAddress) {
        String address = inetSocketAddress.getHostString();
        String port = inetSocketAddress.getPort() + "";

        String str = address + port;
        try {
            return MD5Utils.toLowerStr(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String keyGen(String address, String port) {

        String str = address + port;
        try {
            return MD5Utils.toLowerStr(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
