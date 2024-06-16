package com.example.jobapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreatePositionRequest {
    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 50)
    private String location;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
