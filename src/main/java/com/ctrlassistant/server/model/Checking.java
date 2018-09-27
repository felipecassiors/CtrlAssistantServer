package com.ctrlassistant.server.model;

import com.ctrlassistant.server.Util;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = Checking.TABLE)
@Table(name = Checking.TABLE)
public class Checking {

    public static final String ID = "id";
    public static final String TABLE = "checking";
    public static final String TAG = "tag";
    public static final String DATE_TIME = "date_time";
    @Id
    @GeneratedValue
    @Column(name = ID, nullable = false)
    private Integer id;
    @Column(name = TAG, nullable = false)
    private String tag;
    @Column(name = DATE_TIME, nullable = false)
    private LocalDateTime dateTime;
    @Transient
    private boolean in = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    @Override
    public String toString() {
        return "TAG: [" + tag + "] em " + dateTime.format(Util.DATE_TIME_FORMATTER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checking checking = (Checking) o;
        return Objects.equals(id, checking.id) &&
                Objects.equals(tag, checking.tag) &&
                Objects.equals(dateTime, checking.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag, dateTime);
    }
}
