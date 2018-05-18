package com.ctrlclass.server;

public class Aluno {
    private int matricula;
    private int uid;

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean checkUid(int uid) {
        return this.uid == uid;
    }
}
