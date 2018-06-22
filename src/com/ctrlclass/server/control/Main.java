package com.ctrlclass.server.control;

import com.ctrlclass.server.view.MainScreenController;
import com.ctrlclass.server.model.Util;
import com.ctrlclass.server.model.*;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalTime;
import java.util.*;

public class Main extends Application {

    private MainScreenController controller;
    private FileManager fileManager;
    private AuthManager authManager;
    private FrequenceManager frequenceManager;
    private Communication communication;

    private ObservableList<String> strings;

    @Override
    public void start(Stage primaryStage) throws Exception{
        fileManager = new FileManager();
        authManager = new AuthManager(this);
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
        stopCommunication();
        super.stop();
    }

    public void startCommunication(){
        if(communication == null || !communication.isRunning()) {
            communication = new Communication(authManager);
            communication.start();
            System.out.println("Comunicação iniciada");

            strings = FXCollections.observableArrayList();
            ListProperty<String> listProperty = new SimpleListProperty<>();
            listProperty.setValue((ObservableList<String>) strings);
            controller.getMarcacoesList().itemsProperty().bind(listProperty);
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
            controller.getMarcacoesList().itemsProperty().unbind();
            System.out.println("Comunicação encerrada");
        }
    }

    public void finishClass() {
        frequenceManager.setFinishTime(LocalTime.now());
        frequenceManager.computeClassTime();

        controller.getFinishTimeText().setText(frequenceManager.getFinishTime().format(Util.TIME_FORMATTER));
        controller.getClassTimeText().setText(Util.formatDuration(frequenceManager.getClassTime()));
        controller.getToleranceTimeText().setText(Util.formatDuration(frequenceManager.getToleranceTime()));

        System.out.println("Aula encerrada");
    }

    public void startClass(File file) {
        if (file != null) {
            ArrayList<Aluno> alunos;
            alunos = fileManager.abrirArquivoCsvAutorizados(file);
            authManager.setAlunos(alunos);

            frequenceManager = new FrequenceManager();
            frequenceManager.setStartTime(LocalTime.now());

            ArrayList<String> strings = new ArrayList<>();
            for (Aluno aluno: alunos) {
                strings.add(aluno.getMatricula() + " - UID: ["+aluno.getUid()+"]");
            }
            ObservableList<String> items = FXCollections.observableArrayList (strings);
            controller.getList().setItems(items);

            controller.getStartTimeText().setText(frequenceManager.getStartTime().format(Util.TIME_FORMATTER));

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

    public ObservableList<String> getStrings() {
        return strings;
    }
}
