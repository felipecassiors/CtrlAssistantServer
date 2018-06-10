package com.ctrlclass.server;

import java.io.IOException;
import java.net.*;

public class Communication extends Thread {
    private boolean running;
    private AuthManager authManager;
    private MainScreenController controller;

    public Communication(MainScreenController controller, AuthManager authManager) {
        this.controller = controller;
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
        try{
            int port = 6000;
            byte[] receiveData = new byte[12];
            byte[] sendData;
            DatagramSocket serverSocket = new DatagramSocket(port);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            running = true;
            while(running)  {
                System.out.println("Waiting for some packet...");

                serverSocket.receive(receivePacket);
                String receivedString = new String( receivePacket.getData(), 0,  receivePacket.getLength() );
                System.out.println("Received: [" + receivedString + "] from: " + receivePacket.getAddress() + ":" + receivePacket.getPort());

                Boolean authorized = authManager.isAuthorized(receivedString);

                if(authorized) {
                    controller.updateLastLabel("UID [" + receivedString + "] autorizado");
                } else {
                    controller.updateLastLabel("UID [" + receivedString + "] autorizado");
                }

                sendData =  ( authorized.toString() ).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
                System.out.println("Sent: [" + sendData + "] to: " + receivePacket.getAddress() + ":" + receivePacket.getPort());
            }
            serverSocket.close();
            running = false;
        } catch (Exception e){
            System.err.println(e);
        }
    }
}
