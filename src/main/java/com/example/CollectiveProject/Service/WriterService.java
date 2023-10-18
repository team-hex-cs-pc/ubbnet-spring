package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.Domain.News;
import com.example.CollectiveProject.Domain.Writer;
import com.example.CollectiveProject.Repository.WriterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WriterService {
    private WriterRepository repository;

    public Writer addService(Writer entity) {
        return this.repository.save(entity);
    }

    public List<Writer> addAllService(List<Writer> entities) {
        return this.repository.saveAll(entities);
    }

    public Writer getEntityById(Integer id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<Writer> getAll() {
        return this.repository.findAll();
    }

    public boolean exists(Integer id) { return this.repository.existsById(id); }

    public String deleteService(Integer id)
    {
        Writer user = this.getEntityById(id);
        if(user != null)
        {
            this.repository.delete(user);
            return "User with id " + id + ".";
        }
        return "There is no User with the id " + id + '.';
    }

    public Writer updateService(Integer id, Writer newEntity) {
        Writer entityForUpdate = this.repository.findById(id).orElse(null);
        if (entityForUpdate != null)
        {
            entityForUpdate.setAge(newEntity.getAge());
            entityForUpdate.setName(newEntity.getName());
            entityForUpdate.setEmail(newEntity.getEmail());
            entityForUpdate.setUsername(newEntity.getUsername());
            entityForUpdate.setPassword(newEntity.getPassword());
            entityForUpdate.setGender(newEntity.getGender());
            return this.repository.save(entityForUpdate);
        }
        return null;
    }

    public List<News> getNewsByAuthor(Integer writerId)
    {
        Writer w = this.getEntityById(writerId);
        if(w != null)
        {
            return w.getPosts();
        }
        return null;
    }
}
