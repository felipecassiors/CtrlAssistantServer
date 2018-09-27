package com.ctrlassistant.server.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = Discipline.TABLE)
@Table(name = Discipline.TABLE)
public class Discipline {

    public static final String ID = "id";
    public static final String TABLE = "discipline";
    public static final String NAME = "name";
    public static final String TEACHER_ID = "teacher_id";
    @Id
    @GeneratedValue
    @Column(name = ID)
    private Integer id;
    @Column(name = NAME)
    private String name;
    @ManyToOne
    @JoinColumn(name = TEACHER_ID)
    private Teacher teacher;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = DisciplineHasStudent.TABLE,
            joinColumns = {@JoinColumn(name = DisciplineHasStudent.DISCIPLINE_ID)},
            inverseJoinColumns = {@JoinColumn(name = DisciplineHasStudent.STUDENT_ID)})
    private List<Student> students = new ArrayList<>();
    @OneToMany
    @JoinColumn(name = DisciplineTime.DISCIPLINE_ID)
    private List<DisciplineTime> disciplineTimes;

    public Discipline() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<DisciplineTime> getDisciplineTimes() {
        return disciplineTimes;
    }

    public void setDisciplineTimes(List<DisciplineTime> disciplineTimes) {
        this.disciplineTimes = disciplineTimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discipline discipline = (Discipline) o;
        return Objects.equals(id, discipline.id) &&
                Objects.equals(name, discipline.name) &&
                Objects.equals(teacher, discipline.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, teacher);
    }

    public class DisciplineHasStudent {
        public static final String TABLE = "discipline_has_student";
        public static final String STUDENT_ID = "student_id";
        public static final String DISCIPLINE_ID = "discipline_id";
    }
}
