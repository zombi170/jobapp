package com.example.jobapp.service;

import com.example.jobapp.dto.RegisterClientRequest;
import com.example.jobapp.exception.ValidationException;
import com.example.jobapp.repository.ClientRepository;
import com.example.jobapp.util.HashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ClientServiceTests {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    void testRegisterClientSuccess() {
        RegisterClientRequest request = new RegisterClientRequest();
        request.setName("Teszt Elek");
        request.setEmail("teszt@elek.hu");

        UUID uuid = clientService.registerClient(request);

        assertNotNull(uuid);
        clientRepository.findByApiKey(HashUtil.hashApiKey(uuid)).ifPresent(client -> {
            assertEquals("Teszt Elek", client.getName());
            assertEquals("teszt@elek.hu", client.getEmail());
        });
    }

    @Test
    void testRegisterClientSameEmailFailure() {
        RegisterClientRequest request = new RegisterClientRequest();
        request.setName("Teszt Elek");
        request.setEmail("teszt@elek.hu");
        clientService.registerClient(request);

        assertThrows(ValidationException.class, () -> clientService.registerClient(request), "Email is already in use!");
    }

    @Test
    void testAuthenticateClientSuccess() {
        RegisterClientRequest request = new RegisterClientRequest();
        request.setName("Teszt Elek");
        request.setEmail("teszt@elek.hu");
        UUID uuid = clientService.registerClient(request);

        assertTrue(clientService.authenticateClient(uuid));
    }

    @Test
    void testAuthenticateClientFailure() {
        assertFalse(clientService.authenticateClient(UUID.randomUUID()));
    }
}
