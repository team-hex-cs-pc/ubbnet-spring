package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.DTO.PostResponseDTO;
import com.example.CollectiveProject.DTO.UserRequestDTO;
import com.example.CollectiveProject.DTO.UserResponseDTO;
import com.example.CollectiveProject.Domain.FriendRelation;
import com.example.CollectiveProject.Domain.FriendRequest;
import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Exceptions.DuplicateEntryException;
import com.example.CollectiveProject.Exceptions.NotFoundException;
import com.example.CollectiveProject.Mapper.PostMapper;
import com.example.CollectiveProject.Mapper.UserMapper;
import com.example.CollectiveProject.Repository.FriendRequestRepository;
import com.example.CollectiveProject.Repository.FriendsRepository;
import com.example.CollectiveProject.Repository.UserRepository;
import jakarta.transaction.Transactional;
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
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private FriendsRepository friendsRepository;

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

    public void sendFriendRequest(Integer fromUserId, Integer toUserId) throws NotFoundException, DuplicateEntryException {
        if (fromUserId == toUserId) {
            throw new DuplicateEntryException("You can't send a friend request to yourself");
        }

        User fromUser = this.userRepository.findUserByUserId(fromUserId);
        User toUser = this.userRepository.findUserByUserId(toUserId);

        if (fromUser == null || toUser == null) {
            throw new NotFoundException("User not found");
        }

        if (this.friendRequestRepository.existsBySenderIdAndReceiverId(fromUserId, toUserId) ||
                this.friendRequestRepository.existsBySenderIdAndReceiverId(toUserId, fromUserId)) {
            throw new DuplicateEntryException("Friend request already sent");
        }

        this.friendRequestRepository.save(new FriendRequest(fromUserId, toUserId));
    }

    @Transactional
    public void acceptFriendRequest(Integer id) throws NotFoundException {
        FriendRequest friendRequest = this.friendRequestRepository.findById(id);

        if (friendRequest == null) {
            throw new NotFoundException("Friend request not found");
        }

        this.friendsRepository.save(new FriendRelation(friendRequest.getSenderId(), friendRequest.getReceiverId()));
        this.friendRequestRepository.deleteById(id);
    }

    @Transactional
    public void declineFriendRequest(Integer id) throws NotFoundException {
        FriendRequest friendRequest = this.friendRequestRepository.findById(id);

        if (friendRequest == null) {
            throw new NotFoundException("Friend request not found");
        }

        this.friendRequestRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(email);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.getEmail()).password(user.getPassword()).build();

        return userDetails;
    }
}
