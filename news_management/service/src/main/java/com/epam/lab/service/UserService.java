package com.epam.lab.service;

import com.epam.lab.dto.UserDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.validator.ValidDtoParam;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto save(@ValidDtoParam(paramNames = {"name", "surname", "login", "password"}, message = "name, surname, login, password  must be input") UserDto userDto) throws ServiceException;

    UserDto update(@ValidDtoParam(paramNames = "id", message = "id must be input") UserDto userDto) throws ServiceException;

    boolean delete(@NotNull(message = "id must be input") Long id) throws ServiceException;

    UserDto getById(@NotNull(message = "id must be input") Long id);

    List<UserDto> getAll();
}
