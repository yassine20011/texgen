package com.texgen.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.control.SplitPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorController implements Initializable {

    @FXML
    private WebView pdfPreview;

    @FXML
    private CodeArea codeArea;

    @FXML private VBox rootVBox;

    @FXML
    private SplitPane splitPane;

    // Define LaTeX syntax patterns
    private static final String COMMAND_PATTERN = "\\\\[a-zA-Z]+";
    private static final String BRACKET_PATTERN = "[{}]";
    private static final String MATH_PATTERN = "\\$[^$]+\\$";  // Inline math ($...$)
    private static final String COMMENT_PATTERN = "%.*";

    @FXML
    private void wrapWithBold() {
        wrapSelectedText("\\textbf{", "}");
    }

    @FXML
    private void wrapWithItalic() {
        wrapSelectedText("\\textit{", "}");
    }

    @FXML
    private void wrapWithEquation() {
        wrapSelectedText("$", "$");
    }

    private void wrapSelectedText(String prefix, String suffix) {
        String selectedText = codeArea.getSelectedText();
        if (!selectedText.isEmpty()) {
            int start = codeArea.getSelection().getStart();
            int end = codeArea.getSelection().getEnd();
            codeArea.replaceText(start, end, prefix + selectedText + suffix);
        }
    }


    private static final Pattern PATTERN = Pattern.compile(
            "(?<COMMAND>" + COMMAND_PATTERN
            + ")"
            + "|(?<BRACKET>"
            + BRACKET_PATTERN + ")"
            + "|(?<MATH>"
            + MATH_PATTERN
            + ")"
            + "|(?<COMMENT>"
            + COMMENT_PATTERN
            + ")"
    );


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox.setVgrow(splitPane, javafx.scene.layout.Priority.ALWAYS);
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.requestFocus();

        String initialCode = """
                % Example LaTeX Document
                \\documentclass{article}
                \\begin{document}
                This is an example of LaTeX syntax highlighting.
                
                Here is a math equation: $E = mc^2$
                
                \\section{Introduction}
                \\textbf{Bold text} and \\textit{italic text}.
                
                \\end{document}
                """;


        codeArea.replaceText(0, 0, initialCode);


        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null) {
                codeArea.setStyleSpans(0, computeHighlighting(newText));
            }
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });


        WebEngine webEngine = pdfPreview.getEngine();
        webEngine.loadContent("<html><body><pre>" + codeArea.getText() + "</pre></body></html>");

        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            webEngine.loadContent("<html><body><pre>" + newText + "</pre></body></html>");
        });
    }

    // Syntax highlighting logic
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            String styleClass =
                    matcher.group("COMMAND") != null ? "command" :
                            matcher.group("BRACKET") != null ? "bracket" :
                                    matcher.group("MATH") != null ? "math" :
                                            matcher.group("COMMENT") != null ? "comment" :
                                                    null;

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastEnd);
        return spansBuilder.create();
    }
}
