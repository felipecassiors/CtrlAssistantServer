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

    public void startButtonPressed() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir "+ FileManager.TITULO_ARQUIVO_CSV_AUTORIZADOS);
        File file = fileChooser.showOpenDialog(startButton.getScene().getWindow());

        ArrayList<Aluno> alunos = fileManager.abrirArquivoCsvAutorizados(file);

        authManager.setAlunos(alunos);

        authManager.setStartTime(LocalTime.now());

        communication = new Communication(authManager);
        communication.start();

        startButton.setVisible(false);
        stopButton.setVisible(true);
    }

    public void stopButtonPressed() {
        stopCommunication();


        fileManager.criarArquivoCsvMarcacoes(authManager.getMarcacoes());
        fileManager.criarArquivoCsvFrequencia(authManager.getAlunos());

        authManager.setFinishTime(LocalTime.now());

        stopButton.setVisible(false);
        startButton.setVisible(true);
    }

    public void initialize() {
        startButton.setVisible(true);
        stopButton.setVisible(false);
    }

    public void stopCommunication () {
        if (communication != null || communication.isRunning()) {

            communication.terminate();
            communication = null;

        }
    }


}
