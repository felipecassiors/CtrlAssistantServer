package com.ctrlassistant.server;

import com.ctrlassistant.server.model.Checking;
import com.ctrlassistant.server.model.repository.CheckingRepository;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class CheckingManager {

    @Autowired
    private CheckingRepository checkingRepository;
    private SimpleListProperty<Checking> checkings = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));

    public static boolean validateTag(String tag) {
        return tag.length() == 8;
    }

    public ObservableList<Checking> getCheckings() {
        return checkings.get();
    }

    public SimpleListProperty<Checking> checkingsProperty() {
        return checkings;
    }

    public Checking createAndSaveChecking(String tag) {
        Checking checking = createChecking(tag);

        checkingRepository.save(checking);

        Platform.runLater(() -> {
            if (checkings.size() >= 100) {
                checkings.remove(checkings.size()-1);
            }
            checkings.add(0, checking);
        });

        return checking;
    }

    public Checking createChecking(String tag) {
        Checking checking = new Checking();
        checking.setTag(tag);
        checking.setDateTime(LocalDateTime.now());

        return checking;
    }

}
