package com.ctrlclass.server.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class Aluno {
    private String matricula;
    private String uid;
    private ArrayList<Marcacao> marcacoes;
    private boolean present;
    private LocalTime inTime;
    private LocalTime outTime;
    private Duration permanenceTime;
    private Duration outsideTime;
    private boolean validPresence;

    public Aluno(String matricula, String uid) {
        this.matricula = matricula;
        this.uid = uid;
        this.marcacoes = new ArrayList<>();
        present = false;
        inTime = null;
        outTime = null;
        permanenceTime = Duration.ZERO;
        outsideTime = Duration.ZERO;
        validPresence = false;
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

    public LocalTime getInTime() {
        return inTime;
    }

    public LocalTime getOutTime() {
        return outTime;
    }

    public Duration getPermanenceTime() {
        return permanenceTime;
    }

    public Duration getOutsideTime() {
        return outsideTime;
    }

    public void setOutsideTime(Duration outsideTime) {
        this.outsideTime = outsideTime;
    }

    public boolean isValidPresence() {
        return validPresence;
    }

    public void setValidPresence(boolean validPresence) {
        this.validPresence = validPresence;
    }

    public void addMarcacao(Marcacao marcacao) {
        marcacao.setMatricula(matricula);
        marcacao.setIn(marcacoes.size() % 2 == 0);
        marcacoes.add(marcacao);
    }

    public void computeTimes() {
        // Se houver marcações
        if(!marcacoes.isEmpty()) {
            // Define o horário de entrada
            inTime = marcacoes.get(0).getTime();

            for (int i = 1; i < marcacoes.size() ; i++) {
                // Se for marcação de saída
                if (!marcacoes.get(i).isIn()) {
                    // Incrementa o tempo de permanência
                    permanenceTime = permanenceTime.plus(Duration.between(marcacoes.get(i-1).getTime(), marcacoes.get(i).getTime()));
                }
                // Se for marcação de entrada
                else {
                    // Incrementa o tempo fora da sala
                    outsideTime = outsideTime.plus(Duration.between(marcacoes.get(i-1).getTime(), marcacoes.get(i).getTime()));
                }
            }
            // Se a última marcação for de saída
            if(!marcacoes.get(marcacoes.size()-1).isIn()) {
                // Define o horário de saída = horário da última marcação
                outTime = marcacoes.get(marcacoes.size()-1).getTime();
            }
        }
        // Se a quantidade de marcações for PAR, a presença é válida
        if(marcacoes.size() % 2 == 0) {
            validPresence = true;
        }
        // Se não houver marcações, permanecem os valores inicializados
    }

    public void computePresence(Duration classTime, Duration toleranceTime) {
        present = permanenceTime.plus(toleranceTime).compareTo(classTime) >= 0;
    }
}
