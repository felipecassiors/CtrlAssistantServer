package com.ctrlclass.server;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class FrequenceManager {
    private ArrayList<Aluno> alunos;

    private LocalTime startTime;
    private LocalTime finishTime;

    private Duration classTime;
    private Duration toleranceTime;

    public FrequenceManager() {
        alunos = null;
    }

    public void computeFrequence() {
        classTime = Duration.between(startTime, finishTime);
        toleranceTime = classTime.dividedBy(2);

        System.out.println("Tempo de aula: "+Util.formatDuration(classTime));
        System.out.println("Tempo de tolerância: "+Util.formatDuration(toleranceTime));

        if (alunos != null) {
            for (Aluno aluno : alunos) {
                aluno.computeTimes();
                aluno.computePresence(classTime, toleranceTime);
            }
        }
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(ArrayList<Aluno> alunos) {
        this.alunos = alunos;
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

    public Duration getClassTime() {
        return classTime;
    }

    public void setClassTime(Duration classTime) {
        this.classTime = classTime;
    }

    public Duration getToleranceTime() {
        return toleranceTime;
    }

    public void setToleranceTime(Duration toleranceTime) {
        this.toleranceTime = toleranceTime;
    }
}
