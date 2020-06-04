package com.epam.lab.service.converter;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.model.Author;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AuthorModelDtoConverter implements DtoModelConverter<Author, AuthorDto> {

    @Autowired
    private ModelMapper modelMapper;

    public AuthorDto toDto(Author author) {
        if (author == null) {
            return null;
        }
        AuthorDto authorDto = modelMapper.map(author, AuthorDto.class);
        authorDto.setNewsList(new ArrayList<>());
        return authorDto;

    }

    @Override
    public Author toModel(AuthorDto authorDto) {
        if (authorDto == null) {
            return null;
        }
        return modelMapper.map(authorDto, Author.class);
    }


}
