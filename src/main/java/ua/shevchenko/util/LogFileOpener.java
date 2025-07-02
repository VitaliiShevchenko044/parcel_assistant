package ua.shevchenko.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LogFileOpener {

    private static final Logger log = LoggerFactory.getLogger(LogFileOpener.class);

    private static final File LOG_FILE = new File("logs/app.log");

    public static void openLogFile() {
        try {
            if (LOG_FILE.exists())
                Desktop.getDesktop().open(LOG_FILE);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
