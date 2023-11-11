package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.DTO.UserRequestDTO;
import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Exceptions.NotFoundException;
import com.example.CollectiveProject.Mapper.UserMapper;
import com.example.CollectiveProject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService  implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository repository;

    public boolean registerUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.userRequestDtoToEntity(userRequestDTO);
        try {
            this.repository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User getUserById(Integer id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return this.repository.findAll();
    }

    public boolean userExists(Integer id) {
        return this.repository.existsById(id);
    }

    public boolean deleteUser(Integer id) {
        User user = this.getUserById(id);
        if (user != null) {
            this.repository.delete(user);
            return true;
        }
        return false;
    }

    public boolean updateUser(Integer id, User newEntity) {
        User entityForUpdate = this.repository.findById(id).orElse(null);
        if (entityForUpdate != null) {
            entityForUpdate.setBirthdate(newEntity.getBirthdate());
            entityForUpdate.setFirstName(newEntity.getFirstName());
            entityForUpdate.setLastName(newEntity.getLastName());
            entityForUpdate.setEmail(newEntity.getEmail());
            entityForUpdate.setUsername(newEntity.getUsername());
            entityForUpdate.setPassword(newEntity.getPassword());
            entityForUpdate.setGender(newEntity.getGender());
            this.repository.save(entityForUpdate);
            return true;
        }
        return false;
    }

    public List<Post> getPostsByUser(Integer writerId) {
        User user = this.getUserById(writerId);
        if (user != null) {
            return user.getPosts();
        }
        return null;
    }

    public User getUserByEmail(String email) throws NotFoundException {
        User user = this.repository.findUserByEmail(email);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public void UpdateUserPosts(Integer userId, List<Post> posts) {
        User user = this.getUserById(userId);
        user.setPosts(posts);
        this.repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.repository.findUserByEmail(email);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();

        return userDetails;
    }
}
