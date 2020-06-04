package com.epam.lab.service.converter;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.model.News;
import com.epam.lab.service.util.DateConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class NewsModelDtoConverter implements DtoModelConverter<News, NewsDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public NewsDto toDto(News model) {
        if (model == null) {
            return null;
        }
        NewsDto newsDto = modelMapper.map(model, NewsDto.class);
        newsDto.setTagList(new ArrayList<>());
        newsDto.setModificationDate(DateConverter.convertDateToLocalDate(model.getModificationDate()));
        newsDto.setCreationDate(DateConverter.convertDateToLocalDate(model.getCreationDate()));
        return newsDto;
    }

    @Override
    public News toModel(NewsDto dto) {
        if (dto == null) {
            return null;
        }
        News news = modelMapper.map(dto, News.class);
        news.setModificationDate(DateConverter.convertLocalDateToDate(dto.getModificationDate()));
        news.setCreationDate(DateConverter.convertLocalDateToDate(dto.getCreationDate()));
        return news;
    }

}
