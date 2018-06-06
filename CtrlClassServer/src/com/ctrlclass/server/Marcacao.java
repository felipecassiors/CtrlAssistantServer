package com.ctrlclass.server;

import java.time.LocalTime;

public class Marcacao {
    private String uid;
    private LocalTime horario;
    private String matricula;

    public Marcacao(String uid) {
        this.uid = uid;
        this.horario = LocalTime.now();
        this.matricula = "NEGADO";
    }

    public Marcacao(String uid, String matricula) {
        this.uid = uid;
        this.horario = LocalTime.now();
        this.matricula = matricula;
    }

    public String getUid() {
        return uid;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public String getMatricula() {
        return matricula;
    }
}
