package com.ctrlassistant.server.model.repository;

import com.ctrlassistant.server.model.Teacher;
import org.springframework.data.repository.CrudRepository;

public interface TeacherRepository extends CrudRepository<Teacher, Integer> {
    Teacher findByTag(String tag);

    boolean existsByTag(String tag);
}
