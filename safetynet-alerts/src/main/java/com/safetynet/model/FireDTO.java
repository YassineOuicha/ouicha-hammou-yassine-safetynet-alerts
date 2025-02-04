package com.safetynet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data @Getter @Setter
public class FireDTO {

    private int stationNumber;
    private List<FireStationDTO> residents;

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
