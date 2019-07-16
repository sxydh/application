package mechat.cn.net.bhe.server.service.method;

import java.net.InetSocketAddress;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mechat.cn.net.bhe.server.protocol.MessageObj;
import mechat.cn.net.bhe.server.service.Handler;
import mechat.cn.net.bhe.server.service.Server;
import mechat.cn.net.bhe.server.utils.HelperUtils;

public class LIST implements Handler {

    static final Logger LOGGER = LoggerFactory.getLogger(LIST.class);

    @Override
    // clientA => server => clientA
    public void handle(WebSocket conn, MessageObj messageObj) {
        InetSocketAddress aInet = conn.getRemoteSocketAddress();

        // server => clientA
        messageObj.setSAddress_("");
        messageObj.setSPort_("");
        messageObj.setTAddress_(aInet.getHostString());
        messageObj.setTPort_(aInet.getPort() + "");

        String content = "";
        Map<String, WebSocket> availableClients = Server.getAvailableClients();
        String keySelf = HelperUtils.keyGen(aInet);
        for (String key : availableClients.keySet()) {
            if (key.equals(keySelf)) {
                continue;
            }
            content += "," + key;
        }
        if (content.split(",").length > 0) {
            messageObj.setContent_(content.substring(1));
        }

        String message = MessageObj.wrap(messageObj);

        conn.send(message);
    }

}
