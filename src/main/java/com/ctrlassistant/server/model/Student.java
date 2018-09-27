package com.ctrlassistant.server.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = Student.TABLE)
@Table(name = Student.TABLE)
public class Student extends User {

    public static final String TABLE = "student";
    @Transient
    private boolean present = false;
    @Transient
    private boolean validPresence = false;
    @Transient
    private boolean in = false;
    @Transient
    private LocalDateTime inTime = null;
    @Transient
    private LocalDateTime outTime = null;
    @Transient
    private Duration permanenceTime = Duration.ZERO;
    @Transient
    private Duration outsideTime = Duration.ZERO;
    @Transient
    private List<Checking> checkings = new ArrayList<>();

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isValidPresence() {
        return validPresence;
    }

    public void setValidPresence(boolean validPresence) {
        this.validPresence = validPresence;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public LocalDateTime getInTime() {
        return inTime;
    }

    public void setInTime(LocalDateTime inTime) {
        this.inTime = inTime;
    }

    public LocalDateTime getOutTime() {
        return outTime;
    }

    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime;
    }

    public Duration getPermanenceTime() {
        return permanenceTime;
    }

    public void setPermanenceTime(Duration permanenceTime) {
        this.permanenceTime = permanenceTime;
    }

    public Duration getOutsideTime() {
        return outsideTime;
    }

    public void setOutsideTime(Duration outsideTime) {
        this.outsideTime = outsideTime;
    }

    public List<Checking> getCheckings() {
        return checkings;
    }

    public void setCheckings(List<Checking> checkings) {
        this.checkings = checkings;
    }

    public boolean insertChecking(Checking checking) {
        if(checkings.size() % 2 == 0) {
            checking.setIn(true);
            in = true;
        } else {
            checking.setIn(false);
            in = false;
        }
        this.checkings.add(checking);
        if(checkings.size() == 1) {
            inTime = checkings.get(0).getDateTime();
        }
        return in;
    }

    public void computePresence(Duration classTime, Duration toleranceTime) {
        for (Checking checking : checkings) {
            System.out.println(checking);
        }
        computeTimes();
        present = permanenceTime.plus(toleranceTime).compareTo(classTime) >= 0;
    }

    private void computeTimes() {
        // Se houver marcações
        if(!checkings.isEmpty()) {
            // Define o horário de entrada
            inTime = checkings.get(0).getDateTime();

            for (int i = 1; i < checkings.size() ; i++) {
                // Se for marcação de saída
                if (!checkings.get(i).isIn()) {
                    // Incrementa o tempo de permanência
                    permanenceTime = permanenceTime.plus(Duration.between(checkings.get(i-1).getDateTime(), checkings.get(i).getDateTime()));
                }
                // Se for marcação de entrada
                else {
                    // Incrementa o tempo fora da sala
                    outsideTime = outsideTime.plus(Duration.between(checkings.get(i-1).getDateTime(), checkings.get(i).getDateTime()));
                }
            }
            // Se a última marcação for de saída
            if(!checkings.get(checkings.size()-1).isIn()) {
                // Define o horário de saída = horário da última marcação
                outTime = checkings.get(checkings.size() - 1).getDateTime();
            }
        }
        // Se a quantidade de marcações for PAR, a presença é válida
        if(checkings.size() % 2 == 0) {
            validPresence = true;
        }
        // Se não houver marcações, permanecem os valores inicializados
    }
}
