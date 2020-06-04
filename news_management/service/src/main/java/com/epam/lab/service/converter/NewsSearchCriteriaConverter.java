package com.epam.lab.service.converter;

import com.epam.lab.dto.NewsSearchCriteriaDto;
import com.epam.lab.model.NewsSearchCriteriaModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class NewsSearchCriteriaConverter implements DtoModelConverter<NewsSearchCriteriaModel, NewsSearchCriteriaDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public NewsSearchCriteriaDto toDto(NewsSearchCriteriaModel model) {

        return model == null ? null : modelMapper.map(model, NewsSearchCriteriaDto.class);
    }

    @Override
    public NewsSearchCriteriaModel toModel(NewsSearchCriteriaDto dto) {
        return dto == null ? null : modelMapper.map(dto, NewsSearchCriteriaModel.class);
    }
}
