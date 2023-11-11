package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Repository.PostRepository;
import com.example.CollectiveProject.DTO.PostRequestDTO;
import com.example.CollectiveProject.DTO.PostResponseDTO;
import com.example.CollectiveProject.Mapper.PostMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final PostMapper postMapper;

    public PostResponseDTO addService(PostRequestDTO postRequestDTO, User user) {
        Post postEntity = postMapper.postRequestDtoToEntity(postRequestDTO);
        postEntity.setUser(user); // Set the user obtained from the controller
        Post savedPost = repository.save(postEntity);
        return postMapper.postToResponseDto(savedPost);
    }

    public List<PostResponseDTO> addAllService(List<PostRequestDTO> postRequestDTOs, User user) {
        List<Post> postEntities = postRequestDTOs.stream()
                .map(postRequestDTO -> {
                    Post post = postMapper.postRequestDtoToEntity(postRequestDTO);
                    post.setUser(user); // Set the user obtained from the controller
                    return post;
                })
                .collect(Collectors.toList());

        List<Post> savedPosts = repository.saveAll(postEntities);

        return savedPosts.stream()
                .map(postMapper::postToResponseDto)
                .collect(Collectors.toList());
    }

    public PostResponseDTO getEntityById(Integer id) {
        Post post = repository.findById(id).orElse(null);
        return (post != null) ? postMapper.postToResponseDto(post) : null;
    }

    public List<PostResponseDTO> getAll() {
        List<Post> posts = repository.findAll();
        return posts.stream()
                .map(postMapper::postToResponseDto)
                .collect(Collectors.toList());
    }

    public boolean exists(Integer id) {
        return repository.existsById(id);
    }

    public String deleteService(Integer id) {
        Post post = repository.findById(id).orElse(null);
        if (post != null) {
            repository.delete(post);
            return "Article with id " + id + " was deleted.";
        }
        return "There is no article with the id " + id + '.';
    }

    public PostResponseDTO updateService(Integer id, PostRequestDTO newPostRequestDTO) {
        Post postForUpdate = repository.findById(id).orElse(null);
        if (postForUpdate != null) {
            // Update fields as needed
            postForUpdate.setTitle(newPostRequestDTO.getTitle());
            postForUpdate.setContent(newPostRequestDTO.getContent());
            postForUpdate.setCategory(newPostRequestDTO.getCategory());

            Post updatedPost = repository.save(postForUpdate);
            return postMapper.postToResponseDto(updatedPost);
        }
        return null;
    }

    public User getAuthorByArticle(Integer postId) {
        Post newsPost = repository.findById(postId).orElse(null);
        return (newsPost != null) ? newsPost.getUser() : null;
    }
}
