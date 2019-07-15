package mechat.cn.net.bhe.server.service.method;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mechat.cn.net.bhe.server.protocol.Dict;
import mechat.cn.net.bhe.server.protocol.MessageObj;
import mechat.cn.net.bhe.server.service.Handler;
import mechat.cn.net.bhe.server.service.Server;
import mechat.cn.net.bhe.server.utils.HelperUtils;

/**
 * Passive.
 */
public class CONN implements Handler {

    static final Logger LOGGER = LoggerFactory.getLogger(CONN.class);

    @Override
    // clientA => [server => clientA, clientB]
    public void handle(WebSocket conn, MessageObj messageObj) {
        String content = messageObj.getContent_();
        if (!Dict.Y.equalsIgnoreCase(content) && !Dict.N.equalsIgnoreCase(content)) {
            return;
        }

        String aAddress = messageObj.getSAddress_();
        String aPort = messageObj.getSPort_();
        String bAddress = messageObj.getTAddress_();
        String bPort = messageObj.getTPort_();

        String keyA = HelperUtils.keyGen(aAddress, aPort);
        String keyB = HelperUtils.keyGen(bAddress, bPort);

        Map<String, WebSocket> allClients = Server.getAllClients();
        WebSocket clientA = allClients.get(keyA);
        WebSocket clientB = allClients.get(keyB);

        // server => clientA
        messageObj.setSAddress_(bAddress);
        messageObj.setSPort_(bPort);
        messageObj.setTAddress_(aAddress);
        messageObj.setTPort_(aPort);

        LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + ReflectionToStringBuilder.toString(messageObj, ToStringStyle.MULTI_LINE_STYLE));

        String message = MessageObj.wrap(messageObj);

        clientA.send(message);

        // server => clientB
        messageObj.setSAddress_(aAddress);
        messageObj.setSPort_(aPort);
        messageObj.setTAddress_(bAddress);
        messageObj.setTPort_(bPort);

        LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + ReflectionToStringBuilder.toString(messageObj, ToStringStyle.MULTI_LINE_STYLE));

        message = MessageObj.wrap(messageObj);

        clientB.send(message);
    }

}
