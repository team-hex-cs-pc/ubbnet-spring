package com.example.CollectiveProject.Mapper;
import com.example.CollectiveProject.DTO.ReactionDTO;
import com.example.CollectiveProject.Domain.Reaction;

public class ReactionMapper {
    public static ReactionDTO toDTO(Reaction reaction) {
        return ReactionDTO.fromEntity(reaction);
    }
}
