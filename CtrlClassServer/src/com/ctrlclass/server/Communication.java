package com.ctrlclass.server;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeoutException;

public class Communication extends Thread {
    private DatagramSocket socket;
    private byte[] buf;
    private boolean running;
    private AuthManager authManager;

    public Communication(AuthManager authManager) {

        this.authManager = authManager;

        buf = new byte[1024];
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        InetAddress returnIPAddress;
        int returnPort;
        DatagramPacket packet;

        running = true;

        System.out.println("Communication started");
        while (running) {

            packet = new DatagramPacket(buf, buf.length);

            System.out.println(1);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                //continue;
            }
            String receivedString = new String(packet.getData());
            returnIPAddress = packet.getAddress();
            returnPort = packet.getPort();

            System.out.println(returnIPAddress + ":" + returnPort + " says: " + receivedString);

            // TODO Convert receivedString to uid
            String uid = receivedString;

            String reply;
            if(authManager.isAuthorized(uid)) {
                reply = "UID: ["+uid+"] authorized.";
            } else {
                reply = "UID: ["+uid+"] unauthorized.";
            }

            buf = reply.getBytes();
            packet = new DatagramPacket(buf, buf.length, returnIPAddress, returnPort);

            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

        }
        socket.close();
        System.out.println("Comunicação finalizada");
    }
}
