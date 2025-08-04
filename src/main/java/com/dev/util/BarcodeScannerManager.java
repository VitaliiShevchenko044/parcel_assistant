package com.dev.util;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.function.Consumer;

public class BarcodeScannerManager {

    private static final BarcodeScannerManager INSTANCE = new BarcodeScannerManager();
    private final StringBuilder builder = new StringBuilder();
    private Consumer<String> handler;
    private final EventHandler<KeyEvent> keyEventHandler;

    private BarcodeScannerManager() {
        keyEventHandler = event -> {
            if (event.getTarget() instanceof TextInputControl) return;

            if (event.getCode() == KeyCode.ENTER) {
                String scannedCode = builder.toString().trim().toLowerCase();
                if (!scannedCode.isEmpty() && handler != null) {
                    handler.accept(scannedCode);
                    builder.setLength(0);
                }
            } else
                builder.append(event.getText());
        };
    }

    public static BarcodeScannerManager getInstance() {
        return INSTANCE;
    }

    public void register(Scene scene, Consumer<String> handler) {
        this.handler = handler;
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
    }
}
