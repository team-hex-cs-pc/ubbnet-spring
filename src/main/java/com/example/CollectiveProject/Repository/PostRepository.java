package com.example.CollectiveProject.Repository;

import com.example.CollectiveProject.DTO.PostResponseDTO;
import com.example.CollectiveProject.Domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findPostByPostReference(String postReference);

    @Query("SELECT new com.example.CollectiveProject.DTO.PostResponseDTO(" +
            "p.postReference, p.title, p.content, p.category, p.likes, p.publicationDate, p.user.username) " +
            "FROM Post p")
    Page<PostResponseDTO> findAllPosts(Pageable pageable);
}
