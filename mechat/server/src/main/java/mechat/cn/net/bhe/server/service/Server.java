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
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mechat.cn.net.bhe.server.protocol.MessageObj;
import mechat.cn.net.bhe.server.utils.HelperUtils;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class Server extends WebSocketServer {

    private static ConcurrentMap<String, WebSocket> availableClients = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, WebSocket> allClients = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, String> lock = new ConcurrentHashMap<>();
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

        System.out.println("\n" + "allClients: " + allClients + "\n" + "availableClients: " + availableClients + "\n");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String key = HelperUtils.keyGen(conn.getRemoteSocketAddress());

        allClients.remove(key);
        availableClients.remove(key);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        WebSocket target;

        MessageObj messageObj = MessageObj.parse(message);

        String method = messageObj.getMethod_();
        String sAddress = messageObj.getSAddress_();
        int sPort = messageObj.getSPort_();
        String tAddress = messageObj.getTAddress_();
        int tPort = messageObj.getTport_();
        String content = messageObj.getContent_();

        if (method.equals(MessageObj.GET)) {
            target = getRandomClient(conn);
            if (target == null) {
                return;
            }

            InetSocketAddress inetSocketAddress = target.getRemoteSocketAddress();

            messageObj.setMethod_(MessageObj.POST);
            messageObj.setSAddress_(inetSocketAddress.getHostString());
            messageObj.setSPort_(inetSocketAddress.getPort());
            messageObj.setTAddress_(sAddress);
            messageObj.setTport_(sPort);
            messageObj.setContent_("");

            message = MessageObj.wrap(messageObj);

            conn.send(message);
        }

        else if (method.equals("POST")) {
            String keySource = HelperUtils.keyGen(conn.getRemoteSocketAddress());
            String keyTarget = HelperUtils.keyGen(tAddress, tPort);

            target = allClients.get(keyTarget);
            if (target == null) {
                return;
            }

            if (!keyTarget.equals(lock.get(keySource))) {
                return;
            }

            target.send(content);
        }
    }

    private synchronized WebSocket getRandomClient(WebSocket source) {
        String keySource = HelperUtils.keyGen(source.getRemoteSocketAddress());
        if (lock.get(keySource) != null) {
            return null;
        }

        Set<String> keys = availableClients.keySet();
        int size = keys.size();

        if (size <= 2) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(size);

        WebSocket target = null;
        while (true) {
            target = availableClients.get(Arrays.asList(keys).get(index));
            if (source != target) {
                break;
            }
        }

        String keyTarget = HelperUtils.keyGen(target.getRemoteSocketAddress());

        lock.put(keySource, keyTarget);
        lock.put(keyTarget, keySource);

        availableClients.remove(keyTarget);

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

}
