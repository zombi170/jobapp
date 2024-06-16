package com.example.jobapp.service;

import com.example.jobapp.dto.CreatePositionRequest;
import com.example.jobapp.dto.GetPositionResponse;
import com.example.jobapp.model.Position;
import com.example.jobapp.repository.ClientRepository;
import com.example.jobapp.repository.PositionRepository;
import com.example.jobapp.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PositionService {
    private final PositionRepository positionRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository, ClientRepository clientRepository) {
        this.positionRepository = positionRepository;
        this.clientRepository = clientRepository;
    }

    public URL createPosition(UUID apiKey, CreatePositionRequest request) throws MalformedURLException {
        Position position = new Position();
        position.setTitle(request.getTitle());
        position.setLocation(request.getLocation());
        clientRepository.findByApiKey(HashUtil.hashApiKey(apiKey)).ifPresent(position::setClient);
        positionRepository.save(position);
        return new URL("http://localhost:8080/position/" + position.getId());
    }

    public List<URL> searchPositions(String keyword, String location) {
        if (keyword == null) {
            keyword = "";
        }

        if (location == null) {
            location = "";
        }

        List<Position> positions = positionRepository.searchPositions(keyword, location);
        return positions.stream()
                .map(position -> {
                    try {
                        return new URL("http://localhost:8080/position/" + position.getId());
                    } catch (MalformedURLException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public GetPositionResponse getPositionById(Long id) throws NoSuchElementException {
        Position position = positionRepository.findById(id).orElseThrow();
        GetPositionResponse response = new GetPositionResponse();
        response.setTitle(position.getTitle());
        response.setLocation(position.getLocation());
        return response;
    }
}
