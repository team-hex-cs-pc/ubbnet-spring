package com.example.CollectiveProject.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "FRIEND_REQUESTS_TABLE")

public class FriendRequest {

    @Id
    @Column (name = "friend_request_id")
    private Long id;

    @Column (name = "sender_id")
    private Long senderId;

    @Column (name = "receiver_id")
    private Long receiverId;
}
