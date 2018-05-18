package com.ctrlclass.server;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class MainScreenController {

    public TextField ipAddressField;
    private Communication communication;

    public void startButtonPressed(ActionEvent actionEvent) {
        communication = new Communication(ipAddressField.getText());
        communication.start();
    }

    public void stopButtonPressed(ActionEvent actionEvent) {

        if (communication != null || communication.isRunning()) {

            try {
                communication.terminate();
                communication.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Can't stop communication, not running");
        }
    }
}
