package com.safetynet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
public class PersonFireStationDTO {

    private List<Person> persons;
    private int adults;
    private int children;


}
