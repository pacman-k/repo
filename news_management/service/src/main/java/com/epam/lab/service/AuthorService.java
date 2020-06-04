package com.epam.lab.service;


import com.epam.lab.dto.AuthorDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.validator.ValidDtoParam;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface AuthorService {
    AuthorDto save(@ValidDtoParam(paramNames = {"name", "surname"}, message = "name and surname must be input") AuthorDto author) throws ServiceException;

    AuthorDto update(@ValidDtoParam(paramNames = {"id"}, message = "id must be input") AuthorDto authorDto) throws ServiceException;

    boolean delete(@NotNull(message = "id must be input") Long id) throws ServiceException;

    AuthorDto getById(@NotNull(message = "id must be input") Long id);

    List<AuthorDto> getAll();

}
