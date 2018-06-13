package com.ctrlclass.server;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class AuthManager {

    private ArrayList<Aluno> alunos;
    private ArrayList<Marcacao> marcacoes;

    public AuthManager() {
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

    public void setMarcacoes(ArrayList<Marcacao> marcacoes) {
        this.marcacoes = marcacoes;
    }
}
