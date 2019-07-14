package mechat.cn.net.bhe.server.service.method;

import java.util.Map;

import org.java_websocket.WebSocket;

import mechat.cn.net.bhe.server.protocol.MessageObj;
import mechat.cn.net.bhe.server.service.Handler;
import mechat.cn.net.bhe.server.service.Server;
import mechat.cn.net.bhe.server.utils.HelperUtils;

public class LEAVE implements Handler {

    @Override
    // clientA => [server => clientA, clientB]
    public void handle(WebSocket conn, MessageObj messageObj) {
        String aAddress = messageObj.getSAddress_();
        String aPort = messageObj.getSPort_();
        String bAddress = messageObj.getTAddress_();
        String bPort = messageObj.getTPort_();

        String keyA = HelperUtils.keyGen(conn.getRemoteSocketAddress());
        String keyB = HelperUtils.keyGen(bAddress, bPort);

        WebSocket clientB = Server.getAllClients().get(keyB);
        if (clientB != null) {
            Map<String, String> lock = Server.getLock();
            lock.remove(keyA);
            lock.remove(keyB);

            Map<String, WebSocket> availableClients = Server.getAvailableClients();
            availableClients.put(keyA, conn);
            availableClients.put(keyB, clientB);

            // server => clientA
            messageObj.setSAddress_(bAddress);
            messageObj.setSPort_(bPort);
            messageObj.setTAddress_(aAddress);
            messageObj.setTPort_(aPort);
            messageObj.setContent_("CONNECTEND");

            String message = MessageObj.wrap(messageObj);

            conn.send(message);

            // server => clientB
            messageObj.setSAddress_(aAddress);
            messageObj.setSPort_(aPort);
            messageObj.setTAddress_(bAddress);
            messageObj.setTPort_(bPort);

            message = MessageObj.wrap(messageObj);

            clientB.send(message);
        }
    }

}
