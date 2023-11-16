package com.example.CollectiveProject.Repository;

import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.Reaction;
import com.example.CollectiveProject.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    Optional<Reaction> findByPostAndUser(Post post, User user);
    List<Reaction> findAllByPost(Post post);

    List<Reaction> findAllByUser(User user);
}





