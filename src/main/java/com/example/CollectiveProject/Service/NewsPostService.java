package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.Domain.News;
import com.example.CollectiveProject.Domain.Writer;
import com.example.CollectiveProject.Repository.NewsPostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NewsPostService {
    private NewsPostRepository repository;

    public News addService(News entity) {
        return this.repository.save(entity); }

    public List<News> addAllService(List<News> entities) { return this.repository.saveAll(entities); }

    public News getEntityById(Integer id) { return this.repository.findById(id).orElse(null); }

    public List<News> getAll() { return this.repository.findAll(); }

    public boolean exists(Integer id) { return this.repository.existsById(id); }

    public String deleteService(Integer id)
    {
        News post = this.getEntityById(id);
        if(post != null)
        {
            this.repository.delete(post);
            return "Article with id " + id + " was deleted.";
        }
        return "There is no article with the id " + id + '.';
    }

    public News updateService(Integer id, News newEntity)
    {
        News entityForUpdate = this.repository.findById(id).orElse(null);
        if (entityForUpdate != null)
        {
            // entityForUpdate.setAuthor(newEntity.getAuthor());
            entityForUpdate.setCategory(newEntity.getCategory());
            entityForUpdate.setContent(newEntity.getContent());
            entityForUpdate.setTitle(newEntity.getTitle());
            return this.repository.save(entityForUpdate);
        }
        return null;
    }

    public Writer getAuthorByArticle(Integer postId)
    {
        News newsPost = this.getEntityById(postId);
        if(newsPost != null)
        {
            return newsPost.getWriter();
        }
        return null;
    }
}
