package com.example.jobapp.controller;

import com.example.jobapp.dto.RegisterClientRequest;
import com.example.jobapp.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "Client Management")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @ApiOperation(value = "Register a new client", response = UUID.class, notes = "Returns the API key of the registered client", code = 201)
    public ResponseEntity<UUID> registerClient(@ApiParam(name = "client", value = "Client registration details")
                                                   @Valid @RequestBody RegisterClientRequest request) {

        UUID apiKey = clientService.registerClient(request);
        return new ResponseEntity<>(apiKey, HttpStatus.CREATED);
    }
}
