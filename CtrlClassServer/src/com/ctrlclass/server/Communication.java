package com.ctrlclass.server;

import java.io.IOException;
import java.net.*;

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

            System.out.println("Trying to receive packet...");
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.err.println("Failed to receive packet");
                continue;
            }

            String receivedString = new String(packet.getData());
            returnIPAddress = packet.getAddress();
            returnPort = packet.getPort();

            System.out.println("Received: [" + receivedString + "] from: " + returnIPAddress + ":" + returnPort);

            String reply = authManager.isAuthorized(receivedString).toString();
            System.out.println("Reply: [" + reply + "]");

            System.out.println("Trying to send reply packet...");
            buf = reply.getBytes();
            packet = new DatagramPacket(buf, buf.length, returnIPAddress, returnPort);
            try {
                socket.send(packet);
            } catch (IOException e) {
                System.err.println("Failed to send reply packet");
                continue;
            }

        }
        socket.close();
        System.out.println("Communication finished.");
    }
}
