package com.example.jobapp.controller;

import com.example.jobapp.dto.RegisterClientRequest;
import com.example.jobapp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/client")
@Validated
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<UUID> registerClient(@Valid @RequestBody RegisterClientRequest request) {
        UUID apiKey = clientService.registerClient(request);
        return new ResponseEntity<>(apiKey, HttpStatus.CREATED);
    }
}
