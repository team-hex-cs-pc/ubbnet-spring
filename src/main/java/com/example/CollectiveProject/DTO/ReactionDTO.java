package com.example.CollectiveProject.DTO;

import com.example.CollectiveProject.Domain.Reaction;
import com.example.CollectiveProject.Exceptions.InvalidReactionTypeException;
import lombok.Data;

@Data
public class ReactionDTO {
    private String postReference;
    private Reaction.ReactionType type;
    private Integer userId;

    public static ReactionDTO fromEntity(Reaction reaction) {
        ReactionDTO dto = new ReactionDTO();
        dto.setPostReference(reaction.getPost().getPostReference());
        dto.setType(reaction.getType());
        dto.setUserId(reaction.getUser().getUserId());

        if (!isValidReactionType(dto.getType())) {
            throw new InvalidReactionTypeException("Invalid ReactionType: " + dto.getType());
        }

        return dto;
    }

    private static boolean isValidReactionType(Reaction.ReactionType type) {
        return type == Reaction.ReactionType.LIKE || type == Reaction.ReactionType.DISLIKE;
    }
}

