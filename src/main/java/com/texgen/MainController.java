package com.texgen;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.Objects;
import javafx.scene.layout.StackPane;


public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void loadHome() {
        loadView("home.fxml");
    }

    @FXML
    private void loadSignIn() {
        loadView("/com/texgen/auth/SignIn.fxml");
    }

    @FXML
    private void loadSignUp() {
        loadView("/com/texgen/auth/SignUp.fxml");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            showError("Failed to load " + fxmlFile);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle("An error occurred");
        alert.show();
    }

}