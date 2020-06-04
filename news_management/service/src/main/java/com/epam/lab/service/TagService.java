package com.epam.lab.service;

import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.validator.ValidDtoParam;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface TagService {
    TagDto save(@ValidDtoParam(paramNames = "name", message = "name must be input") TagDto tagDto) throws ServiceException;

    TagDto update(@ValidDtoParam(paramNames = "id", message = "id must be input") TagDto tagDto) throws ServiceException;

    boolean delete(@NotNull(message = "id must be input") Long id) throws ServiceException;

    TagDto getById(@NotNull(message = "id must be input") Long id);

    List<TagDto> getAll();
}
