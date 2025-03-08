package com.texgen.main;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        TextArea editor = new TextArea("Write your Markdown + LaTeX here...");
        VBox root = new VBox(editor);
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("TexGen - Markdown & LaTeX Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}