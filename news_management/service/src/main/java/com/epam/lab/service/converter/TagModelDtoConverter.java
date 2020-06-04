package com.epam.lab.service.converter;

import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class TagModelDtoConverter implements DtoModelConverter<Tag, TagDto> {
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public TagDto toDto(Tag model) {
        if (model == null) {
            return null;
        }
        TagDto tagDto = modelMapper.map(model, TagDto.class);
        tagDto.setNewsList(new ArrayList<>());
        return tagDto;
    }

    @Override
    public Tag toModel(TagDto dto) {
        if (dto == null) {
            return null;
        }
        return modelMapper.map(dto, Tag.class);
    }

}
