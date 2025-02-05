package junit;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.safetynet.SafetyNetAlertsApplication;
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

@AutoConfigureMockMvc
@SpringBootTest(classes = SafetyNetAlertsApplication.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllPersons() throws Exception {
        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThan(0)));
    }

    @Test
    public void testGetPerson() throws Exception {
        mockMvc.perform(get("/person")
                .param("firstName", "John")
                .param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Boyd"));
    }

    @Test
    public void testGetChildAlert() throws Exception {
        mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetPersonInfo() throws Exception {
        mockMvc.perform(get("/personInfo").param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetCommunityEmails() throws Exception {
        mockMvc.perform(get("/communityEmail").param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DirtiesContext
    public void testAddPerson() throws Exception {
        Person newPerson = new Person();
        newPerson.setFirstName("Yassine");
        newPerson.setLastName("Ouicha");
        newPerson.setAddress("Colmar 68000");
        newPerson.setCity("Colmar");
        newPerson.setPhone("0707070707");
        newPerson.setEmail("yassine.ouicha.nc2@gmail.com");
        newPerson.setZip("68000");

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Yassine"))
                .andExpect(jsonPath("$.lastName").value("Ouicha"));
    }

    @Test
    @DirtiesContext
    public void testUpdatePerson() throws Exception {
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Tenley");
        updatedPerson.setLastName("Boyd");
        updatedPerson.setAddress("Colmar 68000");
        updatedPerson.setCity("Colmar");
        updatedPerson.setPhone("0707070707");
        updatedPerson.setEmail("yassine.ouicha.nc2@gmail.com");
        updatedPerson.setZip("68000");

        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DirtiesContext
    public void testDeletePerson() throws Exception {
        mockMvc.perform(delete("/person")
                .param("firstName", "John")
                .param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

}
