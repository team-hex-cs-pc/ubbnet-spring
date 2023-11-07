package com.example.CollectiveProject.API;

import com.example.CollectiveProject.DTO.UserWithoutCredentialsDTO;
import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Mapper.UserMapper;
import com.example.CollectiveProject.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserService service;

    @PostMapping("/add")
    public User add(@RequestBody User newEntity) {
        return this.service.addService(newEntity);
    }

    @PostMapping("/all")
    public List<User> addAll(@RequestBody List<User> list) {
        return this.service.addAllService(list);
    }

    public ResponseEntity<Object> showMessage(Object messageOrEntity, HttpStatus status) {
        return ResponseEntity.status(status).body(messageOrEntity);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        if (this.service.getAll().isEmpty()) {
            return this.showMessage("There are no users yet.", HttpStatus.NOT_FOUND); // 404
        }
        return this.showMessage(this.service.getAll(), HttpStatus.OK); // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Integer id) {
        User user = this.service.getEntityById(id);
        if (user != null) {
            return ResponseEntity.ok(user); // 200
        } else {
            String errorMessage = "User with id " + id + " was not found.";
            return this.showMessage(errorMessage, HttpStatus.NOT_FOUND); // 404
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) {
        String deleteMessage;
        HttpStatus status;
        if (this.service.exists(id)) {
            this.service.deleteService(id);
            deleteMessage = "User with id " + id + " was successfully deleted.";
            status = HttpStatus.OK; // 200
        } else {
            deleteMessage = "User with id " + id + " was not found.";
            status = HttpStatus.NOT_FOUND; // 404
        }
        return this.showMessage(deleteMessage, status);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody User entity) {
        User user = this.service.updateService(id, entity);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user); // 200
        } else {
            String errorMessage = "The user with id " + id + " was not found.";
            return this.showMessage(errorMessage, HttpStatus.NOT_FOUND); // 404
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Object> getAllWithoutCredentials() {
        List<User> users = this.service.getAll();
        if (users.isEmpty()) {
            return showMessage("There are no users yet.", HttpStatus.NOT_FOUND); // 404
        }

        UserMapper mapper = new UserMapper();
        Set<UserWithoutCredentialsDTO> all = new HashSet<>();
        for (User user : users) {
            all.add(mapper.to_userWithoutCredentialsDTO(user));
        }
        return showMessage(all, HttpStatus.OK); // 200
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Object> getPosts(@PathVariable("id") Integer id) {
        List<Post> posts = this.service.getPostsByAuthor(id);
        if (posts.isEmpty()) {
            String message = "There are no posts.";
            this.showMessage(message, HttpStatus.NOT_FOUND); // 404
        }
        return this.showMessage(posts, HttpStatus.OK); // 200
    }
}
