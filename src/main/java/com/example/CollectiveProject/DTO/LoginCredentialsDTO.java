package com.example.CollectiveProject.DTO;


import lombok.Data;

@Data
public class LoginCredentialsDTO {
    private String email;
    private String password;

    public LoginCredentialsDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
