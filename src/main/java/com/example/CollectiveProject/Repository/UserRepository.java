package com.example.CollectiveProject.Repository;

import com.example.CollectiveProject.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
