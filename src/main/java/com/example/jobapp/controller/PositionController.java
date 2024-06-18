package com.example.jobapp.controller;

import com.example.jobapp.dto.CreatePositionRequest;
import com.example.jobapp.dto.GetPositionResponse;
import com.example.jobapp.exception.UnauthorizedException;
import com.example.jobapp.service.ClientService;
import com.example.jobapp.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/position")
@Validated
public class PositionController {
    private final PositionService positionService;
    private final ClientService clientService;

    @Autowired
    public PositionController(PositionService positionService, ClientService clientService) {
        this.positionService = positionService;
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<URL> createPosition(
            @RequestHeader("API-Key") UUID apiKey,
            @Valid @RequestBody CreatePositionRequest request) {

        if (!clientService.authenticateClient(apiKey)) {
            throw new UnauthorizedException("Invalid API-Key!");
        }

        try {
            URL url = positionService.createPosition(apiKey, request);
            return new ResponseEntity<>(url, HttpStatus.CREATED);
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<URL>> searchPositions(
            @RequestHeader("API-Key") UUID apiKey,
            @Size(max = 50) @RequestParam(required = false) String keyword,
            @Size(max = 50) @RequestParam(required = false) String location) {

        if (!clientService.authenticateClient(apiKey)) {
            throw new UnauthorizedException("Invalid API-Key!");
        }

        List<URL> urls = positionService.searchPositions(keyword, location);

        if (urls.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(urls, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPositionResponse> getPositionById(@PathVariable Long id) {
        try {
            GetPositionResponse response = positionService.getPositionById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
