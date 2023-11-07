package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository repository;

    public Post addService(Post entity) {
        return this.repository.save(entity);
    }

    public List<Post> addAllService(List<Post> entities) {
        return this.repository.saveAll(entities);
    }

    public Post getEntityById(Integer id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<Post> getAll() {
        return this.repository.findAll();
    }

    public boolean exists(Integer id) {
        return this.repository.existsById(id);
    }

    public String deleteService(Integer id) {
        Post post = this.getEntityById(id);
        if (post != null) {
            this.repository.delete(post);
            return "Article with id " + id + " was deleted.";
        }
        return "There is no article with the id " + id + '.';
    }

    public Post updateService(Integer id, Post newEntity) {
        Post entityForUpdate = this.repository.findById(id).orElse(null);
        if (entityForUpdate != null) {
            // entityForUpdate.setAuthor(newEntity.getAuthor());
            entityForUpdate.setCategory(newEntity.getCategory());
            entityForUpdate.setContent(newEntity.getContent());
            entityForUpdate.setTitle(newEntity.getTitle());
            return this.repository.save(entityForUpdate);
        }
        return null;
    }

    public User getAuthorByArticle(Integer postId) {
        Post newsPost = this.getEntityById(postId);
        if (newsPost != null) {
            return newsPost.getUser();
        }
        return null;
    }
}
