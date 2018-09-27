package com.ctrlassistant.server;

import com.ctrlassistant.server.model.Checking;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

@Component
public class CommunicationService extends Service {
    public static final int DEFAULT_PORT = 2875;
    private static final int TIMEOUT = 2000;
    private static final int PACKET_SIZE = 64;

    @Autowired
    private CheckingManager checkingManager;
    @Autowired
    private ClassManager classManager;
    private boolean run = false;
    private Integer port = DEFAULT_PORT;

    @Override
    protected Task createTask() {
        return new CommunicationTask();
    }

    public void start(int port) {
        if (!isRunning()) {
            this.port = port;
            super.restart();
        }
    }

    public void stop() {
        if (isRunning()) {
            run = false;
            System.out.println("Communication stopped");
        }
    }

    public Integer getPort() {
        return port;
    }

    class CommunicationTask extends Task {

        private DatagramSocket socket;

        @Override
        public Void call() {
            try {
                //Setting up socket
                if (port == null) {
                    port = DEFAULT_PORT;
                }
                socket = new DatagramSocket(port);
                socket.setSoTimeout(TIMEOUT);

                System.out.println("Communication started on port " + socket.getInetAddress() + ":" + port);
                run = true;
                while (run) {

                    //Setting up packet for receiving data
                    byte[] receiveData = new byte[PACKET_SIZE];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                    //Trying to receive data
                    try {
                        socket.receive(receivePacket);
                    } catch (SocketTimeoutException e) {
                        //Timeout, try again
                        ///checkingManager.createAndSaveChecking("12345678");
                        continue;
                    }
                    //Parsing receive data into String
                    String receivedString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Received: [" + receivedString + "] from: " + receivePacket.getAddress() + ":" + receivePacket.getPort());

                    //Checking if data received is a TAG
                    if (CheckingManager.validateTag(receivedString)) {
                        //Sending confirmation of receipt to client
                        if(classManager.userExists(receivedString)) {
                            sendString("OK", receivePacket.getAddress(), receivePacket.getPort());
                        } else {
                            sendString("NOT", receivePacket.getAddress(), receivePacket.getPort());
                        }

                        Checking checking = checkingManager.createAndSaveChecking(receivedString);
                        classManager.checkTag(checking);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void succeeded() {
            socket.close();
            run = false;
        }

        @Override
        protected void failed() {
            getException().printStackTrace();
        }

        private void sendString(String string, InetAddress address, int port) throws Exception {
            byte[] data = string.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Sent: [" + string + "] to: " + address + ":" + port);
        }
    }

}
