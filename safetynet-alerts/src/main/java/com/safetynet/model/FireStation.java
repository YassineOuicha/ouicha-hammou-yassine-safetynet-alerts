package com.safetynet.model;


import lombok.Data;


@Data
public class FireStation {

    private String address;
    private int station;

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
