package mechat.cn.net.bhe.server.service;

import org.java_websocket.WebSocket;
import org.java_websocket.enums.ReadyState;

import mechat.cn.net.bhe.server.protocol.MessageObj;

public abstract class Handler {

    public abstract void handle(WebSocket conn, MessageObj messageObj);

    public void send(WebSocket webSocket, String message) {
        if (webSocket.getReadyState() == ReadyState.OPEN) {
            webSocket.send(message);
        }
    }

}
