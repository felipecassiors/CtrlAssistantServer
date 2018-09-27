package com.ctrlassistant.server.model.repository;

import com.ctrlassistant.server.model.Discipline;
import com.ctrlassistant.server.model.Teacher;
import org.springframework.data.repository.CrudRepository;

public interface DisciplineRepository extends CrudRepository<Discipline, Integer> {
    Discipline findByTeacher(Teacher teacher);

    /*
        public Discipline getByDateTime(LocalDateTime dateTime) {
        DayOfWeek weekday = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        List<Discipline> disciplines = list();

        for (Discipline discipline : disciplines) {
            for (DisciplineTime disciplineTime : discipline.getDisciplineTimes()) {
                if (disciplineTime.getWeekDay().equals(weekday)) {
                    //TODO get class by now
                    if (disciplineTime.getTime().compareTo(time) > 10) {
                        return discipline;
                    }
                }
            }
        }
        return null;
    }

     */
}
