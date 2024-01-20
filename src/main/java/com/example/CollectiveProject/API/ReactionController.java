package com.example.CollectiveProject.API;

import com.example.CollectiveProject.DTO.ReactionDTO;
import com.example.CollectiveProject.Domain.Reaction;
import com.example.CollectiveProject.Exceptions.InvalidReactionTypeException;
import com.example.CollectiveProject.Exceptions.NotFoundException;
import com.example.CollectiveProject.Service.ReactionService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reaction")
@AllArgsConstructor
public class ReactionController {

    @Autowired
    private final ReactionService reactionService;


    @PostMapping
    public ResponseEntity<?> addReaction(@RequestBody ReactionDTO request) {
        try {
            reactionService.addReaction(request.getPostReference(), request.getUserName(), request.getType());
            return new ResponseEntity<>("Reaction added successfully", HttpStatus.OK);
        } catch (InvalidReactionTypeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid request body. Please check the format and values.";
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
