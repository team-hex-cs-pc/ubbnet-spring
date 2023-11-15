package com.example.CollectiveProject.Domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "FRIEND_RELATIONS_TABLE")
public class FriendRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Integer user1Id;

    @Column
    private Integer user2Id;

    public FriendRelation(Integer user1Id, Integer user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public FriendRelation() {

    }
}
