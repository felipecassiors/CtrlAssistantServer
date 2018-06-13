package com.ctrlclass.server;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;

public class MainScreenController {

    private Button startButton;
    private Button stopButton;

    private Main main;

    public MainScreenController(Main main) {
        this.main = main;
    }

    public void initialize() {
        activateStartButton();
    }

    public void startButtonPressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir "+ FileManager.TITULO_ARQUIVO_CSV_AUTORIZADOS);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(startButton.getScene().getWindow());
        main.startButtonPressed(file);
        activateStopButton();
    }

    public void stopButtonPressed() {
        main.stopButtonPressed();
        activateStartButton();
    }

    public void activateStartButton() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
    }

    public void activateStopButton() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }


}
