package com.example.CollectiveProject.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "FRIEND_RELATIONS_TABLE")
public class FriendRelation {
    @Id
    private Long id;

    @Column
    private Long user1Id;

    @Column
    private Long user2Id;
}
