package com.safetynet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data @Getter @Setter
public class FireDTO {

    private int stationNumber;
    private List<FireStationDTO> residents;

}
