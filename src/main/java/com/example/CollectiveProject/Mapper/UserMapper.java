package com.example.CollectiveProject.Mapper;

import com.example.CollectiveProject.DTO.UserWithoutCredentialsDTO;
import com.example.CollectiveProject.Domain.User;

public class UserMapper {
    public UserWithoutCredentialsDTO to_userWithoutCredentialsDTO(User user) {
        return new UserWithoutCredentialsDTO(user.getName(), user.getGender(), user.getAge(), user.getUsername(),
                user.getPosts());
    }
}
