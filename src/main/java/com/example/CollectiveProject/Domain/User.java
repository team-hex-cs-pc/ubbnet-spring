package com.example.CollectiveProject.Domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "\"user\"")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH)
    @JsonManagedReference
    private List<Post> posts;
}
