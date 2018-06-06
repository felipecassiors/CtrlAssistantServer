package com.ctrlclass.server;

import java.util.ArrayList;

public class Aluno {
    private String matricula;
    private String uid;
    private ArrayList<Marcacao> marcacoes;

    public Aluno(String matricula, String uid) {
        this.matricula = matricula;
        this.uid = uid;
        this.marcacoes = new ArrayList<>();
    }

    public String getMatricula() {
        return matricula;
    }

    public String getUid() {
        return uid;
    }

    public ArrayList<Marcacao> getMarcacoes() {
        return marcacoes;
    }
}
