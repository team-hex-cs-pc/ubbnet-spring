package com.example.CollectiveProject.API;

import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {
    private PostService service;

    @PostMapping("/add")
    public Post add(@RequestBody Post newEntity) {
        return this.service.addService(newEntity);
    }

    @PostMapping("/all")
    public List<Post> addAll(@RequestBody List<Post> list) {
        return this.service.addAllService(list);
    }

    public ResponseEntity<Object> showMessage(Object messageOrEntity, HttpStatus status) {
        return ResponseEntity.status(status).body(messageOrEntity);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        if (this.service.getAll().isEmpty()) {
            return this.showMessage("There are no posts yet.", HttpStatus.NOT_FOUND); // 404
        }
        return this.showMessage(this.service.getAll(), HttpStatus.OK); // 200
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
        Post NewsPost = this.service.getEntityById(id);
        if (NewsPost != null) {
            return ResponseEntity.ok(NewsPost); // 200
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
    public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody Post entity) {
        Post NewsPost = this.service.updateService(id, entity);
        if (NewsPost != null) {
            return ResponseEntity.status(HttpStatus.OK).body(NewsPost); // 200
        } else {
            String errorMessage = "The post with id " + id + " was not found.";
            return this.showMessage(errorMessage, HttpStatus.NOT_FOUND); // 404
        }
    }
}
