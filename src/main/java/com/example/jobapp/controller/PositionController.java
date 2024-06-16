package com.example.jobapp.controller;

import com.example.jobapp.dto.CreatePositionRequest;
import com.example.jobapp.dto.GetPositionResponse;
import com.example.jobapp.service.ClientService;
import com.example.jobapp.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/position")
@Validated
public class PositionController {

    @Autowired
    private PositionService positionService;

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<URL> createPosition(
            @RequestHeader("API-Key") UUID apiKey,
            @Valid @RequestBody CreatePositionRequest request){

        if (clientService.authenticateClient(apiKey)) {
            try {
                URL url = positionService.createPosition(apiKey, request);
                return ResponseEntity.ok(url);
            } catch (MalformedURLException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPositionResponse> getPositionById(@PathVariable Long id) {
        try {
            GetPositionResponse response = positionService.getPositionById(id);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
