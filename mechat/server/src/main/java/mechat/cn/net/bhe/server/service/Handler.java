package mechat.cn.net.bhe.server.service;

import org.java_websocket.WebSocket;

import mechat.cn.net.bhe.server.protocol.MessageObj;

public interface Handler {

    public void handle(WebSocket conn, MessageObj messageObj);

}
