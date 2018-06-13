package com.ctrlclass.server.control;

import com.ctrlclass.server.view.MainScreenController;
import com.ctrlclass.server.model.Util;
import com.ctrlclass.server.model.*;
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
        fileManager = new FileManager();
        authManager = new AuthManager();
        frequenceManager = null;
        communication = null;

        controller = new MainScreenController(this);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("../view/mainscreen.fxml"));
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

    public void startCommunication(){
        if(communication == null || !communication.isRunning()) {
            communication = new Communication(authManager);
            communication.start();
            System.out.println("Comunicação iniciada");
        }
    }

    public void stopCommunication() {
        if(communication != null || communication.isRunning()){
            try {
                communication.finish();
                communication.join();
                communication = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("Erro ao finalizar comunicação");
            }
            System.out.println("Comunicação encerrada");
        }
    }

    public void finishClass() {
        frequenceManager.setFinishTime(LocalTime.now());
        System.out.println("Aula encerrada");
    }

    public void startClass(File file) {
        if (file != null) {
            ArrayList<Aluno> alunos;
            alunos = fileManager.abrirArquivoCsvAutorizados(file);
            authManager.setAlunos(alunos);

            frequenceManager = new FrequenceManager();
            frequenceManager.setStartTime(LocalTime.now());

            System.out.println("Aula iniciada");
        }
    }

    public void generateReports() {
        fileManager.criarArquivoCsvMarcacoes(authManager.getMarcacoes());

        frequenceManager.setAlunos(authManager.getAlunos());

        frequenceManager.computeFrequence();

        fileManager.criarArquivoCsvFrequencia(frequenceManager);

        authManager.clean();
    }
}
