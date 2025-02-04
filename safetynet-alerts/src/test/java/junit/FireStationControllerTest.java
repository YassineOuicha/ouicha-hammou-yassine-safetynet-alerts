package junit;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.safetynet.SafetyNetAlertsApplication;
import com.safetynet.model.FireStation;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(classes = SafetyNetAlertsApplication.class)
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllFireStations() throws Exception {
        mockMvc.perform(get("/firestations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThan(0)));
    }

    @Test
    public void testGetPersonsByFireStation() throws Exception {
        mockMvc.perform(get("/firestation").param("stationNumer","3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isArray())
                .andExpect(jsonPath("$.adults").exists())
                .andExpect(jsonPath("$.children").exists());
    }

    @Test
    public void testGetPhoneNumbersByFireStation() throws Exception {
        mockMvc.perform(get("/phoneAlert").param("firestation", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetPersonsByAddress() throws Exception {
        mockMvc.perform(get("/fire").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").exists())
                .andExpect(jsonPath("$.residents").isArray());
    }

    @Test
    public void testGetResidentsByStations() throws Exception {
        mockMvc.perform(get("/flood/stations").param("stations", "1,3,4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }


    @Test
    @DirtiesContext
    public void testAddFireStation() throws Exception {
        FireStation newStation = new FireStation();
        newStation.setStation(5);
        newStation.setAddress("Colmar 68000");

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Colmar 68000"))
                .andExpect(jsonPath("$.station").value(5));
    }

    @Test
    @DirtiesContext
    public void testUpdateFireStation() throws Exception {
        FireStation updatedStation = new FireStation();
        updatedStation.setStation(3);
        updatedStation.setAddress("Colmar 68000");

        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStation)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DirtiesContext
    public void testDeleteFireStation() throws Exception {
        FireStation stationToDelete = new FireStation();
        stationToDelete.setStation(3);
        stationToDelete.setAddress("1509 Culver St");

        mockMvc.perform(delete("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stationToDelete)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

}
