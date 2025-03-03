package com.safetynet.model;

import lombok.Data;

/**
 * Represents a fire station with its associated address and station number.
 */
@Data
public class FireStation {
    private String address;
    private int station;

    // Setters and getters : the lombok annotations doesn't work correctly despite the dependency injection
    public String getAddress() {
        return address;
    }

    public int getStation() {
        return station;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStation(int station) {
        this.station = station;
    }
}
