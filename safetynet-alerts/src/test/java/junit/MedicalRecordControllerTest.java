package junit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safetynet.SafetyNetAlertsApplication;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest(classes = SafetyNetAlertsApplication.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetAllMedicalRecords() throws Exception {
        mockMvc.perform(get("/medicalrecords"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThan(0)));
    }

    @Test
    public void testGetMedicalRecord() throws Exception {
        mockMvc.perform(get("/medicalrecord")
                .param("firstName", "John")
                .param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Boyd"));
    }

    @Test
    @DirtiesContext
    public void testAddMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Yassine");
        medicalRecord.setLastName("Ouicha");
        medicalRecord.setBirthdate("06/02/1997");
        medicalRecord.setMedications(List.of("med1: 100mg", "med2: 200 mg"));
        medicalRecord.setAllergies(List.of("allergy1", "allergy2", "allergy3"));

        mockMvc.perform(post("/medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Yassine"))
                .andExpect(jsonPath("$.lastName").value("Ouicha"));
    }

    @Test
    @DirtiesContext
    public void testUpdateMedicalRecord() throws Exception {
        MedicalRecord updatedMedicalRecord = new MedicalRecord();
        updatedMedicalRecord.setFirstName("Yassine");
        updatedMedicalRecord.setLastName("Ouicha");
        updatedMedicalRecord.setBirthdate("06/02/1997");
        updatedMedicalRecord.setMedications(List.of("med2: 50mg", "med4: 150 mg"));
        updatedMedicalRecord.setAllergies(List.of("allergy2", "allergy5", "allergy6"));

        mockMvc.perform(put("/medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMedicalRecord)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DirtiesContext
    public void testDeleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/medicalrecord")
                .param("firstName", "John")
                .param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
