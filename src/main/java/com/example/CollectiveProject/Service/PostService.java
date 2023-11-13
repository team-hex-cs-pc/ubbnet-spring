package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.DTO.UserRequestDTO;
import com.example.CollectiveProject.DTO.UserResponseDTO;
import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Exceptions.NotFoundException;
import com.example.CollectiveProject.Mapper.UserMapper;
import com.example.CollectiveProject.Repository.PostRepository;
import com.example.CollectiveProject.DTO.PostRequestDTO;
import com.example.CollectiveProject.DTO.PostResponseDTO;
import com.example.CollectiveProject.Mapper.PostMapper;
import com.example.CollectiveProject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PostMapper postMapper;
    @Autowired
    private final UserMapper userMapper;

    public boolean addService(PostRequestDTO postRequestDTO) {
        try {
            // We will this to get the user that made the post

//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String username = authentication.getName();
//            User user = userRepository.findUserByUsername(username);


            // Just for tests atm; Will be changed with the code above
            User user = userRepository.findUserByUsername(postRequestDTO.getUsername());


            Post postEntity = postMapper.postRequestDtoToEntity(postRequestDTO);
            postEntity.setPostReference("alabala"); // TODO: random
            postEntity.setUser(user);

            postRepository.save(postEntity);
            return true;
        } catch (Exception ex) {      // TODO: More custom exceptions
            return false;
        }
    }

    public List<PostResponseDTO> addAllService(List<PostRequestDTO> postRequestDTOs, User user) {
        List<Post> postEntities = postRequestDTOs.stream()
                .map(postRequestDTO -> {
                    Post post = postMapper.postRequestDtoToEntity(postRequestDTO);
                    post.setUser(user); // Set the user obtained from the controller
                    return post;
                })
                .collect(Collectors.toList());

        List<Post> savedPosts = postRepository.saveAll(postEntities);

        return savedPosts.stream()
                .map(postMapper::postToResponseDto)
                .collect(Collectors.toList());
    }

    public PostResponseDTO getEntityByPostReference(String postReference) throws Exception {
        Post post = postRepository.findPostByPostReference(postReference);
        if(post == null) {
            throw new NotFoundException("Post not found!");
        }

        return postMapper.postToResponseDto(post);
    }

    public List<PostResponseDTO> getAll() throws Exception {
        List<Post> posts = postRepository.findAll();

        if (posts.isEmpty()) {
            throw new NotFoundException("No posts found!");
        }

        return posts.stream()
                .map(postMapper::postToResponseDto)
                .sorted((post1, post2) -> post2.getPublicationDate().compareTo(post1.getPublicationDate()))
                .collect(Collectors.toList());
    }

    public boolean deleteService(String postReference) throws Exception {
        Post post = postRepository.findPostByPostReference(postReference);

        if (post != null) {
            postRepository.delete(post);
            return true;
        } else {
            throw new NotFoundException("Post not found!");
        }
    }

    public boolean updateService(String postReference, PostRequestDTO newPostRequestDTO) throws Exception {
        Post postToUpdate = postRepository.findPostByPostReference(postReference);

        if(postToUpdate == null) {
            throw new NotFoundException("Post not found!");
        }

        try {
            Post newPost = postMapper.postRequestDtoToEntity(newPostRequestDTO);
            newPost.setPostId(postToUpdate.getPostId());
            newPost.setPostReference(postReference);
            postRepository.save(newPost);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public UserResponseDTO getUserByPost(String postReference) throws NotFoundException {
        Post post = postRepository.findPostByPostReference(postReference);

        if(post == null) {
            throw new NotFoundException("Post not found!");
        }

        return userMapper.userToResponseDto(post.getUser());
    }
}
