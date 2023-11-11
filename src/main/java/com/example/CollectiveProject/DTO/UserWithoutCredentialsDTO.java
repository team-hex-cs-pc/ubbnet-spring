package com.example.CollectiveProject.DTO;

import com.example.CollectiveProject.Domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserWithoutCredentialsDTO {
    private String name;
    private String gender;
    private int age;
    private String username;
    private List<Post> posts;
}
