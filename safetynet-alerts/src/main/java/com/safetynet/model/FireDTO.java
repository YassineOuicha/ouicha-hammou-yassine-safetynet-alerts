package com.safetynet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object representing residents covered by a specific fire station.
 * Contains the fire station number and a list of residents associated with that fire station.
 */
@Data @Getter @Setter
public class FireDTO {

    private int stationNumber;
    private List<FireStationDTO> residents;

    // Setters and getters : the lombok annotations doesn't work correctly despite the dependency injection
    public List<FireStationDTO> getResidents() {
        return residents;
    }

    public void setResidents(List<FireStationDTO> residents) {
        this.residents = residents;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(int stationNumber) {
        this.stationNumber = stationNumber;
    }
}
