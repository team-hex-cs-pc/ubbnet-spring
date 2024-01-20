package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.DTO.FriendRequestResponseDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.CollectiveProject.Utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
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

            // Assuming userRequest has a field named 'birthdate' as a String
            String birthdateString = userRequest.getBirthdate(); // Get the birthdate string

            // Parse the string to a date considering it's in ISO 8601 format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Set time zone to UTC
            Date parsedDate = sdf.parse(birthdateString);

            // Set the parsed date in the User entity
            user.setBirthdate(parsedDate);

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

    //TODO remove when getAll is done
    public List<UserResponseDTO> getAllNormal() throws NotFoundException {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new NotFoundException("No users found!");
        }

        return users.stream().map(userMapper::userToResponseDto).collect(Collectors.toList());
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

    public void sendFriendRequest(String fromUserEmail, String toUserEmail) throws NotFoundException, DuplicateEntryException {
        if (fromUserEmail == toUserEmail) {
            throw new DuplicateEntryException("You can't send a friend request to yourself");
        }

        User fromUser = this.userRepository.findUserByEmail(fromUserEmail);
        User toUser = this.userRepository.findUserByEmail(toUserEmail);

        if (fromUser == null || toUser == null) {
            throw new NotFoundException("User not found");
        }

        if (this.friendRequestRepository.existsBySenderIdAndReceiverId(fromUser.getUserId(), toUser.getUserId()) ||
                this.friendRequestRepository.existsBySenderIdAndReceiverId(toUser.getUserId(), fromUser.getUserId())) {
            throw new DuplicateEntryException("Friend request already sent");
        }

        if (this.friendsRepository.existsByUser1IdAndUser2Id(fromUser.getUserId(), toUser.getUserId())
                || this.friendsRepository.existsByUser1IdAndUser2Id(toUser.getUserId(), fromUser.getUserId())
        ) {
            throw new DuplicateEntryException("Friend relation already exist");
        }

        this.friendRequestRepository.save(new FriendRequest(fromUser.getUserId(), toUser.getUserId()));
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

    public FriendRequestResponseDTO getFriendRequest(String user1Email, String user2Email) throws NotFoundException {
        User user1 = this.userRepository.findUserByEmail(user1Email);
        User user2 = this.userRepository.findUserByEmail(user2Email);

        if (user1 == null || user2 == null) {
            throw new NotFoundException("User not found");
        }

        Optional<FriendRequest> friendRequest = this.friendRequestRepository.findBySenderIdAndReceiverId(user1.getUserId(), user2.getUserId());

        if (friendRequest.isEmpty()) {
            friendRequest = this.friendRequestRepository.findBySenderIdAndReceiverId(user2.getUserId(), user1.getUserId());
            if (friendRequest.isEmpty()) {
                throw new NotFoundException("Friend request not found");
            }
        }

        if (friendRequest.get().getReceiverId() == user1.getUserId()) {
            return new FriendRequestResponseDTO(friendRequest.get().getId(), user2.getUsername(), user1.getUsername());
        }

        return new FriendRequestResponseDTO(friendRequest.get().getId(), user1.getUsername(), user2.getUsername());
    }

    public boolean getFriendRelation(String user1Email, String user2Email) throws NotFoundException {
        User user1 = this.userRepository.findUserByEmail(user1Email);
        User user2 = this.userRepository.findUserByEmail(user2Email);

        if (user1 == null || user2 == null) {
            throw new NotFoundException("User not found");
        }

        if (this.friendsRepository.existsByUser1IdAndUser2Id(user1.getUserId(), user2.getUserId())
                || this.friendsRepository.existsByUser1IdAndUser2Id(user2.getUserId(), user1.getUserId())
        ) {
            return true;
        }

        return false;
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

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
