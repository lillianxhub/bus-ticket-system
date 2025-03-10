package com.busticket.controllers;

import com.busticket.utils.SceneManager;
import javafx.event.ActionEvent;

public class SeatSelectionController {
    public void handleBack(ActionEvent actionEvent) {
        System.out.println("Back button clicked");
        SceneManager.switchScene("fxml/login.fxml");
    }
}
