package mechat.cn.net.bhe.server.service;
/*
 * Copyright (c) 2010-2019 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import mechat.cn.net.bhe.server.protocol.Dict;
import mechat.cn.net.bhe.server.protocol.MessageObj;
import mechat.cn.net.bhe.server.utils.HelperUtils;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class Server extends WebSocketServer {

    private static Map<String, WebSocket> availableClients = new HashMap<>();
    private static Map<String, WebSocket> allClients = new HashMap<>();
    private static Map<String, String> lock = new HashMap<>();

    static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public Server(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public Server(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String key = HelperUtils.keyGen(conn.getRemoteSocketAddress());

        allClients.put(key, conn);
        availableClients.put(key, conn);

        printMap(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String keySource = HelperUtils.keyGen(conn.getRemoteSocketAddress());
        String keyTarget = lock.get(keySource);

        lock.remove(keySource);
        lock.remove(keyTarget);

        allClients.remove(keySource);

        availableClients.remove(keySource);

        WebSocket target = allClients.get(keyTarget);
        if (target != null) {
            availableClients.put(keyTarget, target);
        }

        printMap(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        printMap(Thread.currentThread().getStackTrace()[1].getMethodName());

        LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + message + "\n");

        MessageObj messageObj = MessageObj.parse(message);
        LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + ReflectionToStringBuilder.toString(messageObj, ToStringStyle.MULTI_LINE_STYLE) + "\n");

        String method = messageObj.getMethod_();

        // clientA => [server => clientA, clientB]
        if (method.equals(Dict.GET)) {
            WebSocket source = getRandomClient(conn);
            if (source != null) {
                InetSocketAddress targetInet = conn.getRemoteSocketAddress();
                InetSocketAddress sourceInet = source.getRemoteSocketAddress();

                // server => clientA
                messageObj.setMethod_(Dict.POST);
                messageObj.setType_(Dict.TEXT);
                messageObj.setSAddress_(sourceInet.getHostString());
                messageObj.setSPort_(sourceInet.getPort() + "");
                messageObj.setTAddress_(targetInet.getHostString());
                messageObj.setTPort_(targetInet.getPort() + "");
                messageObj.setContent_("");

                LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + ReflectionToStringBuilder.toString(messageObj, ToStringStyle.MULTI_LINE_STYLE));
                message = MessageObj.wrap(messageObj);

                conn.send(message);

                // server => clientB
                messageObj.setSAddress_(targetInet.getHostString());
                messageObj.setSPort_(targetInet.getPort() + "");
                messageObj.setTAddress_(sourceInet.getHostString());
                messageObj.setTPort_(sourceInet.getPort() + "");

                LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + ReflectionToStringBuilder.toString(messageObj, ToStringStyle.MULTI_LINE_STYLE));
                message = MessageObj.wrap(messageObj);

                source.send(message);
            }
        }

        // clientA => [server => clientB]
        else if (method.equals(Dict.POST)) {
            String tAddress = messageObj.getTAddress_();
            String tPort = messageObj.getTPort_();

            String keySource = HelperUtils.keyGen(conn.getRemoteSocketAddress());
            String keyTarget = HelperUtils.keyGen(tAddress, tPort);

            WebSocket target = allClients.get(keyTarget);
            if (target != null && keyTarget.equals(lock.get(keySource))) {
                // server => clientB
                message = MessageObj.wrap(messageObj);
                target.send(message);
            }
        }

        // clientA => [server => clientA]
        else if (method.equals(Dict.LEAVE)) {
            String tAddress = messageObj.getTAddress_();
            String tPort = messageObj.getTPort_();

            String keySource = HelperUtils.keyGen(conn.getRemoteSocketAddress());
            String keyTarget = HelperUtils.keyGen(tAddress, tPort);

            WebSocket target = allClients.get(keyTarget);
            if (target != null) {
                lock.remove(keySource);
                lock.remove(keyTarget);

                availableClients.put(keySource, conn);
                availableClients.put(keyTarget, target);

                messageObj.setMethod_(Dict.LEAVE);
                messageObj.setType_(Dict.TEXT);
                messageObj.setSAddress_("");
                messageObj.setSPort_("");
                messageObj.setTAddress_("");
                messageObj.setTPort_("");
                messageObj.setContent_("");

                message = MessageObj.wrap(messageObj);

                conn.send(message);
                target.send(message);
            }
        }

        printMap(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    private synchronized WebSocket getRandomClient(WebSocket source) {
        String keySource = HelperUtils.keyGen(source.getRemoteSocketAddress());
        if (lock.get(keySource) != null) {
            return null;
        }

        Set<String> keySet = availableClients.keySet();
        List<String> keys = new ArrayList<>();
        keys.addAll(keySet);
        int size = keys.size();

        if (size < 1) {
            return null;
        }

        WebSocket target = null;
        int index;
        while (true) {
            index = ThreadLocalRandom.current().nextInt(size);
            target = availableClients.get(keys.get(index));
            if (source != target) {
                break;
            }
        }

        String keyTarget = HelperUtils.keyGen(target.getRemoteSocketAddress());

        lock.put(keySource, keyTarget);
        lock.put(keyTarget, keySource);

        availableClients.remove(keyTarget);
        availableClients.remove(keySource);

        return target;
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        broadcast(message.array());
        System.out.println(conn + ": " + message);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 8887; // 843 flash policy port
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
        }
        Server s = new Server(port);
        s.start();
        System.out.println("ChatServer started on port: " + s.getPort());

        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
            s.broadcast(in);
            if (in.equals("exit")) {
                s.stop(1000);
                break;
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    private void printMap(String methodName) {
        LOGGER.info(methodName
                //
                + "\n" + "allClients: " + "\n" + Joiner.on('\n').withKeyValueSeparator(" = ").join(allClients)
                //
                + "\n" + "availableClients: " + "\n" + Joiner.on('\n').withKeyValueSeparator(" = ").join(availableClients)
                //
                + "\n" + "lock: " + "\n" + Joiner.on('\n').withKeyValueSeparator(" = ").join(lock) + "\n");
    }

}
