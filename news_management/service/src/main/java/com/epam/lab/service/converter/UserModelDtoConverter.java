package com.epam.lab.service.converter;

import com.epam.lab.dto.UserDto;
import com.epam.lab.model.User;
import com.epam.lab.service.util.CryptographyManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserModelDtoConverter implements DtoModelConverter<User, UserDto> {

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDto toDto(User model) {
        if (model == null) return null;
        UserDto userDto = modelMapper.map(model, UserDto.class);
        userDto.setPassword(CryptographyManager.decode(userDto.getPassword()));
        return userDto;
    }

    @Override
    public User toModel(UserDto dto) {
        if (dto == null) return null;
        User user = modelMapper.map(dto, User.class);
        user.setPassword(CryptographyManager.encode(dto.getPassword()));
        return user;
    }

}
