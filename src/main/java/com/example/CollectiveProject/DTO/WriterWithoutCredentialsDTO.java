package com.example.CollectiveProject.DTO;

import com.example.CollectiveProject.Domain.News;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WriterWithoutCredentialsDTO {
    private String name;
    private String gender;
    private int age;
    private String username;
    private List<News> posts;
}
