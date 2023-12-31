package com.example.CollectiveProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String birthdate;
    private String username;
    private String password;
}
