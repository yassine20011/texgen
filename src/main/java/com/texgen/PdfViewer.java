package com.texgen;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class PdfViewer {

    private final WebEngine webEngine;
    private boolean webEngineReady = false;
    private Runnable onWebEngineReadyCallback;

    public PdfViewer(WebView webView) {
        this.webEngine = webView.getEngine();
        initializeWebEngine();
    }

    public void setOnWebEngineReadyCallback(Runnable callback) {
        this.onWebEngineReadyCallback = callback;
    }

    private void initializeWebEngine() {
        webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                webEngineReady = true;
                if (onWebEngineReadyCallback != null) {
                    onWebEngineReadyCallback.run();
                }
            } else if (newState == Worker.State.FAILED) {
                System.err.println("WebEngine failed to load host page.");
                webEngine.getLoadWorker().getException().printStackTrace();
            }
        });

        URL hostHtmlUrl = getClass().getResource("/pdf_host.html");
        if (hostHtmlUrl == null) {
            throw new RuntimeException("Cannot find pdf_host.html in resources!");
        }
        webEngine.load(hostHtmlUrl.toExternalForm());
    }

    public void loadPdfFile(File pdfFile) {
        System.out.println("Loading PDF file: " + pdfFile.getAbsolutePath());
        System.out.println("WebEngine ready: " + webEngineReady);
        if (pdfFile != null && pdfFile.exists() && webEngineReady) {
            try {
                URI fileUri = pdfFile.toURI();
                String pdfUrl = fileUri.toString();
                System.out.println("PDF URL: " + pdfUrl);
                String escapedUrl = pdfUrl.replace("'", "\\'");
                String script = String.format("loadPdf('%s')", escapedUrl);
                webEngine.executeScript(script);
            } catch (Exception ex) {
                System.err.println("Error generating file URI or calling JavaScript: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (!webEngineReady) {
            System.err.println("WebEngine not ready when trying to load PDF.");
        } else {
            System.err.println("Invalid file provided for loading.");
        }
    }

    public boolean isWebEngineReady() {
        return webEngineReady;
    }
}
