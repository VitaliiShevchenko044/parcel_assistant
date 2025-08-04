package com.dev.model;

import java.util.Objects;

public class Parcel {
    private final String trackingNumber;
    private final String gibitNumber;
    private String tourNumber;

    public Parcel(String trackingNumber, String gibitNumber, String tourNumber) {
        this.trackingNumber = trackingNumber;
        this.gibitNumber = gibitNumber;
        this.tourNumber = tourNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getGibitNumber() {
        return gibitNumber;
    }

    public String getTourNumber() {
        return tourNumber;
    }

    public void setTourNumber(String tourNumber) {
        this.tourNumber = tourNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return Objects.equals(trackingNumber, parcel.trackingNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(trackingNumber);
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "trackingNumber='" + trackingNumber + '\'' +
                ", gibitNumber='" + gibitNumber + '\'' +
                ", tourNumber='" + tourNumber + '\'' +
                '}';
    }
}

