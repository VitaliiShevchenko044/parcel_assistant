package ua.shevchenko.io.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.shevchenko.model.Parcel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TXTReader implements DataReader {

    private static final Logger log = LoggerFactory.getLogger(TXTReader.class);

    @Override
    public List<Parcel> read(String filePath) {
        List<Parcel> inputList = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));

            for (String line : lines) {
                String[] parts = line.split("\\s+");

                if (parts.length != 3)
                    continue;
                String trackingNumber = parts[0];
                String gibitNumber = parts[1];
                if (gibitNumber.length() != 5)
                    continue;
                String tourNumber = parts[2];
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
}
