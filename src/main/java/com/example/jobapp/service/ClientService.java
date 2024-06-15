package com.example.jobapp.service;

import com.example.jobapp.model.Client;
import com.example.jobapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client registerClient(String name, String email) {
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setApiKey(UUID.randomUUID());
        return clientRepository.save(client);
    }
}
