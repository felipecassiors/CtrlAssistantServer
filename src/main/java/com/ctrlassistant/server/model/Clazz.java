package com.ctrlassistant.server.model;

import com.ctrlassistant.server.Util;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Clazz {
    private Discipline discipline;
    private LocalDateTime startDateTime = null;
    private LocalDateTime finishDateTime = null;
    private Map<Integer, Student> students = new HashMap<>();
    private Duration classTime = Duration.ZERO;
    private Duration toleranceTime = Duration.ZERO;
    private SimpleBooleanProperty finished = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty updateTable = new SimpleBooleanProperty(false);

    public Clazz() {
    }

    public Clazz(Discipline discipline, LocalDateTime startDateTime) {
        start(discipline, startDateTime);
    }

    public void start(Discipline discipline, LocalDateTime startDateTime) {
        if(finished.get()) {
            this.discipline = discipline;
            this.startDateTime = startDateTime;

            System.out.println("Quantidade de estudantes da lista: "+discipline.getStudents().size());

            discipline.getStudents().forEach(student -> students.putIfAbsent(student.getId(), student));
            students.forEach((integer, student) -> {
                if(student.getCheckings().size() > 0 && student.getInTime().compareTo(startDateTime) > 1) {
                    students.remove(integer, student);
                }
            });
            finished.set(false);
            System.out.println("Aula iniciada, disciplina "+discipline.getName()+" às "+startDateTime.format(Util.TIME_FORMATTER));
        }
    }

    public void finish(LocalDateTime finishDateTime) {
        if(!finished.get()) {
            this.finishDateTime = finishDateTime;
            classTime = Duration.between(startDateTime, this.finishDateTime);
            toleranceTime = classTime.dividedBy(5);
            students.forEach((integer, student) -> student.computePresence(classTime, toleranceTime));
            System.out.println("Aula finalizada às "+finishDateTime.format(Util.TIME_FORMATTER));
            finished.set(true);
        }
    }

    public void computePresence() {
        if(finished.get()) {
            students.forEach((integer, student) -> student.computePresence(classTime, toleranceTime));
        }
    }

    public int getQuantityOfStudentsInside() {
        AtomicInteger result = new AtomicInteger(0);
        students.forEach((integer, student) -> {
            if(student.isIn()) {
                result.getAndIncrement();
            }
        });
        return result.get();
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getFinishDateTime() {
        return finishDateTime;
    }

    public void setFinishDateTime(LocalDateTime finishDateTime) {
        this.finishDateTime = finishDateTime;
    }

    public Map<Integer, Student> getStudents() {
        return students;
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

    public boolean isFinished() {
        return finished.get();
    }

    public void setFinished(boolean finished) {
        this.finished.set(finished);
    }

    public SimpleBooleanProperty finishedProperty() {
        return finished;
    }

    public boolean isUpdateTable() {
        return updateTable.get();
    }

    public SimpleBooleanProperty updateTableProperty() {
        return updateTable;
    }
}
