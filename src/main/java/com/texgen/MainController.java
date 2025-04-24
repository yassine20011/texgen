package com.texgen;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import java.io.IOException;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import java.io.File;
import javafx.stage.DirectoryChooser;


public class MainController {

    @FXML private StackPane contentArea;
    @FXML private TreeView<File> treeView;

    @FXML
    public void loadHome() {
        loadView("editor.fxml");
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

    @FXML
    public void initialize() {

    }


    @FXML
    private void handleNewProject() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory for the new project");
        File selectedDir = directoryChooser.showDialog(contentArea.getScene().getWindow());
        if (selectedDir != null) {
            TreeItem<File> rootItem = createTreeItem(selectedDir);
            treeView.setRoot(rootItem);
            treeView.setShowRoot(true);
        }
    }

    private void dialogBox() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory for the new project");
        File selectedDir = directoryChooser.showDialog(contentArea.getScene().getWindow());
        if (selectedDir != null) {
            TreeItem<File> rootItem = createTreeItem(selectedDir);
            treeView.setRoot(rootItem);
            treeView.setShowRoot(true);
        }
    }


    private TreeItem<File> createTreeItem(File directory) {
        TreeItem<File> item = new TreeItem<>(directory);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively add subdirectories
                    item.getChildren().add(createTreeItem(file));
                } else {
                    // Add files as leaf nodes
                    item.getChildren().add(new TreeItem<>(file));
                }
            }
        }
        return item;
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
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