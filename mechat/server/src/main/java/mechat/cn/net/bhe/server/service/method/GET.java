package mechat.cn.net.bhe.server.service.method;

import java.net.InetSocketAddress;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mechat.cn.net.bhe.server.protocol.MessageObj;
import mechat.cn.net.bhe.server.service.Handler;
import mechat.cn.net.bhe.server.service.Server;

public class GET implements Handler {

    static final Logger LOGGER = LoggerFactory.getLogger(GET.class);

    @Override
    // clientA => [server => clientA, clientB]
    public void handle(WebSocket conn, MessageObj messageObj) {
        WebSocket clientB = Server.getRandomClient(conn);
        if (clientB != null) {
            InetSocketAddress aInet = conn.getRemoteSocketAddress();
            InetSocketAddress bInet = clientB.getRemoteSocketAddress();

            // server => clientA
            messageObj.setSAddress_("");
            messageObj.setSPort_("");
            messageObj.setTAddress_(aInet.getHostString());
            messageObj.setTPort_(aInet.getPort() + "");
            messageObj.setContent_("");

            LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + ReflectionToStringBuilder.toString(messageObj, ToStringStyle.MULTI_LINE_STYLE));
            String message = MessageObj.wrap(messageObj);

            conn.send(message);

            // server => clientB
            messageObj.setSAddress_(aInet.getHostString());
            messageObj.setSPort_(aInet.getPort() + "");
            messageObj.setTAddress_(bInet.getHostString());
            messageObj.setTPort_(bInet.getPort() + "");

            LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + ReflectionToStringBuilder.toString(messageObj, ToStringStyle.MULTI_LINE_STYLE));
            message = MessageObj.wrap(messageObj);

            clientB.send(message);
        }
    }
}
