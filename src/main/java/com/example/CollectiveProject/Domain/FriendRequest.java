package com.example.CollectiveProject.Domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name = "FRIEND_REQUESTS_TABLE")

public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "friend_request_id")
    private Long id;

    @Column (name = "sender_id")
    private Integer senderId;

    @Column (name = "receiver_id")
    private Integer receiverId;

    public FriendRequest(Integer senderId, Integer receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public FriendRequest() {

    }
}
