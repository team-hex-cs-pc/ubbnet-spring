package com.example.CollectiveProject.API;

import com.example.CollectiveProject.Domain.Reaction;
import com.example.CollectiveProject.Exceptions.NotFoundException;
import com.example.CollectiveProject.Service.ReactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reaction")
@AllArgsConstructor
public class ReactionController {

    @Autowired
    private final ReactionService reactionService;

    @PostMapping("/like/{postReference}/{userId}")
    public ResponseEntity<?> like(@PathVariable String postReference, @PathVariable Integer userId) {
        //TODO CHANGE WITH LOGGED-IN USER, INSTEAD OF USERID
        return handleReaction(postReference, userId, Reaction.ReactionType.LIKE);
    }

    @PostMapping("/dislike/{postReference}/{userId}")
    public ResponseEntity<?> dislike(@PathVariable String postReference, @PathVariable Integer userId) {
        //TODO CHANGE WITH LOGGED-IN USER, INSTEAD OF USERID
        return handleReaction(postReference, userId, Reaction.ReactionType.DISLIKE);
    }

    @DeleteMapping("/{postReference}/{userId}")
    public ResponseEntity<?> removeReaction(@PathVariable String postReference, @PathVariable Integer userId) {
        try {
            reactionService.removeReaction(postReference, userId);
            return new ResponseEntity<>("Reaction removed successfully", HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    private ResponseEntity<?> handleReaction(String postReference, Integer userId, Reaction.ReactionType type) {
        try {
            Reaction reaction = reactionService.addReaction(postReference, userId, type);
            return new ResponseEntity<>(reaction, HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }
}