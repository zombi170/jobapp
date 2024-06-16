package com.example.jobapp.service;

import com.example.jobapp.dto.RegisterClientRequest;
import com.example.jobapp.model.Client;
import com.example.jobapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public UUID registerClient(RegisterClientRequest request) {
        Client client = new Client();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setApiKey(UUID.randomUUID());
        return clientRepository.save(client).getApiKey();
    }

    public boolean authenticateClient(UUID apiKey) {
        return clientRepository.findByApiKey(apiKey).isPresent();
    }
}
