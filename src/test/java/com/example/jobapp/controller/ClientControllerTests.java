package com.example.jobapp.controller;

import com.example.jobapp.repository.ClientRepository;
import com.example.jobapp.model.Client;
import org.hamcrest.Matcher;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setup() {
        clientRepository.deleteAll();
    }

    @Test
    void testRegisterClientSuccess() throws Exception {
        String clientJson = "{\"name\": \"Teszt Elek\", \"email\": \"teszt@elek.hu\"}";

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testRegisterClientInvalidParamsFailure() throws Exception {
        String clientJson = "{}";

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("must not be blank"))
                .andExpect(jsonPath("$.name").value("must not be blank"));

        clientJson = "{\"name\": \"     \", \"email\": \"     \"}";

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value(anyOf(
                        List.of("must not be blank",
                        "must be a well-formed email address").stream()
                                .map(StringContains::containsString)
                                .toArray(Matcher[]::new))
                ))
                .andExpect(jsonPath("$.name").value("must not be blank"));

        clientJson = "{\"name\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard.\", " +
                "\"email\": \"invalid\"}";

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("must be a well-formed email address"));
    }

    @Test
    void testRegisterClientDuplicateEmailFailure() throws Exception {
        Client client = new Client();
        client.setName("Teszt Elek");
        client.setEmail("teszt@elek.hu");
        client.setApiKey("hashedApiKey");
        clientRepository.save(client);

        String clientJson = "{\"name\": \"Teszt Elek\", \"email\": \"teszt@elek.hu\"}";

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email is already in use!"));
    }
}
