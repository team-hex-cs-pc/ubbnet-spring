package com.example.CollectiveProject.Mapper;

import com.example.CollectiveProject.DTO.WriterWithoutCredentialsDTO;
import com.example.CollectiveProject.Domain.Writer;

public class WriterMapper {
    public WriterWithoutCredentialsDTO to_writerWithoutCredentialsDTO(Writer writer)
    {
        return new WriterWithoutCredentialsDTO(writer.getName(), writer.getGender(), writer.getAge(), writer.getUsername(),
                writer.getPosts());
    }
}
