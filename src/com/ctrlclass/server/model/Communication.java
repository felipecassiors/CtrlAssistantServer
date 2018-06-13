package com.ctrlclass.server.model;

import java.net.*;

public class Communication extends Thread {
    private boolean running;
    private AuthManager authManager;

    public Communication(AuthManager authManager) {
        this.authManager = authManager;
    }

    public boolean isRunning() {
        return running;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public void setAuthManager(AuthManager authManager) {
        this.authManager = authManager;
    }

    public void finish() {
        running = false;
    }

    @Override
    public void run() {
        try{
            int port = 6000;
            byte[] receiveData = new byte[12];
            byte[] sendData;
            DatagramSocket serverSocket = new DatagramSocket(port);
            serverSocket.setSoTimeout(1000);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            running = true;
            while(running)  {
                //System.out.println("Waiting for some packet...");

                try {
                    serverSocket.receive(receivePacket);
                } catch (SocketTimeoutException e) {
                    continue;
                }
                String receivedString = new String( receivePacket.getData(), 0,  receivePacket.getLength() );
                //System.out.println("Received: [" + receivedString + "] from: " + receivePacket.getAddress() + ":" + receivePacket.getPort());

                Boolean authorized = authManager.checkAuthorization(receivedString);

                String sendString = authorized.toString();
                sendData =  sendString.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
                //System.out.println("Sent: [" + sendString + "] to: " + receivePacket.getAddress() + ":" + receivePacket.getPort());
            }
            serverSocket.close();
            running = false;
        } catch (Exception e){
            System.err.println(e);
        }
    }
}
