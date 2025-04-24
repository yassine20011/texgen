package com.texgen.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
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

import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;
import com.texgen.PdfViewer;


public class EditorController implements Initializable {
    private static final Logger logger = Logger.getLogger(EditorController.class.getName());
    private File pdfOutputFile;

    // Define LaTeX syntax patterns
    private static final String COMMAND_PATTERN = "\\\\[a-zA-Z]+";
    private static final String BRACKET_PATTERN = "[{}]";
    private static final String MATH_PATTERN = "\\$[^$]+\\$";  // Inline math ($...$)
    private static final String COMMENT_PATTERN = "%.*";

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
    private final String initialCode = """
            \\documentclass{article}
            \\usepackage[utf8]{inputenc}
            \\usepackage{amsmath}
            
            \\title{My First \\LaTeX{} Document}
            \\author{L7o9}
            \\date{\\today}
            
            \\begin{document}
            
            \\maketitle
            
            \\section{Introduction}
            
            This is a simple introduction to \\LaTeX{}. It's used for creating high-quality documents, especially in academia.
            
            \\section{Text Formatting}
            
            You can make text \\textbf{bold}, \\textit{italic}, or \\underline{underlined}. You can also combine them: \\textbf{\\textit{like this}}.
            
            \\section{Math Mode}
            
            Here’s an inline math expression: \\( a^2 + b^2 = c^2 \\). \s
            And here’s a displayed equation:
            
            \\[
            E = mc^2
            \\]
            
            \\section{Lists}
            
            \\subsection*{Unordered List}
            \\begin{itemize}
                \\item First item
                \\item Second item
                \\item Third item
            \\end{itemize}
            
            \\subsection*{Ordered List}
            
            \\begin{enumerate}
                \\item Step one
                \\item Step two
                \\item Step three
            \\end{enumerate}

           \\section{A Table}

            \\begin{tabular}{|c|c|}
            \\hline
            Fruit & Color \\\\
            \\hline
            Apple & Red \\\\
            Banana & Yellow \\\\
            Grapes & Purple \\\\
            \\hline
            \\end{tabular}
                           
            \\section{Conclusion}
            
            Now you know the basics of \\LaTeX{}! You can write documents, solve equations, and structure everything beautifully.
            
            \\end{document}
            """;

    public VBox pdfContainer;

    @FXML
    private WebView pdfPreview;

    @FXML
    private CodeArea codeArea;

    @FXML private VBox rootVBox;

    @FXML
    private SplitPane splitPane;



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

    @FXML
    private void insertImage(ActionEvent event) {
        int cursorPosition = codeArea.getCaretPosition();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            String latexImageSyntax = "\\includegraphics{" + imagePath + "}";
            codeArea.replaceText(cursorPosition, cursorPosition, latexImageSyntax);
        }
    }

    @FXML
    public void wrapWithImage(){
        wrapSelectedText("\\textit{", "}");
    }


    @FXML
    public void wrapWithLink(ActionEvent event) {
        wrapSelectedText("\\textit{", "}");
    }

    @FXML void compileDocument(){
      PdfViewer pdfViewer = new PdfViewer(pdfPreview);
        pdfViewer.setOnWebEngineReadyCallback(() -> {
            String latexFileName = "document.tex";
            Path tempDir;
            try {
                tempDir = Files.createTempDirectory("texgen");
                pdfOutputFile = tempDir.resolve("document.pdf").toFile();
                String texFilePath = tempDir.resolve(latexFileName).toString();
                Files.writeString(Path.of(texFilePath), codeArea.getText(), StandardOpenOption.CREATE);
                String command = String.format("pdflatex -output-directory=%s %s", tempDir.toString(), texFilePath);
                Process process = Runtime.getRuntime().exec(command);
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    pdfViewer.loadPdfFile(pdfOutputFile);
                } else {
                    logger.severe("PDF compilation failed with exit code: " + exitCode);
                }
            } catch (IOException | InterruptedException e) {
                logger.severe("Error generating PDF: " + e.getMessage());
            }
        });
    }


    private void wrapSelectedText(String prefix, String suffix) {
        String selectedText = codeArea.getSelectedText();
        if (!selectedText.isEmpty()) {
            int start = codeArea.getSelection().getStart();
            int end = codeArea.getSelection().getEnd();
            codeArea.replaceText(start, end, prefix + selectedText + suffix);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PdfViewer pdfViewer = new PdfViewer(pdfPreview);
        VBox.setVgrow(splitPane, javafx.scene.layout.Priority.ALWAYS);
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.requestFocus();
        codeArea.replaceText(0, 0, initialCode);
        codeArea.setStyleSpans(0, computeHighlighting(initialCode));

        pdfViewer.setOnWebEngineReadyCallback(() -> {
                String latexFileName = "document.tex";
                Path tempDir;
                try {
                    tempDir = Files.createTempDirectory("texgen");
                    pdfOutputFile = tempDir.resolve("document.pdf").toFile();
                    String texFilePath = tempDir.resolve(latexFileName).toString();
                    Files.writeString(Path.of(texFilePath), initialCode, StandardOpenOption.CREATE);
                    String command = String.format("pdflatex -output-directory=%s %s", tempDir.toString(), texFilePath);
                    Process process = Runtime.getRuntime().exec(command);
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        pdfViewer.loadPdfFile(pdfOutputFile);
                    } else {
                        logger.severe("PDF compilation failed with exit code: " + exitCode);
                    }
                } catch (IOException | InterruptedException e) {
                    logger.severe("Error generating PDF: " + e.getMessage());
                }
        });

        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null) {
                codeArea.setStyleSpans(0, computeHighlighting(newText));
            }
            codeArea.setStyleSpans(0, computeHighlighting(newText));
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