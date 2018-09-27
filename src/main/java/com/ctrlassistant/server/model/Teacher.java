package com.ctrlassistant.server.model;

import javax.persistence.*;

@Entity(name = Teacher.TABLE)
@Table(name = Teacher.TABLE)
public class Teacher extends User {
    public static final String TABLE = "teacher";
    public static final String EMAIL = "email";
    @Column(name = EMAIL, nullable = false)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}