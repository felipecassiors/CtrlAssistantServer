package com.ctrlclass.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    MainScreenController mainScreenController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainScreenController = new MainScreenController(this);

        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("mainscreen.fxml"));
        mainViewLoader.setController(mainScreenController);
        Parent mainView = mainViewLoader.load();

        primaryStage.setTitle("Servidor - CtrlClass");
        primaryStage.setScene(new Scene(mainView));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public MainScreenController getMainScreenController() {
        return mainScreenController;
    }

    @Override
    public void stop() throws Exception {
        getMainScreenController().stopCommunication();
        super.stop();
    }
}
