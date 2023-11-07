package com.example.CollectiveProject.Repository;

import com.example.CollectiveProject.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
