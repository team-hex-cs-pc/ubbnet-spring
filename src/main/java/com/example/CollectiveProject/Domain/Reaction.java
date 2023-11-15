package com.example.CollectiveProject.Domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "REACTIONS_TABLE")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reactionId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private ReactionType type;

    public enum ReactionType {
        LIKE,
        DISLIKE
    }
}


