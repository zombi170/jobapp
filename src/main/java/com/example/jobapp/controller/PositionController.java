package com.example.jobapp.controller;

import com.example.jobapp.dto.CreatePositionRequest;
import com.example.jobapp.dto.GetPositionResponse;
import com.example.jobapp.exception.UnauthorizedException;
import com.example.jobapp.service.ClientService;
import com.example.jobapp.service.PositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "Position Management")
public class PositionController {
    private final PositionService positionService;
    private final ClientService clientService;

    @Autowired
    public PositionController(PositionService positionService, ClientService clientService) {
        this.positionService = positionService;
        this.clientService = clientService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new position", response = URL.class, notes = "Returns the URL of the created position", code = 201)
    public ResponseEntity<URL> createPosition(
            @ApiParam(value = "Client API-Key for authorization", defaultValue = "e0d2f6d8-a136-4b90-878b-fcdef228350a")
            @RequestHeader("API-Key")
            UUID apiKey,
            @ApiParam(name = "position", value = "Position details")
            @Valid @RequestBody
            CreatePositionRequest request) {

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
    @ApiOperation(value = "Search for positions", response = URL.class, responseContainer = "List",
            notes = "Returns a list of URLs of the found positions")
    public ResponseEntity<List<URL>> searchPositions(
            @ApiParam(value = "Client API-Key for authorization", defaultValue = "e0d2f6d8-a136-4b90-878b-fcdef228350a")
            @RequestHeader("API-Key")
            UUID apiKey,
            @ApiParam(value = "Searched job", defaultValue = "java dev") @Size(max = 50) @RequestParam(required = false)
            String keyword,
            @ApiParam(value = "Searched location", defaultValue = "budapest") @Size(max = 50) @RequestParam(required = false)
            String location) {

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
    @ApiOperation(value = "Get a position by ID", response = GetPositionResponse.class, notes = "Returns the position details")
    public ResponseEntity<GetPositionResponse> getPositionById(
            @ApiParam(value = "Position ID", defaultValue = "2") @PathVariable Long id) {

        try {
            GetPositionResponse response = positionService.getPositionById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
