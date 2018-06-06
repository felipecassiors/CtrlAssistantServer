package com.ctrlclass.server;

import java.io.IOException;
import java.net.*;

public class Communication extends Thread {
    private boolean running;
    private AuthManager authManager;

    public Communication(AuthManager authManager) {

        this.authManager = authManager;

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

        InetAddress ip = null;
        try {
            ip = InetAddress.getByName("192.168.137.2");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int port = 6000;

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] receiveData = new byte[1024];

        DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

        try {
            System.out.println("Communication started, listening on " + InetAddress.getLocalHost().getHostAddress() + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        running = true;
        while (running) {

            System.out.println("Trying to receive packet...");
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.err.println("Failed to receive packet");
                continue;
            }

            String receivedString = new String(packet.getData(),  0, packet.getLength());

            returnIPAddress = packet.getAddress();
            returnPort = packet.getPort();

            System.out.println("Received: [" + receivedString + "] from: " + returnIPAddress + ":" + returnPort);

            System.out.println("Checking if is authorized...");
            String reply = authManager.isAuthorized(receivedString).toString();
            System.out.println("Reply: [" + reply + "]");

            System.out.println("Trying to send reply packet...");
            receiveData = reply.getBytes();
            packet = new DatagramPacket(receiveData, receiveData.length, returnIPAddress, returnPort);
            try {
                socket.send(packet);
            } catch (IOException e) {
                System.err.println("Failed to send reply packet");
                continue;
            }
            System.out.println("Packet sent.");
        }
        socket.close();
        System.out.println("Communication finished.");
    }
}
