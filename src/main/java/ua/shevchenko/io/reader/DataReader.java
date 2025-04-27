package ua.shevchenko.io.reader;

import ua.shevchenko.model.Parcel;

import java.util.List;

public interface DataReader {
    List<Parcel> read(String filePath);
}
