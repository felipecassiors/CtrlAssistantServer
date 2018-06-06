package com.ctrlclass.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    MainScreenController controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("mainscreen.fxml"));
        controller = (MainScreenController) fxmlLoader.getController();
        primaryStage.setTitle("CtrlClass Server");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /*
    @Override
    public void stop() throws Exception {
        controller.stopButtonPressed();
        super.stop();
    }*/
}
