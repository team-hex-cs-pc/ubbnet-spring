package com.example.CollectiveProject.Service;

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

    public User addService(User entity) {
        return this.repository.save(entity);
    }

    public List<User> addAllService(List<User> entities) {
        return this.repository.saveAll(entities);
    }

    public User getEntityById(Integer id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<User> getAll() {
        return this.repository.findAll();
    }

    public boolean exists(Integer id) {
        return this.repository.existsById(id);
    }

    public String deleteService(Integer id) {
        User user = this.getEntityById(id);
        if (user != null) {
            this.repository.delete(user);
            return "User with id " + id + ".";
        }
        return "There is no User with the id " + id + '.';
    }

    public User updateService(Integer id, User newEntity) {
        User entityForUpdate = this.repository.findById(id).orElse(null);
        if (entityForUpdate != null) {
            entityForUpdate.setBirthdate(newEntity.getBirthdate());
            entityForUpdate.setFirstName(newEntity.getFirstName());
            entityForUpdate.setLastName(newEntity.getLastName());
            entityForUpdate.setEmail(newEntity.getEmail());
            entityForUpdate.setUsername(newEntity.getUsername());
            entityForUpdate.setPassword(newEntity.getPassword());
            entityForUpdate.setGender(newEntity.getGender());
            return this.repository.save(entityForUpdate);
        }
        return null;
    }

    public List<Post> getPostsByAuthor(Integer writerId) {
        User w = this.getEntityById(writerId);
        if (w != null) {
            return w.getPosts();
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
