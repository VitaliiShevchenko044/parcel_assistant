package com.dev.io.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dev.model.Parcel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader implements DataReader {

    private static final Logger log = LoggerFactory.getLogger(ExcelReader.class);

    @Override
    public List<Parcel> read(String filePath) {
        List<Parcel> inputList = new ArrayList<>();

        try (InputStream inStream = Files.newInputStream(Path.of(filePath));
             Workbook workbook = new XSSFWorkbook(inStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                String trackingNumber = getCellAsString(row, 0).trim();
                if (trackingNumber.isEmpty())
                    continue;
                String gibitNumber = getCellAsString(row, 1).trim();
                if (gibitNumber.length() != 5)
                    continue;
                String tourNumber = getCellAsString(row, 2).trim();
                if (tourNumber.length() != 3)
                    continue;

                Parcel parcel = new Parcel(trackingNumber, gibitNumber, tourNumber);
                inputList.add(parcel);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return inputList;
    }

    private String getCellAsString(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }
}
