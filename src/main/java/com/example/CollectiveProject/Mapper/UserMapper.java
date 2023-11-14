package com.example.CollectiveProject.Mapper;

import com.example.CollectiveProject.Utilities.CalendarUtils;
import com.example.CollectiveProject.DTO.UserRequestDTO;
import com.example.CollectiveProject.DTO.UserResponseDTO;
import com.example.CollectiveProject.Domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.text.ParseException;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true)


    @Mapping(target = "username", source = "userRequestDTO.username")
    @Mapping(target = "firstName", source = "userRequestDTO.firstName")
    @Mapping(target = "lastName", source = "userRequestDTO.lastName")
    @Mapping(target = "email", source = "userRequestDTO.email")
    @Mapping(target = "gender", source = "userRequestDTO.gender")
    @Mapping(target = "birthdate", expression = "java(convertStringToDate(userRequestDTO.getBirthdate()))")
    @Mapping(target = "password", source = "userRequestDTO.password")
    User userRequestDtoToEntity(UserRequestDTO userRequestDTO);

    default Date convertStringToDate(String date) {
        try {
            return CalendarUtils.convertStringToDate(date);
        } catch (ParseException e) {
            // Handle the exception or log it
            return null;
        }
    }

    default String convertDateToString(Date date) {
        return CalendarUtils.convertDateToString(String.valueOf(date));
    }

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "gender", source = "user.gender")
    @Mapping(target = "birthdate", source = "user.birthdate")
    @Mapping(target = "posts", ignore = true)
    UserResponseDTO userToResponseDto(User user);

}
