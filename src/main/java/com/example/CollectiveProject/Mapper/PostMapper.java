package com.example.CollectiveProject.Mapper;

import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.DTO.PostRequestDTO;
import com.example.CollectiveProject.DTO.PostResponseDTO;
import com.example.CollectiveProject.Utilities.CalendarUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "postId", ignore = true)
    @Mapping(target = "postReference", source = "postRequestDTO.postReference")
    @Mapping(target = "title", source = "postRequestDTO.title")
    @Mapping(target = "content", source = "postRequestDTO.content")
    @Mapping(target = "category", source = "postRequestDTO.category")
    @Mapping(target = "publicationDate", expression = "java(getCurrentDate())")
    @Mapping(target = "user", ignore = true)
    Post postRequestDtoToEntity(PostRequestDTO postRequestDTO);

    default Date getCurrentDate() {
        try {
            Date date = Calendar.getInstance().getTime();
            String todayDate = CalendarUtils.convertDateToString(date);
            return CalendarUtils.convertStringToDate(todayDate);
        } catch (Exception e) {
            return Calendar.getInstance().getTime();
        }
    }

    @Mapping(target = "postReference", source = "post.postReference")
    @Mapping(target = "title", source = "post.title")
    @Mapping(target = "content", source = "post.content")
    @Mapping(target = "category", source = "post.category")
    @Mapping(target = "likes", source = "post.likes")
    @Mapping(target = "publicationDate", expression = "java(getStringFromDate(post.getPublicationDate()))")
    @Mapping(target = "username", source = "post.user.username")
    PostResponseDTO postToResponseDto(Post post);

    default String getStringFromDate(Date date) {
        return CalendarUtils.convertDateToString(date);
    }
}
