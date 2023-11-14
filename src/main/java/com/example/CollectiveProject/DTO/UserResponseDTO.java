package com.example.CollectiveProject.DTO;

import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Mapper.PostMapper;
import com.example.CollectiveProject.Utilities.CalendarUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String birthdate;
    private String username;
    private List<String> posts;

    public UserResponseDTO(String firstName, String lastName, String email, Date birthday, String gender, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.username = username;
        this.birthdate = new CalendarUtils().convertDateToString(birthday);

    }
}
