package com.example.CollectiveProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserWithoutCredentialsDTO {
    private String name;
    private String gender;
    private int age;
    private String username;
}
