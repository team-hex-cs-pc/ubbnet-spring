package com.example.CollectiveProject.DTO;

import com.example.CollectiveProject.Utilities.CalendarUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
