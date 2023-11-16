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


    //TODO ADD REACTION DTOS and change post like/dislike into 1, with postref,userid,type in body instead of pathvariable
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

    @DeleteMapping("/{postReference}")
    private ResponseEntity<?> removeReactionsByPostReference(@PathVariable String postReference){
        try {
            reactionService.deleteReactionsByPostReference(postReference);
            return new ResponseEntity<>("Reactions removed successfully", HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @GetMapping("/{postReference}/post")
    private ResponseEntity<?> getReactionsByPostReference(@PathVariable String postReference){
        try {
            return new ResponseEntity<>(reactionService.getReactionsByPostReference(postReference), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{userId}/user")
    private ResponseEntity<?> getReactionsByUserId(@PathVariable Integer userId){
        try {
            return new ResponseEntity<>(reactionService.getReactionsByUserId(userId), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{postReference}/{userId}")
    private ResponseEntity<?> getReactionByPostReferenceAndUserId(@PathVariable String postReference, @PathVariable Integer userId){
        try {
            return new ResponseEntity<>(reactionService.getReactionByPostReferenceAndUserId(postReference, userId), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
