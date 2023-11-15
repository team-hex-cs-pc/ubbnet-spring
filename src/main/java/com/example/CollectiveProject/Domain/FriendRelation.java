package com.example.CollectiveProject.Domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Table(name = "FRIEND_RELATIONS_TABLE")
public class FriendRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
