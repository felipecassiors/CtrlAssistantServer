package com.ctrlclass.server;

import java.util.ArrayList;

public class AuthManager {

    private ArrayList<Aluno> alunos;
    private ArrayList<Marcacao> marcacoes;

    public AuthManager() {
        this.marcacoes = new ArrayList<>();
        this.alunos =  new ArrayList<>();
    }

    public boolean isAuthorized (String uid) {
        for (Aluno aluno: alunos) {
            if (aluno.getUid().equalsIgnoreCase(uid)) {
                Marcacao marcacao = new Marcacao(uid, aluno.getMatricula());
                aluno.getMarcacoes().add(marcacao);
                marcacoes.add(marcacao);
                return true;
            }
        }
        marcacoes.add(new Marcacao(uid));
        return false;
    }

    public static boolean isValidUid (String uid) {
        String sample = "043A98CABB2B80";
        return uid.length() == sample.length();
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public ArrayList<Marcacao> getMarcacoes() {
        return marcacoes;
    }

    public void setAlunos(ArrayList<Aluno> alunos) {
        this.alunos = alunos;
    }

}
