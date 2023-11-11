package com.example.CollectiveProject.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "POSTS_TABLE")
public class Post {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    @Column(unique = true)
    private String postReference;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String category;
    @Column
    private int likes = 0;
    @Column
    private Date publicationDate;

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
