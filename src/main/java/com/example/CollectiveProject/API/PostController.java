package com.example.CollectiveProject.API;

import com.example.CollectiveProject.DTO.PostRequestDTO;
import com.example.CollectiveProject.DTO.PostResponseDTO;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Exceptions.NotFoundException;
import com.example.CollectiveProject.Service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {
    @Autowired
    private final PostService postService;

    @PostMapping
    //  We will use this method declaration in order to use JWT from headers
    //  In Service everything is implemented, just needs to be uncommented
    // Those params should be passed to all methods after the login is in frontend in order to properly test it
//    public ResponseEntity<?> add(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PostRequestDTO postRequestDTO) {
    public ResponseEntity<?> add(@RequestBody PostRequestDTO postRequestDTO) {
        try {
            return new ResponseEntity<>(postService.addService(postRequestDTO), HttpStatus.CREATED);
        } catch (Exception ex) { // TODO: More custom exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @PostMapping("/all")
    public ResponseEntity<Object> addAll(@RequestBody List<PostRequestDTO> postRequestDTOs) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getCredentials();
        User user = null;

        List<PostResponseDTO> responseDTOs = postService.addAllService(postRequestDTOs, user);
        return showMessage(responseDTOs, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> showMessage(Object messageOrEntity, HttpStatus status) {
        return ResponseEntity.status(status).body(messageOrEntity);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page) {
        try {
            return new ResponseEntity<>(postService.getAll(page), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @GetMapping("/user/{postReference}")
    public ResponseEntity<?> getUser(@PathVariable String postReference) {
        try {
            return new ResponseEntity<>(postService.getUserByPost(postReference), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @GetMapping("/{postReference}")
    public ResponseEntity<?> getByPostReference(@PathVariable String postReference) {
        try {
            return new ResponseEntity<>(postService.getEntityByPostReference(postReference), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @DeleteMapping("/{postReference}")
    public ResponseEntity<?> delete(@PathVariable String postReference) {
        try {
            return new ResponseEntity<>(postService.deleteService(postReference), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @PutMapping("/{postReference}")
    public ResponseEntity<?> update(@PathVariable String postReference, @RequestBody PostRequestDTO postRequestDTO) {
        try {
            return new ResponseEntity<>(postService.updateService(postReference, postRequestDTO), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }
}
