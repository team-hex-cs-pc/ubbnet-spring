package com.example.CollectiveProject.API;

import com.example.CollectiveProject.DTO.LoginCredentialsDTO;
import com.example.CollectiveProject.DTO.UserRequestDTO;
import com.example.CollectiveProject.DTO.UserResponseDTO;
import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Exceptions.NotFoundException;
import com.example.CollectiveProject.Mapper.UserMapper;
import com.example.CollectiveProject.Service.UserService;
import com.example.CollectiveProject.Utilities.JwtUtilities;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private UserMapper userMapper;
    private JwtUtilities jwtUtil;
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO user) {
        if(this.service.registerUser(user)) {
            return ResponseEntity.ok("User registered successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User already exists");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginCredentialsDTO credentialsDTO) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentialsDTO.getEmail(), credentialsDTO.getPassword()));
            String email = authentication.getName();
            User user = service.getUserByEmail(email);
            String token = jwtUtil.createToken(user);

            return ResponseEntity.ok(token);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid username or password");
        } catch (NotFoundException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<Object> showMessage(Object messageOrEntity, HttpStatus status) {
        return ResponseEntity.status(status).body(messageOrEntity);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {

        List <UserResponseDTO> usersResponseList = this.service.getAllUsers().stream().map(userMapper::userToResponseDto).toList();

        if (this.service.getAllUsers().isEmpty()) {
            return this.showMessage("There are no users yet.", HttpStatus.NOT_FOUND); // 404
        }
        return this.showMessage(usersResponseList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Integer id) {
        User user = this.service.getUserById(id);
        UserResponseDTO userResponseDTO = userMapper.userToResponseDto(user);
        if (user != null) {
            return ResponseEntity.ok(userResponseDTO); // 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!"); // 404
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        if (this.service.deleteUser(id)) {
            return ResponseEntity.ok("Account deleted successfully!");
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Deletion failed!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody UserRequestDTO entity) {
        User userToUpdate = userMapper.userRequestDtoToEntity(entity);

        User userToUpdatePosts = service.getUserById(id);
        List<Post> posts = userToUpdatePosts.getPosts();

        User newUser = this.service.updateUser(id, userToUpdate);
        service.UpdateUserPosts(id, posts);

        if (newUser != null) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            String errorMessage = "The user with id " + id + " was not found.";
            return this.showMessage(errorMessage, HttpStatus.NOT_FOUND); // 404
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getAllWithoutCredentials() {
        List<User> users = this.service.getAllUsers();
        if (users.isEmpty()) {
            return showMessage("There are no users yet.", HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(userMapper.userToResponseDto(users.get(0)), HttpStatus.OK);
    }

    @GetMapping("{id}/posts")
    public ResponseEntity<?> getPostsByUser(@PathVariable("id") Integer id) {
        List<Post> posts = this.service.getPostsByUser(id);
        if (posts.isEmpty()) {
            String message = "There are no posts.";
            this.showMessage(message, HttpStatus.NOT_FOUND); // 404
        }
        return this.showMessage(posts, HttpStatus.OK); // 200
    }
}
