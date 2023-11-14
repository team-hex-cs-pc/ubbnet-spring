package com.example.CollectiveProject.Mapper;

import com.example.CollectiveProject.DTO.UserRequestDTO;
import com.example.CollectiveProject.DTO.UserResponseDTO;
import com.example.CollectiveProject.Domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true)


    @Mapping(target = "username", source = "userRequestDTO.username")
    @Mapping(target = "firstName", source = "userRequestDTO.firstName")
    @Mapping(target = "lastName", source = "userRequestDTO.lastName")
    @Mapping(target = "email", source = "userRequestDTO.email")
    @Mapping(target = "gender", source = "userRequestDTO.gender")
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "password", source = "userRequestDTO.password")
    User userRequestDtoToEntity(UserRequestDTO userRequestDTO);


    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "gender", source = "user.gender")
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "posts", ignore = true)
    UserResponseDTO userToResponseDto(User user);

}
