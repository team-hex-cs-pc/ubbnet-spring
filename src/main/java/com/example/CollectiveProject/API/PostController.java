package com.example.CollectiveProject.API;

import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.DTO.PostRequestDTO;
import com.example.CollectiveProject.DTO.PostResponseDTO;
import com.example.CollectiveProject.Service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {
    private final PostService service;

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getCredentials();

        PostResponseDTO responseDTO = service.addService(postRequestDTO, user);
        return showMessage(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/all")
    public ResponseEntity<Object> addAll(@RequestBody List<PostRequestDTO> postRequestDTOs) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getCredentials();

        List<PostResponseDTO> responseDTOs = service.addAllService(postRequestDTOs, user);
        return showMessage(responseDTOs, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> showMessage(Object messageOrEntity, HttpStatus status) {
        return ResponseEntity.status(status).body(messageOrEntity);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        List<PostResponseDTO> posts = service.getAll();

        if (posts.isEmpty()) {
            return showMessage("There are no posts yet.", HttpStatus.NOT_FOUND);
        }

        // sort the posts by publicationDate in descending order
        List<PostResponseDTO> sortedPosts = posts.stream()
                .sorted((post1, post2) -> post2.getPublicationDate().compareTo(post1.getPublicationDate()))
                .collect(Collectors.toList());

        return showMessage(sortedPosts, HttpStatus.OK);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<Object> getAuthor(@PathVariable("id") Integer id) {
        User user = this.service.getAuthorByArticle(id);
        if (user != null) {
            return ResponseEntity.ok(user); // 200
        } else {
            String errorMessage = "User not found for post with ID: " + id;
            return showMessage(errorMessage, HttpStatus.NOT_FOUND); // 404
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Integer id) {
        PostResponseDTO postResponseDTO = this.service.getEntityById(id);
        if (postResponseDTO != null) {
            return ResponseEntity.ok(postResponseDTO); // 200
        } else {
            String errorMessage = "Post with id " + id + " was not found.";
            return this.showMessage(errorMessage, HttpStatus.NOT_FOUND); // 404
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) {
        String deleteMessage;
        HttpStatus status;
        if (this.service.exists(id)) {
            this.service.deleteService(id);
            deleteMessage = "Post with id " + id + " was successfully deleted.";
            status = HttpStatus.OK; // 200
        } else {
            deleteMessage = "Post with id " + id + " was not found.";
            status = HttpStatus.NOT_FOUND; // 404
        }
        return this.showMessage(deleteMessage, status);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody PostRequestDTO postRequestDTO) {
        PostResponseDTO postResponseDTO = this.service.updateService(id, postRequestDTO);
        if (postResponseDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(postResponseDTO); // 200
        } else {
            String errorMessage = "The post with id " + id + " was not found.";
            return this.showMessage(errorMessage, HttpStatus.NOT_FOUND); // 404
        }
    }
}
