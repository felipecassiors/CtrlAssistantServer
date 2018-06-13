package com.ctrlclass.server;

import java.time.LocalTime;

public class Marcacao {
    private String uid;
    private LocalTime time;
    private String matricula;
    private Boolean in;

    public Marcacao(String uid, LocalTime time) {
        this.uid = uid;
        this.time = time;
        matricula = null;
        in = null;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getUid() {
        return uid;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getMatricula() {
        return matricula;
    }

    public Boolean isIn() {
        return in;
    }

    public void setIn(Boolean in) {
        this.in = in;
    }

    @Override
    public String toString() {
        if(in) {
            return "MATRÍCULA: ["+matricula+"] UID: ["+uid+"] entrou às "+ time.format(Util.TIME_FORMATTER);
        } else {
            return "MATRÍCULA: ["+matricula+"] UID: ["+uid+"] saiu às "+ time.format(Util.TIME_FORMATTER);
        }

    }
}
