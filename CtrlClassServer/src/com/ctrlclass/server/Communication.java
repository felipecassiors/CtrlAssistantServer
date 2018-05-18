package com.ctrlclass.server;

import java.io.IOException;
import java.net.*;

public class Communication extends Thread {

    private String serverHostname;
    private DatagramSocket ds;
    private InetAddress ip;
    private DatagramPacket send, rec;
    private String receivedString;

    private boolean running;

    public Communication(String serverHostname) {
        this.serverHostname = serverHostname;
        try {
            byte[] receiveData = new byte[1024];
            ip = InetAddress.getByName(serverHostname);
            ds = new DatagramSocket();
            rec = new DatagramPacket(receiveData, receiveData.length);
            ds.setSoTimeout(10000);
        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (SocketException e) {
            e.printStackTrace();

        }
        running = true;
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
        System.out.println("Communication started");
        while (running) {

            try {
                ds.receive(rec);
                receivedString = new String(rec.getData());

                returnIPAddress = rec.getAddress();
                returnPort = rec.getPort();

                System.out.println("Message: " + receivedString);
                System.out.println("Server: " + returnIPAddress + ":" + returnPort);

                ds.close();
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
        }
        System.out.println("Communication finished");
    }
}
