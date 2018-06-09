package com.ctrlclass.server;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class MainScreenController {

    public Button startButton;
    public Button stopButton;
    private Communication communication = null;
    private FileManager fileManager = new FileManager();
    private AuthManager authManager = new AuthManager();
    private FrequenceManager frequenceManager;
    private Main main;

    public MainScreenController(Main main) {
        this.main = main;
    }

    public void startButtonPressed() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir "+ FileManager.TITULO_ARQUIVO_CSV_AUTORIZADOS);
        File file = fileChooser.showOpenDialog(startButton.getScene().getWindow());

        ArrayList<Aluno> alunos = fileManager.abrirArquivoCsvAutorizados(file);

        authManager.setAlunos(alunos);

        authManager.setStartTime(LocalTime.now());

        communication = new Communication(authManager);
        communication.start();

        activateStopButton();
    }

    public void stopButtonPressed() {
        stopCommunication();

        fileManager.criarArquivoCsvMarcacoes(authManager.getMarcacoes());
        fileManager.criarArquivoCsvFrequencia(authManager.getAlunos());

        authManager.setFinishTime(LocalTime.now());

        activateStartButton();
    }

    public void initialize() {
        activateStartButton();
    }

    public void stopCommunication () {
        if (communication != null ) {
            if (communication.isRunning()){
                communication.terminate();
            }
            communication = null;
        }
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
