package com.example.jobapp.controller;

import com.example.jobapp.model.Client;
import com.example.jobapp.repository.ClientRepository;
import com.example.jobapp.repository.PositionRepository;
import com.example.jobapp.util.HashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PositionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ClientRepository clientRepository;

    private UUID apiKey;

    @BeforeEach
    public void setup() {
        positionRepository.deleteAll();
        clientRepository.deleteAll();
        Client client = new Client();
        client.setName("Teszt Elek");
        client.setEmail("teszt@elek.hu");
        apiKey = UUID.randomUUID();
        client.setApiKey(HashUtil.hashApiKey(apiKey));
        clientRepository.save(client);
    }

    @Test
    void testCreatePositionSuccess() throws Exception {
        String positionJson = "{\"title\": \"Java Developer\", \"location\": \"Budapest\"}";

        mockMvc.perform(post("/position")
                        .header("API-Key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(positionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testCreatePositionUnauthorizedFailure() throws Exception {
        String positionJson = "{\"title\": \"Java Developer\", \"location\": \"Budapest\"}";

        mockMvc.perform(post("/position")
                        .header("API-Key", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(positionJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("Invalid API-Key!"));
    }

    @Test
    void testCreatePositionInvalidParamsFailure() throws Exception {
        String positionJson = "{}";

        mockMvc.perform(post("/position")
                        .header("API-Key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(positionJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("must not be blank"))
                .andExpect(jsonPath("$.title").value("must not be blank"));

        positionJson = "{\"title\": \"    \", \"location\": \"    \"}";

        mockMvc.perform(post("/position")
                        .header("API-Key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(positionJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("must not be blank"))
                .andExpect(jsonPath("$.title").value("must not be blank"));

        positionJson = "{\"title\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry.\", " +
                "\"location\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry.\"}";

        mockMvc.perform(post("/position")
                        .header("API-Key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(positionJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("size must be between 0 and 50"))
                .andExpect(jsonPath("$.title").value("size must be between 0 and 50"));
    }

    @Test
    void testSearchPositionsSuccess() throws Exception {
        String positionJson1 = "{\"title\": \"Java Developer\", \"location\": \"Budapest\"}";
        String positionJson2 = "{\"title\": \"Python Developer\", \"location\": \"Pest\"}";

        mockMvc.perform(post("/position")
                .header("API-Key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(positionJson1));

        mockMvc.perform(post("/position")
                .header("API-Key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(positionJson2));

        mockMvc.perform(get("/position/search")
                        .header("API-Key", apiKey)
                        .param("keyword", "dev")
                        .param("location", "pest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testSearchPositionsNotFoundFailure() throws Exception {
        mockMvc.perform(get("/position/search")
                        .header("API-Key", apiKey)
                        .param("keyword", "dev")
                        .param("location", "pest"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchPositionsUnauthorizedFailure() throws Exception {
        mockMvc.perform(get("/position/search")
                        .header("API-Key", UUID.randomUUID())
                        .param("keyword", "dev")
                        .param("location", "pest"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("Invalid API-Key!"));
    }

    @Test
    void testSearchPositionsInvalidParamsFailure() throws Exception {
        mockMvc.perform(get("/position/search")
                        .header("API-Key", apiKey)
                        .param("keyword", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                        .param("location", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.keyword").value("size must be between 0 and 50"))
                .andExpect(jsonPath("$.location").value("size must be between 0 and 50"));
    }

    @Test
    void testGetPositionSuccess() throws Exception {
        String positionJson = "{\"title\": \"Java Developer\", \"location\": \"Budapest\"}";

        ResultActions response = mockMvc.perform(post("/position")
                .header("API-Key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(positionJson));

        String url = response.andReturn().getResponse().getContentAsString();
        url = url.substring(url.lastIndexOf("0") + 1, url.length() - 1);


        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Developer"))
                .andExpect(jsonPath("$.location").value("Budapest"));
    }

    @Test
    void testGetPositionNotFoundFailure() throws Exception {
        mockMvc.perform(get("/position/1"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/position/valami"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value("The given value is not class java.lang.Long type."));
    }
}
