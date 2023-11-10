package com.example.CollectiveProject.DTO;

import lombok.Getter;

@Getter
public class RegisterCredentialsDTO {
    private String name;
    private String email;
    private String gender;
    private int age;
    private String username;
    private String password;

    public RegisterCredentialsDTO(String name, String email, String gender, int age, String username, String password) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.username = username;
        this.password = password;
    }
}
