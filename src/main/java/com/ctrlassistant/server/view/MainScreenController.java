package com.ctrlassistant.server.view;

import com.ctrlassistant.server.CheckingManager;
import com.ctrlassistant.server.ClassManager;
import com.ctrlassistant.server.CommunicationService;
import com.ctrlassistant.server.Util;
import com.ctrlassistant.server.model.Checking;
import com.ctrlassistant.server.model.Student;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Controller
public class MainScreenController implements Initializable {

    public static final String TITLE = "Ctrl Assistant";
    
    public Button startCommunicationButton;
    public Button stopCommunicationButton;
    public ListView<Checking> checkingsListView;
    public TextField statusTextField;
    public TextField portTextField;
    public TableView<Student> studentsTableView;
    public TableColumn<Student, Boolean> localeCollumn;
    public TableColumn<Student, String> nameCollumn;
    public TableColumn<Student, Integer> idCollumn;
    public TableColumn<Student, LocalDateTime> inTimeCollumn;
    public TextField ipTextField;
    public TextField toleranceTextField;
    public TextField durationTextField;
    public TextField finishTimeTextField;
    public TextField startTimeTextField;
    public TextField teacherTextField;
    public TextField disciplineTextField;

    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private CheckingManager checkingManager;
    @Autowired
    private ClassManager classManager;
    private ListProperty<Student> students = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
    private SimpleBooleanProperty onClass = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        startCommunicationButton.disableProperty().bind(communicationService.runningProperty());

        stopCommunicationButton.disableProperty().bind(communicationService.runningProperty().not());

        statusTextField.textProperty().bind(communicationService.stateProperty().asString());

        checkingsListView.itemsProperty().bind(checkingManager.checkingsProperty());

        portTextField.setText(String.valueOf(CommunicationService.DEFAULT_PORT));
        portTextField.disableProperty().bind(communicationService.runningProperty());

        classManager.statusProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case WAITING_TO_START:
                    students.removeAll();
/*                    classManager.getCurrentClass().getStudents().forEach((integer, student) -> {
                        students.add(student);
                    });
                    classManager.getCurrentClass().getStudents().addListener((MapChangeListener<? super Integer, ? super Student>) change -> {
                        if (change.wasAdded()) {
                            students.add(change.getValueAdded());
                        }
                        if (change.wasRemoved()) {
                            students.remove(change.getValueRemoved());
                        }
                    });*/
                    classManager.getCurrentClass().updateTableProperty().addListener((observable1, oldValue1, newValue1) -> {
                        students.clear();
                        classManager.getCurrentClass().getStudents().forEach((integer, student) -> {
                            students.add(student);
                        });
                    });
                    break;
                case STARTED:
                    disciplineTextField.setText(classManager.getCurrentClass().getDiscipline().getName());
                    teacherTextField.setText(classManager.getCurrentClass().getDiscipline().getTeacher().getName());
                    startTimeTextField.setText(classManager.getCurrentClass().getStartDateTime().format(Util.TIME_FORMATTER));

                    disciplineTextField.setDisable(false);
                    teacherTextField.setDisable(false);
                    startTimeTextField.setDisable(false);
                    finishTimeTextField.setDisable(true);
                    durationTextField.setDisable(true);
                    toleranceTextField.setDisable(true);
                    break;
                case WAITING_TO_FINISH:
                    disciplineTextField.setDisable(true);
                    teacherTextField.setDisable(true);
                    startTimeTextField.setDisable(true);
                    finishTimeTextField.setDisable(false);
                    durationTextField.setDisable(false);
                    toleranceTextField.setDisable(false);

                    finishTimeTextField.setText(classManager.getCurrentClass().getFinishDateTime().format(Util.TIME_FORMATTER));
                    durationTextField.setText(Util.formatDuration(classManager.getCurrentClass().getClassTime()));
                    toleranceTextField.setText(Util.formatDuration(classManager.getCurrentClass().getToleranceTime()));
                    break;
                case FINISHED:
                    disciplineTextField.setDisable(true);
                    teacherTextField.setDisable(true);
                    startTimeTextField.setDisable(true);
                    finishTimeTextField.setDisable(true);
                    durationTextField.setDisable(true);
                    toleranceTextField.setDisable(true);

                    disciplineTextField.setText(null);
                    teacherTextField.setText(null);
                    startTimeTextField.setText(null);
                    finishTimeTextField.setText(null);
                    durationTextField.setText(null);
                    toleranceTextField.setText(null);

                    students.clear();
                    break;
            }
        });

        studentsTableView.itemsProperty().bind(students);

        idCollumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCollumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        localeCollumn.setCellFactory(param -> {
            TableCell<Student, Boolean> cell = new TableCell<Student, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        if(item) {
                            setText("Dentro");
                        } else {
                            setText("Fora");
                        }
                    }
                }
            };
            return cell;
        });
        localeCollumn.setCellValueFactory(new PropertyValueFactory<>("in"));
        inTimeCollumn.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        inTimeCollumn.setCellFactory(column -> {
            TableCell<Student, LocalDateTime> cell = new TableCell<Student, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty || item == null) {
                        setText(null);
                    }
                    else {
                        setText(item.format(Util.TIME_FORMATTER));
                    }
                }
            };
            return cell;
        });
        inTimeCollumn.setCellValueFactory(new PropertyValueFactory<>("inTime"));

        startCommunicationButtonPressed();
    }


    public void startCommunicationButtonPressed() {
        boolean valid = true;
        int port = 0;
        try {
            port = Integer.parseInt(portTextField.getText());
        } catch (NumberFormatException e) {
            valid = false;
        } finally {
            if(port > 9999 || port <= 0) {
                valid = false;
            }
        }
        if(!valid) {
            port = CommunicationService.DEFAULT_PORT;
            portTextField.setText(String.valueOf(CommunicationService.DEFAULT_PORT));
        }
        communicationService.start(port);
    }

    public void stopCommunicationButtonPressed() {
        communicationService.stop();
    }

    public void onClose () {
        startCommunicationButton.disableProperty().unbind();
        stopCommunicationButton.disableProperty().unbind();
        statusTextField.textProperty().unbind();
        checkingsListView.itemsProperty().unbind();
    }

    public void updateTable() {
        students.clear();
        students.addAll(new ArrayList<>(classManager.getCurrentClass().getStudents().values()));
    }

}
