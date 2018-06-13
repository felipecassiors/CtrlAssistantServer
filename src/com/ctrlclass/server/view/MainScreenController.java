package com.ctrlclass.server.view;

import com.ctrlclass.server.control.Main;
import com.ctrlclass.server.model.FileManager;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;

public class MainScreenController {

    public Button startCommunicationButton;
    public Button stopCommunicationButton;
    public Button startClassButton;
    public Button finishClassButton;
    public Button generateReportsButton;

    private Main main;

    public MainScreenController(Main main) {
        this.main = main;
    }

    public void initialize() {
        enableStartCommunicationButton();
        enableStartClassButton();
        generateReportsButton.setDisable(true);
    }

    public void startCommunicationButtonPressed() {
        main.startCommunication();

        enableStopCommunicationButton();
    }

    public void stopCommunicationButtonPressed() {
        main.stopCommunication();

        enableStartCommunicationButton();
    }

    public void startClassButtonPressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir "+ FileManager.TITULO_ARQUIVO_CSV_AUTORIZADOS);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(startCommunicationButton.getScene().getWindow());

        if (file != null) {
            main.startClass(file);
            startCommunicationButtonPressed();

            enableFinishClassButton();
        }
    }

    public void finishClassButtonPressed() {
        main.finishClass();

        enableStartClassButton();
        generateReportsButton.setDisable(false);
    }

    public void generateReportsButtonPressed() {
        main.generateReports();
        generateReportsButton.setDisable(true);
    }
    private void enableStartCommunicationButton() {
        startCommunicationButton.setDisable(false);
        stopCommunicationButton.setDisable(true);
    }

    private void enableStopCommunicationButton() {
        stopCommunicationButton.setDisable(false);
        startCommunicationButton.setDisable(true);
    }

    private void enableStartClassButton() {
        startClassButton.setDisable(false);
        finishClassButton.setDisable(true);
    }

    private void enableFinishClassButton() {
        finishClassButton.setDisable(false);
        startClassButton.setDisable(true);
    }

}
