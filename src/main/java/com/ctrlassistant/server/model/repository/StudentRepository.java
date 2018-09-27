package com.ctrlassistant.server.model.repository;

import com.ctrlassistant.server.model.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    Student findByTag(String tag);

    boolean existsByTag(String tag);
}
