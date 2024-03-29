package com.example.CollectiveProject.API;

import com.example.CollectiveProject.DTO.AuthResponseDTO;
import com.example.CollectiveProject.DTO.LoginCredentialsDTO;
import com.example.CollectiveProject.DTO.UserRequestDTO;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Exceptions.DuplicateEntryException;
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
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtilities jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> add(@RequestBody UserRequestDTO userRequest) {
        try {
            String hashedPassword = userService.hashPassword(userRequest.getPassword());
            userRequest.setPassword(hashedPassword);
            return new ResponseEntity<>(userService.addService(userRequest), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @PostMapping("/all")
    public List<User> addAll(@RequestBody List<User> list) {
        return this.userService.addAllService(list);
    }


    // TODO: less logic in controller
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCredentialsDTO credentialsDTO) {
        try {
            User user = userService.getUserByEmail(credentialsDTO.getEmail());
            if (!BCrypt.checkpw(credentialsDTO.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid username or password");
            }
            String hashedPassword = user.getPassword();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentialsDTO.getEmail(), hashedPassword));
            String token = jwtUtil.createToken(user);

            return ResponseEntity.ok(new AuthResponseDTO(token));

        } catch (InternalAuthenticationServiceException | BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        } catch (NotFoundException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page) {
        try {
            //TODO CHANGE TO service.getAll(page) when it is done
            return new ResponseEntity<>(userService.getAllNormal(), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/username/{email}")
    public ResponseEntity<String> getUsernameByEmail(@PathVariable String email) {
        try {
            String username = userService.getUsernameByEmail(email);
            return ResponseEntity.ok(username);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> delete(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.deleteService(username), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> update(@PathVariable String username, @RequestBody UserRequestDTO newUser) {
        try {
            return new ResponseEntity<>(userService.updateService(username, newUser), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @GetMapping("/posts/{username}")
    public ResponseEntity<?> getPosts(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.getPostsByUser(username), HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
//        UserMapper mapper = new UserMapper();
//        Set<UserWithoutCredentialsDTO> all = new HashSet<>();
//        for (User user : users) {
//            all.add(mapper.to_userWithoutCredentialsDTO(user));
//        }
//        return showMessage(all, HttpStatus.OK); // 200
//        return null;
    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUserByEmail(email);

            return ResponseEntity.ok(userMapper.userToResponseDto(user));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        } catch (NotFoundException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/add-friend/{to}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable String to) {
        try {
            String from = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.sendFriendRequest(from, to);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DuplicateEntryException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @PostMapping("/accept-friend/{id}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Integer id) {
        try {
            userService.acceptFriendRequest(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/decline-friend/{id}")
    public ResponseEntity<?> declineFriendRequest(@PathVariable Integer id) {
        try {
            userService.declineFriendRequest(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("get-friend-request/{toUserEmail}")
    public ResponseEntity<?> getFriendRequestBetweenTwoUsers(@PathVariable String toUserEmail) {
        try {
            var friendRequest = userService.getFriendRequest(SecurityContextHolder.getContext().getAuthentication().getName(), toUserEmail);
            return new ResponseEntity<>(friendRequest, HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("is-friend/{toUserEmail}")
    public ResponseEntity<?> getFriendRelationBetweenTwoUsers(@PathVariable String toUserEmail) {
        try {
            var areFriends = userService.getFriendRelation(SecurityContextHolder.getContext().getAuthentication().getName(), toUserEmail);
            return new ResponseEntity<>(areFriends, HttpStatus.OK);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
