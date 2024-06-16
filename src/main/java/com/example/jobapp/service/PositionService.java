package com.example.jobapp.service;

import com.example.jobapp.dto.CreatePositionRequest;
import com.example.jobapp.dto.GetPositionResponse;
import com.example.jobapp.model.Position;
import com.example.jobapp.repository.ClientRepository;
import com.example.jobapp.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ClientRepository clientRepository;

    public URL createPosition(UUID apiKey, CreatePositionRequest request) throws MalformedURLException {
        Position position = new Position();
        position.setName(request.getName());
        position.setLocation(request.getLocation());
        clientRepository.findByApiKey(apiKey).ifPresent(position::setClient);
        positionRepository.save(position);
        return new URL("http://localhost:8080/position/" + position.getId());
    }

    public GetPositionResponse getPositionById(Long id) throws NoSuchElementException {
        Position position = positionRepository.findById(id).orElseThrow();
        GetPositionResponse response = new GetPositionResponse();
        response.setName(position.getName());
        response.setLocation(position.getLocation());
        return response;
    }
}
