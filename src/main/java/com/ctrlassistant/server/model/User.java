package com.ctrlassistant.server.model;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public class User {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TAG = "tag";
    @Id
    @GeneratedValue
    @Column(name = ID, nullable = false)
    private Integer id = null;
    @Column(name = NAME, nullable = false)
    private String name;
    @Column(name = TAG, nullable = false)
    private String tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(tag, user.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tag);
    }
}
