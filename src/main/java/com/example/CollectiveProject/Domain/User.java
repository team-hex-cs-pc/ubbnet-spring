package com.example.CollectiveProject.Domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class User {
    @Id
    @Column
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
}
