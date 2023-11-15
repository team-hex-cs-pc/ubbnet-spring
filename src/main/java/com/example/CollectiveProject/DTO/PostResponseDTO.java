package com.example.CollectiveProject.DTO;

import com.example.CollectiveProject.Utilities.CalendarUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private String postReference;
    private String title;
    private String content;
    private String category;
    private int likes;
    private String publicationDate;
    private String username;

    public PostResponseDTO(String postReference, String title, String content, String category, int likes, Date publicationDate, String username) {
        this.postReference = postReference;
        this.title = title;
        this.content = content;
        this.category = category;
        this.likes = likes;
        this.publicationDate = new CalendarUtils().convertDateToString(publicationDate);
        this.username = username;
    }
}
