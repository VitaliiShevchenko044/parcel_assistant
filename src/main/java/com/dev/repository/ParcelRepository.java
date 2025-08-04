package com.dev.repository;

import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dev.model.Parcel;

import java.util.Optional;

public class ParcelRepository {

    private static final Logger log = LoggerFactory.getLogger(ParcelRepository.class);

    private final ObservableList<Parcel> parcels;

    public ParcelRepository(ObservableList<Parcel> parcels) {
        this.parcels = parcels;
    }

    public Optional<Parcel> findParcel(String trackingNumber) {
        return parcels.stream()
                .filter(parcel -> parcel.getTrackingNumber().equalsIgnoreCase(trackingNumber))
                .findFirst();
    }

    public void add(Parcel parcel) {
        if (parcels.contains(parcel))
            log.warn("Duplicate - {}", parcel.getTrackingNumber());
        else
            parcels.add(parcel);
    }

    public void remove(Parcel parcel) {
        parcels.remove(parcel);
    }

    public void clear() {
        parcels.clear();
    }

    public ObservableList<Parcel> getAll() {
        return parcels;
    }
}
