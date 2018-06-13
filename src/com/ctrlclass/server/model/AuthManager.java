package com.ctrlclass.server.model;

import com.ctrlclass.server.control.Main;
import javafx.application.Platform;

import java.time.LocalTime;
import java.util.ArrayList;

public class AuthManager {

    private ArrayList<Aluno> alunos;
    private ArrayList<Marcacao> marcacoes;

    private Main main;

    public AuthManager(Main main) {
        this.main = main;
        this.marcacoes = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

    public Boolean checkAuthorization (String uid) {
        Marcacao marcacao = new Marcacao(uid, LocalTime.now());

        boolean authorized = false;

        if (alunos != null) {
            for (Aluno aluno : alunos) {
                if (aluno.getUid().equalsIgnoreCase(uid)) {
                    aluno.addMarcacao(marcacao);
                    authorized = true;
                }
            }
        }
        System.out.println(marcacao.toString());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                main.getStrings().add(marcacao.toString());
            }
        });
        marcacoes.add(marcacao);
        return authorized;
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(ArrayList<Aluno> alunos) {
        this.alunos = alunos;
    }

    public ArrayList<Marcacao> getMarcacoes() {
        return marcacoes;
    }

    public void clean() {
        this.marcacoes = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

}
