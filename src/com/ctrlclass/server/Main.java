package com.ctrlclass.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;

public class Main extends Application {

    private MainScreenController controller;
    private FileManager fileManager;
    private AuthManager authManager;
    private FrequenceManager frequenceManager;
    private Communication communication;

    @Override
    public void start(Stage primaryStage) throws Exception{
        communication = new Communication();
        fileManager = new FileManager();
        authManager = new AuthManager();
        frequenceManager = new FrequenceManager();

        controller = new MainScreenController(this);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("mainscreen.fxml"));
        mainLoader.setController(controller);
        Parent mainView = mainLoader.load();

        primaryStage.setTitle("Servidor - CtrlClass");
        primaryStage.setScene(new Scene(mainView));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        communication.finish();
        super.stop();
    }

    public void startButtonPressed(File file){

        // Carrega os alunos do arquivo
        ArrayList<Aluno> alunos;
        if (file != null) {
            alunos = fileManager.abrirArquivoCsvAutorizados(file);
        } else {
            alunos = new ArrayList<>();
        }
        authManager = new AuthManager();
        authManager.setAlunos(alunos);

        frequenceManager = new FrequenceManager();
        frequenceManager.setStartTime(LocalTime.now());

        System.out.println("Hora de início da aula: "+frequenceManager.getStartTime().format(Util.TIME_FORMATTER));

        communication = new Communication();
        communication.setAuthManager(authManager);
        communication.start();
    }

    public void stopButtonPressed() {
        stopCommunication();

        fileManager.criarArquivoCsvMarcacoes(authManager.getMarcacoes());

        frequenceManager.setFinishTime(LocalTime.now());
        System.out.println("Hora de término da aula: "+frequenceManager.getFinishTime().format(Util.TIME_FORMATTER));

        frequenceManager.setAlunos(authManager.getAlunos());
        frequenceManager.computeFrequence();

        fileManager.criarArquivoCsvFrequencia(frequenceManager.getAlunos(), frequenceManager.getClassTime(), frequenceManager.getToleranceTime());
    }

    private void stopCommunication() {
        try {
            communication.finish();
            communication.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("Erro ao finalizar comunicação");
        }
    }
}
