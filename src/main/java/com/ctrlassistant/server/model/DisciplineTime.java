package com.ctrlassistant.server.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity(name = DisciplineTime.TABLE)
@Table(name = DisciplineTime.TABLE)
public class DisciplineTime {

    public static final String ID = "id";
    public static final String TABLE = "discipline_time";
    public static final String DISCIPLINE_ID = "discipline_id";
    @Id
    @GeneratedValue
    @Column(name = ID)
    private Integer id;

    public static final String TIME = "time";
    @Column(name = TIME)
    private LocalTime time;

    public static final String WEEKDAY = "weekday";
    @Column(name = WEEKDAY)
    private Integer weekDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisciplineTime disciplineTime = (DisciplineTime) o;
        return Objects.equals(id, disciplineTime.id) &&
                Objects.equals(time, disciplineTime.time) &&
                Objects.equals(weekDay, disciplineTime.weekDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, weekDay);
    }
}
