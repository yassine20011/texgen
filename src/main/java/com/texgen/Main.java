package com.texgen;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.5);

        TextArea textEditor = new TextArea();
        textEditor.setPromptText("Write your text here...");

        WebView preview = new WebView();

        // Sync text changes to the preview
        textEditor.textProperty().addListener((obs, oldText, newText) -> {
            preview.getEngine().loadContent("<html><body><p>" + newText + "</p></body></html>");
        });

        // Add both elements to the SplitPane
        splitPane.getItems().addAll(new StackPane(textEditor), new StackPane(preview));

        // Create scene and set stage
        Scene scene = new Scene(splitPane);
        stage.setTitle("JavaFX Text Editor with Live Preview");
        stage.setScene(scene);
        stage.fullScreenProperty();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
