package com.example.CollectiveProject.Domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table
public class Writer {
    @Id
    @Column(name = "writer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer writerId;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String gender;
    @Column
    private int age;
    @Column
    private String username;
    @Column
    private String password;

    // One writer can publish multiple posts
    @OneToMany(mappedBy = "writer", cascade = CascadeType.DETACH)
    @JsonManagedReference
    private List<News> posts;
}
