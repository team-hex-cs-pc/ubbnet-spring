package com.example.CollectiveProject.Mapper;

import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.DTO.PostRequestDTO;
import com.example.CollectiveProject.DTO.PostResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "postId", ignore = true)
    @Mapping(target = "postReference", source = "postRequestDTO.postReference")
    @Mapping(target = "title", source = "postRequestDTO.title")
    @Mapping(target = "content", source = "postRequestDTO.content")
    @Mapping(target = "category", source = "postRequestDTO.category")
    @Mapping(target = "publicationDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    Post postRequestDtoToEntity(PostRequestDTO postRequestDTO);

    @Mapping(target = "postReference", source = "post.postReference")
    @Mapping(target = "title", source = "post.title")
    @Mapping(target = "content", source = "post.content")
    @Mapping(target = "category", source = "post.category")
    @Mapping(target = "likes", source = "post.likes")
    @Mapping(target = "publicationDate", source = "post.publicationDate")
    @Mapping(target = "username", source = "post.user.username")
    PostResponseDTO postToResponseDto(Post post);
}
