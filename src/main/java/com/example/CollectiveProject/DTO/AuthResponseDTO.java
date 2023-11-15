package com.example.CollectiveProject.DTO;

import lombok.Getter;

@Getter
public class AuthResponseDTO {
    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }
}
