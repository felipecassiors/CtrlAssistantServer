package com.ctrlassistant.server;

import com.ctrlassistant.server.model.*;
import com.ctrlassistant.server.model.repository.DisciplineRepository;
import com.ctrlassistant.server.model.repository.StudentRepository;
import com.ctrlassistant.server.model.repository.TeacherRepository;
import com.ctrlassistant.server.view.MainScreenController;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClassManager {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private MainScreenController mainScreenController;

    private Clazz currentClass = null;
    private SimpleObjectProperty<ClassStatus> status = new SimpleObjectProperty<>(ClassStatus.FINISHED);

    public User findUserByTag(String tag) {
        User user = teacherRepository.findByTag(tag);
        if(user == null) {
            user = studentRepository.findByTag(tag);
        }
        return user;
    }

    public boolean userExists(String tag) {
        return findUserByTag(tag) != null;
    }

    public boolean checkTag(Checking checking) {
        User user = findUserByTag(checking.getTag());
        if(user == null) {
            return false;
        }
        if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            System.out.print("Professor "+teacher.getName());
            //Start class
            if (status.get() == ClassStatus.WAITING_TO_START || status.get() == ClassStatus.FINISHED) {
                System.out.println(" entrou");
                Discipline discipline = disciplineRepository.findByTeacher(teacher);
                if(discipline != null) {
                    System.out.println("Iniciando aula na disciplina "+discipline.getName());
                    startClass(discipline,checking.getDateTime());
                } else {
                    System.out.println("Nenhuma disciplina encontrada para este professor");
                }
            }
            //Finish class
            else if (status.get() == ClassStatus.STARTED){
                System.out.println(" saiu");
                firstFinishClass(checking.getDateTime());
            }
        } else if(user instanceof Student) {
            Student student = (Student) user;
            System.out.print("Aluno " + student.getName());
            if(status.get() == ClassStatus.FINISHED) {
                startPreviousClass();
            }
            currentClass.getStudents().putIfAbsent(student.getId(), student);
            student = currentClass.getStudents().get(student.getId());
            if(student.insertChecking(checking)) {
                System.out.println(" entrou");
            } else {
                System.out.println(" saiu");
            }
            mainScreenController.updateTable();
            //currentClass.getStudents().replace(student.getId(), student);
            if (status.get() == ClassStatus.WAITING_TO_FINISH) {
                int quantityOfStudentsInside = currentClass.getQuantityOfStudentsInside();
                System.out.println("Estudantes dentro da sala = "+quantityOfStudentsInside);
                if (quantityOfStudentsInside <= 0) {
                    secondFinishClass();
                }
            }
        }
        return true;
    }

    private void startClass(Discipline discipline, LocalDateTime startDateTime) {
        startPreviousClass();
        currentClass.start(discipline,startDateTime);
        mainScreenController.updateTable();
        status.set(ClassStatus.STARTED);
    }

    private void startPreviousClass() {
        if(currentClass == null) {
            currentClass = new Clazz();
        }
        status.set(ClassStatus.WAITING_TO_START);
    }

    private void firstFinishClass(LocalDateTime finishDateTime) {
        currentClass.finish(finishDateTime);
        status.set(ClassStatus.WAITING_TO_FINISH);
        int quantityOfStudentsInside = currentClass.getQuantityOfStudentsInside();
        System.out.println("Estudantes dentro da sala = "+quantityOfStudentsInside);
        if (quantityOfStudentsInside <= 0) {
            secondFinishClass();
        }
    }

    private void secondFinishClass() {
        System.out.println("Aula finalizada e pronto para gerar relatórios");
        currentClass.computePresence();
        status.set(ClassStatus.FINISHED);
        new FileCreater(currentClass).criarArquivoCsvFrequencia();
        currentClass = null;
    }

    public Clazz getCurrentClass() {
        return currentClass;
    }

    public ClassStatus getStatus() {
        return status.get();
    }

    public SimpleObjectProperty<ClassStatus> statusProperty() {
        return status;
    }
}
