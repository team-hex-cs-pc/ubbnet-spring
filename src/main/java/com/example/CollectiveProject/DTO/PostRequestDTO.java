package com.example.CollectiveProject.DTO;

import lombok.Data;

@Data
public class PostRequestDTO {
    private String postReference;
    private String title;
    private String content;
    private String category;
    private int likes;
    private String publicationDate;
    private String username;
}
