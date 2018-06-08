package com.ctrlclass.server;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class AuthManager {

    private ArrayList<Aluno> alunos;
    private ArrayList<Marcacao> marcacoes;

    private LocalTime startTime;
    private LocalTime finishTime;
    private Duration classDuration;

    public AuthManager() {
        this.marcacoes = new ArrayList<>();
        this.alunos =  new ArrayList<>();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public Boolean isAuthorized (String uid) {
        for (Aluno aluno: alunos) {
            if (aluno.getUid().equalsIgnoreCase(uid)) {
                Marcacao marcacao = new Marcacao(uid, aluno.getMatricula());
                aluno.addMarcacao(marcacao);
                marcacoes.add(marcacao);
                return true;
            }
        }
        marcacoes.add(new Marcacao(uid));
        return false;
    }

    public static boolean isValidUid (String uid) {
        String sample = "FA CC 36 A5";
        return uid.length() == sample.length();
    }

    public void computeFrequence() {
        classDuration = Duration.between(startTime, finishTime);
        System.out.println("Class duration: "+classDuration.toString());

        for (Aluno aluno :  alunos) {
            aluno.setPresent(aluno.getPermanenceTime().compareTo(classDuration) >= 0);
        }
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
