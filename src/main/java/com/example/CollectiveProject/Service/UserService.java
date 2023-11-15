package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.DTO.PostResponseDTO;
import com.example.CollectiveProject.DTO.UserRequestDTO;
import com.example.CollectiveProject.DTO.UserResponseDTO;
import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Exceptions.NotFoundException;
import com.example.CollectiveProject.Mapper.PostMapper;
import com.example.CollectiveProject.Mapper.UserMapper;
import com.example.CollectiveProject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.CollectiveProject.Utilities.Constants;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserRepository userRepository;

    public boolean addService(UserRequestDTO userRequest) {
        try {
            User user = userMapper.userRequestDtoToEntity(userRequest);
            this.userRepository.save(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<User> addAllService(List<User> entities) {
        return this.userRepository.saveAll(entities);
    }

    public Page<UserResponseDTO> getAll(int pageCount) throws NotFoundException {
        Page<UserResponseDTO> users = userRepository.findAllUsers(PageRequest.of(pageCount, Constants.PAGE_SIZE));

        if (users.getContent().isEmpty()) {
            throw new NotFoundException("No users found!");
        }

        return users;
    }

    public UserResponseDTO getUserByUsername(String username) throws NotFoundException {
        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new NotFoundException("No user found!");
        }

        return userMapper.userToResponseDto(user);
    }

    public boolean deleteService(String username) throws NotFoundException {
        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new NotFoundException("No user found!");
        } else {
            userRepository.delete(user);
            return true;
        }
    }

    public boolean updateService(String username, UserRequestDTO newEntity) throws NotFoundException {
        User userToUpdate = userRepository.findUserByUsername(username);

        if (userToUpdate == null) {
            throw new NotFoundException("Post not found!");
        }

        try {
            User newUser = userMapper.userRequestDtoToEntity(newEntity);
            newUser.setUserId(userToUpdate.getUserId());
            userRepository.save(newUser);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<PostResponseDTO> getPostsByUser(String username) throws NotFoundException {
        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new NotFoundException("User not found!");
        } else {
            List<Post> userPosts = user.getPosts();
            return userPosts.stream().map(postMapper::postToResponseDto).collect(Collectors.toList());
        }
    }

    public User getUserByEmail(String email) throws NotFoundException {
        User user = this.userRepository.findUserByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(email);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.getEmail()).password(user.getPassword()).build();

        return userDetails;
    }

    public String getUsernameByEmail(String email) throws NotFoundException {
        User user = userRepository.findUsernameByEmail(email);
        if (user != null) {
            return user.getUsername();
        } else {
            throw new NotFoundException("Username not found for the given email");
        }
    }
}
