package com.example.CollectiveProject.Repository;

import com.example.CollectiveProject.DTO.UserResponseDTO;
import com.example.CollectiveProject.Domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.CollectiveProject.Utilities.CalendarUtils;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);

    User findUserByUserId(Integer id);

    User findUserByUsername(String username);

    @Query("SELECT new com.example.CollectiveProject.DTO.UserResponseDTO(" +
            "u.userId, u.firstName, u.lastName, u.email, u.birthdate, u.gender, u.username) " +
            "FROM User u")
    Page<UserResponseDTO> findAllUsers(Pageable pageable);
}
