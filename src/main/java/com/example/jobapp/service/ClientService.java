package com.example.jobapp.service;

import com.example.jobapp.dto.RegisterClientRequest;
import com.example.jobapp.model.Client;
import com.example.jobapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client registerClient(RegisterClientRequest request) {
        Client client = new Client();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setApiKey(UUID.randomUUID());
        return clientRepository.save(client);
    }

    public boolean authenticateClient(UUID apiKey) {
        return clientRepository.findByApiKey(apiKey).isPresent();
    }
}
