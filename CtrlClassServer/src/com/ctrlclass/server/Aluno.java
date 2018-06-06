package com.ctrlclass.server;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class Aluno {
    private String matricula;
    private String uid;
    private ArrayList<Marcacao> marcacoes;
    private Boolean present;
    private LocalTime inTime;
    private LocalTime outTime;
    private Duration permanenceTime;

    public Aluno(String matricula, String uid) {
        this.matricula = matricula;
        this.uid = uid;
        this.marcacoes = new ArrayList<>();
        present = false;
        inTime = null;
        outTime = null;
        permanenceTime = null;
    }

    public Boolean isPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
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

    public void addMarcacao (Marcacao marcacao) {
        marcacoes.add(marcacao);
        if(marcacoes.size() == 1) {
            inTime = marcacoes.get(0).getHorario();
        }
        if(marcacoes.size() %2 == 0) {
            outTime = marcacoes.get(marcacoes.size() - 1).getHorario();
            permanenceTime = Duration.between(inTime, outTime);
        }
    }

    public LocalTime getInTime() {
        return inTime;
    }

    public void setInTime(LocalTime inTime) {
        this.inTime = inTime;
    }

    public LocalTime getOutTime() {
        return outTime;
    }

    public void setOutTime(LocalTime outTime) {
        this.outTime = outTime;
    }

    public Duration getPermanenceTime() {
        return permanenceTime;
    }

    public void setPermanenceTime(Duration permanenceTime) {
        this.permanenceTime = permanenceTime;
    }
}
