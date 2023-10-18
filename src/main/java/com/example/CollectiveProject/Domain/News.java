package com.example.CollectiveProject.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table
public class News {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String category;
    @Column
    private String author;
    @Column
    private int likes = 0;
    @Column
    private Date publicationDate;

    @ManyToOne(optional = true)
    @JoinColumn(name = "writer_id")
    @JsonBackReference
    private Writer writer;
}
