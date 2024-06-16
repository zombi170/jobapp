package com.example.jobapp.controller;

import com.example.jobapp.dto.RegisterClientRequest;
import com.example.jobapp.model.Client;
import com.example.jobapp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/client")
@Validated
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<UUID> registerClient(@Valid @RequestBody RegisterClientRequest request) {
        Client registeredClient = clientService.registerClient(request);
        return ResponseEntity.ok(registeredClient.getApiKey());
    }
}
