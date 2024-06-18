package com.example.jobapp.service;

import com.example.jobapp.dto.CreatePositionRequest;
import com.example.jobapp.dto.GetPositionResponse;
import com.example.jobapp.model.Client;
import com.example.jobapp.model.Position;
import com.example.jobapp.repository.ClientRepository;
import com.example.jobapp.repository.PositionRepository;
import com.example.jobapp.util.HashUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PositionServiceTests {
    @Autowired
    private PositionService positionService;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ClientRepository clientRepository;

    private UUID apiKey;

    @BeforeEach
    public void setUp() {
        Client client = new Client();
        client.setName("Teszt Elek");
        client.setEmail("teszt@elek.hu");
        apiKey = UUID.randomUUID();
        client.setApiKey(HashUtil.hashApiKey(apiKey));
        clientRepository.save(client);
    }

    @AfterEach
    public void cleanUp() {
        positionRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void testCreatePositionSuccess() {
        CreatePositionRequest request = new CreatePositionRequest();
        request.setTitle("Java Developer");
        request.setLocation("Budapest");

        assertDoesNotThrow(() -> assertNotNull(positionService.createPosition(apiKey, request)));
        positionRepository.findOne(Example.of(new Position())).ifPresent(position -> {
            assertEquals("Java Developer", position.getTitle());
            assertEquals("Budapest", position.getLocation());
        });
    }

    @Test
    void testSearchPositionByKeywordSuccess() {
        CreatePositionRequest request = new CreatePositionRequest();
        request.setTitle("Java Developer");
        request.setLocation("Budapest");

        try {
            positionService.createPosition(apiKey, request);
            request.setTitle("Backend Java Developer");
            positionService.createPosition(apiKey, request);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        assertEquals(2, positionService.searchPositions("java", null).size());
    }

    @Test
    void testSearchPositionByLocationSuccess() {
        CreatePositionRequest request = new CreatePositionRequest();
        request.setTitle("Java Developer");
        request.setLocation("Budapest");

        try {
            positionService.createPosition(apiKey, request);
            request.setLocation("Pest");
            positionService.createPosition(apiKey, request);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        assertEquals(2, positionService.searchPositions(null, "pest").size());
    }

    @Test
    void testSearchPositionSuccess() {
        CreatePositionRequest request = new CreatePositionRequest();
        request.setTitle("Java Developer");
        request.setLocation("Budapest");

        try {
            positionService.createPosition(apiKey, request);
            request.setTitle("Python Developer");
            request.setLocation("Pécs");
            positionService.createPosition(apiKey, request);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1, positionService.searchPositions("java", "pest").size());
    }

    @Test
    void testSearchPositionFailure() {
        CreatePositionRequest request = new CreatePositionRequest();
        request.setTitle("Java Developer");
        request.setLocation("Budapest");

        try {
            positionService.createPosition(apiKey, request);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        assertEquals(0, positionService.searchPositions("python", "pécs").size());
    }

    @Test
    void testGetPositionSuccess() {
        CreatePositionRequest request = new CreatePositionRequest();
        request.setTitle("Java Developer");
        request.setLocation("Budapest");

        try {
            URL url = positionService.createPosition(apiKey, request);
            long id = Integer.parseInt(url.toString().split("/")[4]);

            GetPositionResponse position = positionService.getPositionById(id);

            assertEquals("Java Developer", position.getTitle());
            assertEquals("Budapest", position.getLocation());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetPositionFailure() {
        assertThrows(NoSuchElementException.class, () -> positionService.getPositionById(1L));
    }
}
