package com.example.CollectiveProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String birthdate;
    private String username;
    private List<PostResponseDTO> posts;
}
