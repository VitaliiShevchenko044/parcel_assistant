package com.dev.io.writer;

import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dev.model.Parcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TXTWriter {

    private static final Logger log = LoggerFactory.getLogger(TXTWriter.class);

    public static void saveParcelsToTXT(List<Parcel> scannedList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        fileChooser.setInitialFileName("scanned_parcels.txt");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Tracking Number | Gibit Number | Tour Number\n");
                for (Parcel parcel : scannedList) {
                    writer.write(parcel.getTrackingNumber() + "  |  " + parcel.getGibitNumber() + "  |  " +
                            parcel.getTourNumber() + "\n");
                }
            } catch (IOException e) {
                log.error("Error saving file {}", e.getMessage());
            }
        }
    }
}
