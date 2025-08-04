package com.dev.service;

import javafx.collections.FXCollections;
import javafx.stage.FileChooser;
import com.dev.io.reader.ExcelReader;
import com.dev.io.reader.TXTReader;
import com.dev.model.Parcel;

import java.io.File;
import java.util.List;

public class ParcelFileLoaderService {

    public static void uploadFile(ParcelService parcelService) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(null);
        List<Parcel> inputList = null;
        if (file != null) {
            if (file.getName().endsWith(".txt")) {
                TXTReader txtReader = new TXTReader();
                inputList = txtReader.read(file.getPath());
            } else if (file.getName().endsWith(".xlsx")) {
                ExcelReader excelReader = new ExcelReader();
                inputList = excelReader.read(file.getPath());
            }

            if (inputList != null) {
                parcelService.addParcelsToInput(FXCollections.observableList(inputList));
            }
        }
    }
}
