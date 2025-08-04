package com.dev.io.reader;

import com.dev.model.Parcel;

import java.util.List;

public interface DataReader {
    List<Parcel> read(String filePath);
}
