package ua.shevchenko.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TourNumberDialogController {

    @FXML private Label tourNumberLabel;

    public void setTourNumberLabel(String tourNumber) {
        tourNumberLabel.setText(tourNumber);
    }
}
