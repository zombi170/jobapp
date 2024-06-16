package com.example.jobapp;

import com.example.jobapp.model.Client;
import com.example.jobapp.model.Position;
import com.example.jobapp.repository.ClientRepository;
import com.example.jobapp.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataLoader implements ApplicationRunner {
    private final ClientRepository clientRepository;
    private final PositionRepository positionRepository;

    @Autowired
    public DataLoader(ClientRepository clientRepository, PositionRepository positionRepository) {
        this.clientRepository = clientRepository;
        this.positionRepository = positionRepository;
    }

    public void run(ApplicationArguments args) {
        Client client1 = new Client();
        client1.setName("Teszt Elek");
        client1.setEmail("teszt@elek.hu");
        client1.setApiKey(UUID.randomUUID());
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("Gipsz Jakab");
        client2.setEmail("gipsz@jakab.com");
        client2.setApiKey(UUID.randomUUID());
        clientRepository.save(client2);

        Position position1 = new Position();
        position1.setTitle("Java Fullstack Developer");
        position1.setLocation("Budapest");
        position1.setClient(client1);
        positionRepository.save(position1);

        Position position2 = new Position();
        position2.setTitle("Java Backend Developer");
        position2.setLocation("Budapest");
        position2.setClient(client1);
        positionRepository.save(position2);

        Position position3 = new Position();
        position3.setTitle("Python Fullstack Developer");
        position3.setLocation("Pécs");
        position3.setClient(client2);
        positionRepository.save(position3);

        Position position4 = new Position();
        position4.setTitle("Python Backend Developer");
        position4.setLocation("Pécs");
        position4.setClient(client2);
        positionRepository.save(position4);
    }
}
