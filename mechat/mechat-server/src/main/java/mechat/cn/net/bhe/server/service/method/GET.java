package mechat.cn.net.bhe.server.service.method;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mechat.cn.net.bhe.server.protocol.MessageObj;
import mechat.cn.net.bhe.server.service.Handler;
import mechat.cn.net.bhe.server.service.Server;
import mechat.cn.net.bhe.server.utils.HelperUtils;

public class GET extends Handler {

    static final Logger LOGGER = LoggerFactory.getLogger(GET.class);

    @Override
    // clientA => [server => clientA, clientB]
    public synchronized void handle(WebSocket conn, MessageObj messageObj) {
        WebSocket clientB = null;

        String keyA = HelperUtils.keyGen(conn.getRemoteSocketAddress());
        String keyB = messageObj.getContent_();

        Map<String, String> lock = Server.getLock();

        if (StringUtils.isNotEmpty(keyB)) {
            clientB = Server.getAvailableClients().get(keyB);
        } else {
            if (lock.get(keyA) == null) {
                clientB = Server.getRandomClient(conn);
            }
        }

        if (clientB != null) {
            keyB = HelperUtils.keyGen(clientB.getRemoteSocketAddress());

            lock.put(keyA, keyB);
            lock.put(keyB, keyA);

            Map<String, WebSocket> availableClients = Server.getAvailableClients();
            availableClients.remove(keyB);
            availableClients.remove(keyA);

            InetSocketAddress aInet = conn.getRemoteSocketAddress();
            InetSocketAddress bInet = clientB.getRemoteSocketAddress();

            // server => clientA
            messageObj.setSAddress_("");
            messageObj.setSPort_("");
            messageObj.setTAddress_(aInet.getHostString());
            messageObj.setTPort_(aInet.getPort() + "");
            messageObj.setContent_("");

            String message = MessageObj.wrap(messageObj);

            send(conn, message);

            // server => clientB
            messageObj.setSAddress_(aInet.getHostString());
            messageObj.setSPort_(aInet.getPort() + "");
            messageObj.setTAddress_(bInet.getHostString());
            messageObj.setTPort_(bInet.getPort() + "");

            message = MessageObj.wrap(messageObj);

            send(clientB, message);
        }
    }
}
