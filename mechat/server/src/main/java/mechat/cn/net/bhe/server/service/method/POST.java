package mechat.cn.net.bhe.server.service.method;

import org.java_websocket.WebSocket;

import mechat.cn.net.bhe.server.protocol.MessageObj;
import mechat.cn.net.bhe.server.service.Handler;
import mechat.cn.net.bhe.server.service.Server;
import mechat.cn.net.bhe.server.utils.HelperUtils;

public class POST extends Handler {

    @Override
    // clientA => [server => clientB]
    public void handle(WebSocket conn, MessageObj messageObj) {
        String bAddress = messageObj.getTAddress_();
        String bPort = messageObj.getTPort_();

        String keyA = HelperUtils.keyGen(conn.getRemoteSocketAddress());
        String keyB = HelperUtils.keyGen(bAddress, bPort);

        WebSocket clientB = Server.getAllClients().get(keyB);
        if (clientB != null && keyB.equals(Server.getLock().get(keyA))) {
            // server => clientB
            String message = MessageObj.wrap(messageObj);
            send(clientB, message);
        }
    }

}
