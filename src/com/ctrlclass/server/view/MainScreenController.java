package com.ctrlclass.server.view;

import com.ctrlclass.server.control.Main;
import com.ctrlclass.server.model.FileManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class MainScreenController {

    @FXML
    private Button startCommunicationButton;

    @FXML
    private Button stopCommunicationButton;

    @FXML
    private Button startClassButton;

    @FXML
    private Button finishClassButton;

    @FXML
    private Button generateReportsButton;

    @FXML
    private ListView<String> list = new ListView<String>();

    @FXML
    private ListView<String> marcacoesList = new ListView<String>();

    @FXML
    private TextField startTimeText;

    @FXML
    private TextField finishTimeText;

    @FXML
    private TextField classTimeText;

    @FXML
    private TextField toleranceTimeText;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    private Main main;

    public MainScreenController(Main main) {
        this.main = main;
    }

    public void initialize() {
        enableStartCommunicationButton();
        enableStartClassButton();
        generateReportsButton.setDisable(true);

        marcacoesList.setDisable(true);
        list.setDisable(true);
    }

    public void startCommunicationButtonPressed() {
        main.startCommunication();

        marcacoesList.setDisable(false);
        enableStopCommunicationButton();
    }

    public void stopCommunicationButtonPressed() {
        main.stopCommunication();

        marcacoesList.setDisable(true);
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

            list.setDisable(false);
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

        list.setDisable(true);

        classTimeText.setText("");
        toleranceTimeText.setText("");
        startTimeText.setText("");
        finishTimeText.setText("");

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

    public ListView<String> getList() {
        return list;
    }

    public TextField getStartTimeText() {
        return startTimeText;
    }

    public TextField getFinishTimeText() {
        return finishTimeText;
    }

    public TextField getClassTimeText() {
        return classTimeText;
    }

    public TextField getToleranceTimeText() {
        return toleranceTimeText;
    }

    public ListView<String> getMarcacoesList() {
        return marcacoesList;
    }

    public TextField getIpTextField() {
        return ipTextField;
    }

    public TextField getPortTextField() {
        return portTextField;
    }
}
