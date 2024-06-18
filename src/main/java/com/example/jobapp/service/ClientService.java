package com.example.jobapp.service;

import com.example.jobapp.dto.RegisterClientRequest;
import com.example.jobapp.exception.ValidationException;
import com.example.jobapp.model.Client;
import com.example.jobapp.repository.ClientRepository;
import com.example.jobapp.util.HashUtil;
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
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("email", "Email is already in use!");
        }

        Client client = new Client();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        UUID apiKey = UUID.randomUUID();
        client.setApiKey(HashUtil.hashApiKey(apiKey));
        clientRepository.save(client);
        return apiKey;
    }

    public boolean authenticateClient(UUID apiKey) {
        String hashedApiKey = HashUtil.hashApiKey(apiKey);
        return clientRepository.findByApiKey(hashedApiKey).isPresent();
    }
}
